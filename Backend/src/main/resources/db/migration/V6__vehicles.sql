-- =====================================================================
-- V6__vehicles.sql (v2.0) — Bảng vehicles + mở rộng CHECK của fee_types.
-- =====================================================================

CREATE TABLE vehicles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  apartment_id BIGINT NOT NULL,
  type VARCHAR(20) NOT NULL,                       -- MOTORBIKE, CAR
  plate VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',    -- ACTIVE, INACTIVE (hủy đăng ký)
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  CONSTRAINT uq_vehicles_plate UNIQUE (plate),
  CONSTRAINT fk_vehicles_apartment FOREIGN KEY (apartment_id) REFERENCES apartments(id),
  CONSTRAINT ck_vehicles_type CHECK (type IN ('MOTORBIKE','CAR')),
  CONSTRAINT ck_vehicles_status CHECK (status IN ('ACTIVE','INACTIVE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_vehicles_apartment ON vehicles (apartment_id, status);

-- Mở rộng enum của fee_types cho v2.0: phí xe (PER_VEHICLE) và các khoản
-- nhập tay theo căn hộ (MANUAL_PER_APARTMENT: điện, nước, internet).
ALTER TABLE fee_types DROP CHECK ck_fee_types_type;
ALTER TABLE fee_types ADD CONSTRAINT ck_fee_types_type CHECK
  (type IN ('SERVICE','MANAGEMENT','DONATION',
            'PARKING_MOTORBIKE','PARKING_CAR',
            'ELECTRICITY','WATER','INTERNET'));

ALTER TABLE fee_types DROP CHECK ck_fee_types_calc;
ALTER TABLE fee_types ADD CONSTRAINT ck_fee_types_calc CHECK
  (calc_method IN ('PER_AREA','VOLUNTARY','PER_VEHICLE','MANUAL_PER_APARTMENT'));
