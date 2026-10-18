-- =====================================================================
-- V3__fee_types_records.sql — Tạo fee_types, fee_records theo đặc tả
-- 4.5/4.6 và ánh xạ dữ liệu bảng bills cũ sang fee_records.
-- =====================================================================

-- Đồng nhất collation trước khi join: DB cũ do Hibernate tạo dùng
-- utf8mb4_0900_ai_ci (mặc định MySQL 8), còn bảng migration dùng
-- utf8mb4_unicode_ci -> "Illegal mix of collations" khi so sánh
-- f.name = b.bill_type hay a.number = b.apartment_number.
-- No-op trên fresh install (bảng V1 đã là utf8mb4_unicode_ci).
ALTER TABLE bills CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE apartments CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE fee_types (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  type VARCHAR(30) NOT NULL,                       -- SERVICE, MANAGEMENT, DONATION
  calc_method VARCHAR(30) NOT NULL,                -- PER_AREA, VOLUNTARY
  rate DECIMAL(15,2) NULL,                         -- đ/m²/tháng (PER_AREA)
  suggested_amount DECIMAL(15,2) NULL,             -- gợi ý cho khoản đóng góp
  start_date DATE NULL,
  end_date DATE NULL,
  -- Boolean entity -> Hibernate expect BIT(1) (khop baseline Hibernate tao)
  mandatory BIT(1) NOT NULL DEFAULT b'1',
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  description VARCHAR(500) NULL,
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  CONSTRAINT uq_fee_types_name UNIQUE (name),
  CONSTRAINT ck_fee_types_type CHECK (type IN ('SERVICE','MANAGEMENT','DONATION')),
  CONSTRAINT ck_fee_types_calc CHECK (calc_method IN ('PER_AREA','VOLUNTARY')),
  CONSTRAINT ck_fee_types_status CHECK (status IN ('ACTIVE','INACTIVE')),
  CONSTRAINT ck_fee_types_rate CHECK (rate IS NULL OR rate > 0),
  CONSTRAINT ck_fee_types_dates CHECK (start_date IS NULL OR end_date IS NULL OR end_date >= start_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE fee_records (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  apartment_id BIGINT NOT NULL,
  fee_type_id BIGINT NOT NULL,
  -- VARCHAR(7): Hibernate expect VARCHAR cho String (CHAR bi validate mismatch)
  period VARCHAR(7) NOT NULL,                       -- 2026-10
  amount DECIMAL(15,2) NULL,                       -- NULL = chưa nhập (v2.0 điện, nước)
  status VARCHAR(10) NOT NULL DEFAULT 'UNPAID',
  contribution BIT(1) NOT NULL DEFAULT b'0',       -- 1 = đóng góp ghi nhận trực tiếp
  paid_date DATE NULL,
  collected_by BIGINT NULL,
  note VARCHAR(500) NULL,
  dedup_key VARCHAR(60) GENERATED ALWAYS AS
    (IF(contribution = 0, CONCAT(apartment_id,'-',fee_type_id,'-',period), NULL)) STORED,
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  CONSTRAINT uq_fee_records_dedup UNIQUE (dedup_key),
  CONSTRAINT fk_fr_apartment FOREIGN KEY (apartment_id) REFERENCES apartments(id),
  CONSTRAINT fk_fr_fee_type FOREIGN KEY (fee_type_id) REFERENCES fee_types(id),
  CONSTRAINT fk_fr_user FOREIGN KEY (collected_by) REFERENCES users(id),
  CONSTRAINT ck_fr_status CHECK (status IN ('UNPAID','PAID')),
  CONSTRAINT ck_fr_period CHECK (period REGEXP '^[0-9]{4}-(0[1-9]|1[0-2])$'),
  CONSTRAINT ck_fr_paid CHECK (
    (status = 'PAID' AND paid_date IS NOT NULL AND collected_by IS NOT NULL AND amount IS NOT NULL)
    OR (status = 'UNPAID' AND paid_date IS NULL))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- Ánh xạ dữ liệu bills cũ -> fee_types + fee_records
-- ---------------------------------------------------------------------

-- Người thu mặc định cho các bản ghi PAID cũ: user ADMIN đầu tiên,
-- fallback user đầu tiên. (ck_fr_paid bắt buộc collected_by khi PAID)
SET @collector := (
  SELECT COALESCE(
    (SELECT id FROM users WHERE role = 'ADMIN' ORDER BY id LIMIT 1),
    (SELECT MIN(id) FROM users)
  )
);

-- 1) Mỗi bill_type cũ -> một fee_type
--    BỔ SUNG: ánh xạ enum cũ sang type/calc_method của spec v1.0:
--      FIXED_COST  -> MANAGEMENT / PER_AREA
--      SERVICE_COST, ELECTRICITY, WATER, OTHER -> SERVICE / PER_AREA
--      CONTRIBUTION -> DONATION / VOLUNTARY
INSERT INTO fee_types (name, type, calc_method, rate, mandatory, status, description)
SELECT DISTINCT
  b.bill_type,
  CASE b.bill_type
    WHEN 'FIXED_COST'   THEN 'MANAGEMENT'
    WHEN 'CONTRIBUTION' THEN 'DONATION'
    ELSE 'SERVICE'
  END,
  CASE WHEN b.bill_type = 'CONTRIBUTION' THEN 'VOLUNTARY' ELSE 'PER_AREA' END,
  NULL,
  CASE WHEN b.bill_type = 'CONTRIBUTION' THEN 0 ELSE 1 END,
  'ACTIVE',
  CONCAT('Di trú từ bill_type cũ: ', b.bill_type)
FROM bills b;

-- 2) bills -> fee_records.
--    - period lấy theo due_date (fallback created_at).
--    - Bill PAID thiếu paid_date/collected_by -> gán due_date hoặc
--      ngày tạo, collected_by = @collector.
--    - Bill CONTRIBUTION -> contribution = 1 (không chịu dedup).
--    - Các bill trùng (apartment, bill_type, period) chỉ giữ bản ghi
--      đầu tiên để không vi phạm uq_fee_records_dedup.
INSERT INTO fee_records
  (apartment_id, fee_type_id, period, amount, status, contribution,
   paid_date, collected_by, note, created_at, updated_at)
SELECT
  a.id,
  f.id,
  DATE_FORMAT(COALESCE(b.due_date, b.created_at), '%Y-%m'),
  b.amount,
  b.status,
  IF(b.bill_type = 'CONTRIBUTION', 1, 0),
  IF(b.status = 'PAID', COALESCE(b.due_date, DATE(b.created_at), CURDATE()), NULL),
  IF(b.status = 'PAID', @collector, NULL),
  CONCAT('legacy_bills#', b.id),
  COALESCE(b.created_at, NOW(3)),
  NOW(3)
FROM bills b
JOIN apartments a ON a.number = b.apartment_number
JOIN fee_types f ON f.name = b.bill_type
WHERE b.bill_type = 'CONTRIBUTION'
   OR b.id = (
     SELECT MIN(b2.id)
     FROM bills b2
     JOIN apartments a2 ON a2.number = b2.apartment_number
     WHERE a2.id = a.id
       AND b2.bill_type = b.bill_type
       AND DATE_FORMAT(COALESCE(b2.due_date, b2.created_at), '%Y-%m')
           = DATE_FORMAT(COALESCE(b.due_date, b.created_at), '%Y-%m')
       AND b2.bill_type <> 'CONTRIBUTION'
   );

-- Giữ bảng cũ làm archive (BỔ SUNG): đổi tên thay vì xóa để truy vết
-- nếu cần; xóa hẳn ở migration sau khi xác nhận dữ liệu đã đúng.
RENAME TABLE bills TO legacy_bills;
