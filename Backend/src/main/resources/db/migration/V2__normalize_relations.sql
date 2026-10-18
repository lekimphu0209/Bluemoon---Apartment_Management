-- =====================================================================
-- V2__normalize_relations.sql — Chuẩn hóa quan hệ:
--   * users: thêm full_name, email, password_hash, +TS theo đặc tả 4.1
--            (các cột cũ name/password/role/activation/resident_id
--             giữ lại cho code hiện tại — BỔ SUNG ngoài spec, sẽ dọn
--             bằng migration sau khi entity refactor xong)
--   * apartments: apartment_number -> number, chuẩn kiểu floor/area,
--            map status VACANT/RENT -> EMPTY/OCCUPIED, +TS, CHECK
--   * residents: apartment_id FK thay cho @ElementCollection,
--            thêm birth_date, id_card, relationship, residency_status,
--            cột sinh head_apartment_id, +TS, CHECK
--   * Xóa các bảng collection: resident_apartment_numbers,
--     apartment_residents, apartment_bills
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1) apartments: đổi tên cột khóa trước (để các bước join bên dưới dùng
--    `number`), sau đó chuẩn hóa kiểu + ràng buộc.
-- ---------------------------------------------------------------------
ALTER TABLE apartments RENAME COLUMN apartment_number TO number;

-- DB cũ do Hibernate tạo có thể là ENUM('OCCUPIED','RENT','VACANT') ->
-- phải mở rộng sang VARCHAR trước, nếu không giá trị 'EMPTY' bị MySQL
-- reject ("Data truncated for column 'status'"). An toàn cả khi cột đã
-- là VARCHAR (fresh install từ V1) vì MODIFY là no-op về mặt dữ liệu.
ALTER TABLE apartments MODIFY COLUMN status VARCHAR(255) NULL;

-- Dọn dữ liệu cũ không hợp lệ trước khi siết ràng buộc
UPDATE apartments SET floor = 1 WHERE floor IS NULL OR floor < 1;
UPDATE apartments SET floor = 30 WHERE floor > 30;
UPDATE apartments SET area = 1 WHERE area IS NULL OR area <= 0;
UPDATE apartments SET status = 'EMPTY' WHERE status IS NULL OR status = 'VACANT';
UPDATE apartments SET status = 'OCCUPIED' WHERE status = 'RENT';

-- floor/area: Hibernate map Integer -> INT, Double -> FLOAT/DOUBLE.
-- (DECIMAL/TINYINT UNSIGNED se bi ddl-auto=validate reject).
ALTER TABLE apartments
  MODIFY COLUMN number VARCHAR(20) NOT NULL,
  MODIFY COLUMN floor INT NOT NULL,
  MODIFY COLUMN area DOUBLE NOT NULL,
  MODIFY COLUMN status VARCHAR(20) NOT NULL DEFAULT 'EMPTY',
  ADD COLUMN created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  ADD COLUMN updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  DROP COLUMN room_number,
  DROP COLUMN type,
  ADD CONSTRAINT ck_apartments_floor CHECK (floor BETWEEN 1 AND 30),
  ADD CONSTRAINT ck_apartments_area CHECK (area > 0),
  ADD CONSTRAINT ck_apartments_status CHECK (status IN ('OCCUPIED','EMPTY'));

-- ---------------------------------------------------------------------
-- 2) residents: thêm cột mới rồi chuyển dữ liệu cũ
-- ---------------------------------------------------------------------
ALTER TABLE residents
  ADD COLUMN birth_date DATE NULL,
  ADD COLUMN id_card VARCHAR(20) NULL,
  ADD COLUMN apartment_id BIGINT NULL,
  ADD COLUMN relationship VARCHAR(20) NOT NULL DEFAULT 'OTHER',
  ADD COLUMN residency_status VARCHAR(20) NOT NULL DEFAULT 'LIVING',
  ADD COLUMN created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  ADD COLUMN updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  MODIFY COLUMN gender VARCHAR(10) NULL,
  MODIFY COLUMN email VARCHAR(150) NULL;

-- Ánh xạ trạng thái cũ (enum ResidentStatus) sang residency_status mới
UPDATE residents SET residency_status = 'LIVING'         WHERE status = 'THUONGTRU';
UPDATE residents SET residency_status = 'TEMP_RESIDENCE' WHERE status = 'TAMTRU';
UPDATE residents SET residency_status = 'TEMP_ABSENCE'   WHERE status = 'TAMVANG';

-- Chuyển quan hệ resident->apartment: lấy apartment đầu tiên theo
-- bảng collection cũ (resident_apartment_numbers lưu apartment_number)
UPDATE residents r
JOIN resident_apartment_numbers ran ON ran.resident_id = r.id
JOIN apartments a ON a.number = ran.apartment_number
SET r.apartment_id = a.id;

-- Fallback: resident liên kết qua bảng apartment_residents
UPDATE residents r
JOIN apartment_residents ar ON ar.resident_id = r.id
SET r.apartment_id = ar.apartment_id
WHERE r.apartment_id IS NULL;

-- Resident mồ côi (không thuộc căn hộ nào, vd: resident phụ của tài
-- khoản admin do AdminUserInitializer tạo) -> gán vào căn hộ giữ chỗ
-- 'N/A' để thỏa ràng buộc NOT NULL của spec.
INSERT INTO apartments (number, floor, area, status)
SELECT 'N/A', 1, 1, 'EMPTY'
WHERE EXISTS (SELECT 1 FROM residents WHERE apartment_id IS NULL)
  AND NOT EXISTS (SELECT 1 FROM apartments WHERE number = 'N/A');

UPDATE residents
SET apartment_id = (SELECT id FROM apartments WHERE number = 'N/A')
WHERE apartment_id IS NULL;

-- Dữ liệu cũ chưa có CCCD -> sinh placeholder duy nhất trước khi siết NOT NULL
UPDATE residents SET id_card = CONCAT('LEGACY-', id) WHERE id_card IS NULL OR id_card = '';

ALTER TABLE residents
  MODIFY COLUMN id_card VARCHAR(20) NOT NULL,
  MODIFY COLUMN apartment_id BIGINT NOT NULL,
  ADD COLUMN head_apartment_id BIGINT GENERATED ALWAYS AS
    (IF(relationship = 'HEAD' AND residency_status <> 'MOVED_OUT', apartment_id, NULL)) STORED,
  ADD CONSTRAINT uq_residents_id_card UNIQUE (id_card),
  ADD CONSTRAINT uq_residents_one_head UNIQUE (head_apartment_id),
  ADD CONSTRAINT fk_residents_apartment FOREIGN KEY (apartment_id) REFERENCES apartments(id),
  ADD CONSTRAINT ck_residents_rel CHECK (relationship IN ('HEAD','SPOUSE','CHILD','PARENT','OTHER')),
  ADD CONSTRAINT ck_residents_status CHECK (residency_status IN ('LIVING','TEMP_RESIDENCE','TEMP_ABSENCE','MOVED_OUT')),
  DROP COLUMN age,
  DROP COLUMN role,
  DROP COLUMN status;

-- Cột age của schema cũ bị bỏ: spec dùng birth_date (không suy ra được
-- từ tuổi một cách chính xác), cần nhập lại tay nếu muốn giữ.

-- ---------------------------------------------------------------------
-- 3) users: bổ sung các cột theo đặc tả 4.1 (auth theo email)
-- ---------------------------------------------------------------------
ALTER TABLE users
  ADD COLUMN full_name VARCHAR(100) NULL,
  ADD COLUMN email VARCHAR(150) NULL,
  ADD COLUMN password_hash VARCHAR(100) NULL,
  ADD COLUMN created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  ADD COLUMN updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3);

-- Di trú dữ liệu: ưu tiên lấy từ resident liên kết, fallback username
UPDATE users u
LEFT JOIN residents r ON r.id = u.resident_id
SET u.full_name = COALESCE(r.full_name, u.name, 'User'),
    u.password_hash = COALESCE(u.password, ''),
    u.email = COALESCE(r.email, CONCAT(COALESCE(u.name, 'user'), '-', u.id, '@bluemoon.local'));

ALTER TABLE users
  MODIFY COLUMN full_name VARCHAR(100) NOT NULL,
  MODIFY COLUMN email VARCHAR(150) NOT NULL,
  MODIFY COLUMN password_hash VARCHAR(100) NOT NULL,
  ADD CONSTRAINT uq_users_email UNIQUE (email);

-- ---------------------------------------------------------------------
-- 4) Xóa các bảng collection đã được chuẩn hóa
-- ---------------------------------------------------------------------
DROP TABLE resident_apartment_numbers;
DROP TABLE apartment_residents;
DROP TABLE apartment_bills;
