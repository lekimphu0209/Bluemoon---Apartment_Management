-- =====================================================================
-- V5__indexes.sql — Chỉ mục phục vụ truy vấn theo đặc tả mục 5.
-- =====================================================================

-- fee_records: lọc trạng thái + kỳ (dashboard, thống kê)
CREATE INDEX idx_fee_records_status_period   ON fee_records (status, period);
-- fee_records: khoản thu gần đây của căn hộ
CREATE INDEX idx_fee_records_apartment_period ON fee_records (apartment_id, period);
-- fee_records: thống kê theo khoản thu
CREATE INDEX idx_fee_records_feetype_period  ON fee_records (fee_type_id, period);

-- residents: danh sách theo căn hộ, lọc trạng thái, tìm theo tên
-- (apartment_id đã có index của FK; tạo thêm tên rõ ràng cho consistency)
CREATE INDEX idx_residents_apartment   ON residents (apartment_id);
CREATE INDEX idx_residents_status      ON residents (residency_status);
CREATE INDEX idx_residents_fullname    ON residents (full_name);

-- residency_events: lịch sử / lọc biến động
CREATE INDEX idx_events_resident_date  ON residency_events (resident_id, event_date);
CREATE INDEX idx_events_apartment_date ON residency_events (apartment_id, event_date);
CREATE INDEX idx_events_type_date      ON residency_events (type, event_date);

-- apartments: lọc căn hộ, sinh bản ghi thu
CREATE INDEX idx_apartments_status     ON apartments (status);

-- fee_types: lọc danh sách khoản thu
CREATE INDEX idx_fee_types_type_status ON fee_types (type, status);
