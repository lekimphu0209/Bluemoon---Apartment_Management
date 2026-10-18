-- =====================================================================
-- V1__baseline.sql — Schema hiện tại của dự án (do Hibernate ddl-auto
-- tạo ra từ các entity cũ). File này phục vụ DB MỚI chạy lần đầu.
-- DB cũ đã có dữ liệu sẽ được Flyway đánh dấu "đã áp dụng V1" nhờ
-- spring.flyway.baseline-on-migrate=true (baseline-version=1),
-- rồi chạy tiếp V2..V6 để chuẩn hóa.
--
-- Lưu ý: các tên cột/bảng phản ánh đúng schema Hibernate đã tạo
-- (camelCase -> snake_case, không có CHECK, không có FK cho
-- các bảng @ElementCollection/@ManyToMany).
-- =====================================================================

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NULL,
  password VARCHAR(255) NULL,
  role VARCHAR(255) NULL,
  activation BIT(1) NOT NULL DEFAULT b'1',
  resident_id BIGINT NULL,
  date_created VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE apartments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  apartment_number VARCHAR(255) NOT NULL,
  room_number VARCHAR(255) NOT NULL,
  floor INT NULL,
  area DOUBLE NULL,
  status VARCHAR(255) NULL,
  type VARCHAR(255) NULL,
  CONSTRAINT uq_apartments_number UNIQUE (apartment_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE residents (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(255) NULL,
  age BIGINT NULL,
  gender VARCHAR(255) NULL,
  role VARCHAR(255) NULL,
  status VARCHAR(255) NULL,
  CONSTRAINT uq_residents_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng collection của Resident.apartmentNumbers (@ElementCollection)
CREATE TABLE resident_apartment_numbers (
  resident_id BIGINT NOT NULL,
  apartment_number VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng collection của Apartment.billIds / residentIds (@ElementCollection)
CREATE TABLE apartment_bills (
  apartment_id BIGINT NOT NULL,
  bill_id BIGINT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE apartment_residents (
  apartment_id BIGINT NOT NULL,
  resident_id BIGINT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE bills (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  apartment_number VARCHAR(255) NOT NULL,
  bill_type VARCHAR(255) NOT NULL,
  amount DOUBLE NOT NULL,
  due_date DATE NULL,
  description VARCHAR(255) NULL,
  status VARCHAR(255) NOT NULL,
  created_at DATETIME(6) NULL,
  qr_code_url VARCHAR(255) NULL,
  transaction_id VARCHAR(255) NULL,
  last_check_time DATETIME(6) NULL,
  payment_error VARCHAR(255) NULL,
  payment_reference_code VARCHAR(255) NULL,
  invoice_id BIGINT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE notifications (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  resident_id BIGINT NOT NULL,
  message VARCHAR(255) NOT NULL,
  link_header VARCHAR(255) NULL,
  link_api VARCHAR(255) NULL,
  is_read BIT(1) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_notifications_resident FOREIGN KEY (resident_id) REFERENCES residents(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE manual_notifications (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  message VARCHAR(255) NOT NULL,
  created_at DATETIME(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE manual_notification_residents (
  manual_notification_id BIGINT NOT NULL,
  resident_id BIGINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE apartment_fee_units (
  id BIGINT NOT NULL PRIMARY KEY,
  apartment_price_per_m2 BIGINT NOT NULL,
  service_fee_per_m2 BIGINT NOT NULL,
  motorbike_parking_fee_by_month BIGINT NOT NULL,
  motorbike_parking_fee_by_hour BIGINT NOT NULL,
  car_parking_fee_by_month BIGINT NOT NULL,
  car_parking_fee_by_hour BIGINT NOT NULL,
  water_fee_per_m3 BIGINT NOT NULL,
  electricity_fee_per_kwh BIGINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
