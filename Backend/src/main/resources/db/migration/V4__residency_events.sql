-- =====================================================================
-- V4__residency_events.sql — Bảng biến động nhân khẩu theo đặc tả 4.4.
-- =====================================================================

CREATE TABLE residency_events (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  resident_id BIGINT NOT NULL,
  apartment_id BIGINT NOT NULL,
  type VARCHAR(20) NOT NULL,
  event_date DATE NOT NULL,
  from_date DATE NULL,
  to_date DATE NULL,
  destination VARCHAR(255) NULL,
  note VARCHAR(500) NULL,
  created_by BIGINT NOT NULL,
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  CONSTRAINT fk_events_resident FOREIGN KEY (resident_id) REFERENCES residents(id) ON DELETE CASCADE,
  CONSTRAINT fk_events_apartment FOREIGN KEY (apartment_id) REFERENCES apartments(id),
  CONSTRAINT fk_events_user FOREIGN KEY (created_by) REFERENCES users(id),
  CONSTRAINT ck_events_type CHECK (type IN ('MOVE_IN','MOVE_OUT','TEMP_RESIDENCE','TEMP_ABSENCE','INFO_CHANGE')),
  CONSTRAINT ck_events_range CHECK (from_date IS NULL OR to_date IS NULL OR to_date >= from_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- BỔ SUNG: backfill sự kiện MOVE_IN cho resident đang ở trong dữ liệu
-- cũ (dữ liệu legacy không ghi biến động). created_by = admin đầu tiên.
-- Chỉ chạy khi có ít nhất 1 user để thỏa FK created_by.
INSERT INTO residency_events (resident_id, apartment_id, type, event_date, note, created_by)
SELECT r.id, r.apartment_id, 'MOVE_IN', CURDATE(),
       'Backfill từ dữ liệu cũ (V4)',
       (SELECT COALESCE(
          (SELECT id FROM users WHERE role = 'ADMIN' ORDER BY id LIMIT 1),
          (SELECT MIN(id) FROM users)))
FROM residents r
WHERE r.residency_status <> 'MOVED_OUT'
  AND EXISTS (SELECT 1 FROM users);
