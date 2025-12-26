CREATE EXTENSION IF NOT EXISTS unaccent;

-- ==========================================
-- 1. Insert Roles
-- ==========================================
INSERT INTO role (id, name, description) 
VALUES (1, 'ADMIN', 'Administrator'), (2, 'OWNER', 'Court Owner'), (3, 'PLAYER', 'Badminton Player')
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 2. Insert Users
-- ==========================================
INSERT INTO t_user (id, username, password, first_name, last_name, email, phone, user_role)
VALUES
    ('admin-1', 'admin_user', 'pass123', 'Super', 'Admin', 'admin@test.com', '0901111111', 1),
    ('owner-1', 'court_owner', 'pass123', 'John', 'Owner', 'owner@test.com', '0902222222', 2),
    ('owner-2', 'court_owner_2', 'pass123', 'Jane', 'Owner2', 'owner2@test.com', '0902222223', 2),
    ('player-1', 'badminton_player', 'pass123', 'Alice', 'Player', 'alice@test.com', '0903333333', 3),
    ('player-2', 'badminton_player_2', 'pass123', 'Bob', 'Player2', 'bob@test.com', '0903333334', 3)
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 3. Insert Locations
-- ==========================================
INSERT INTO location (id, name, address, description, image, rating, price_per_hour, open_time, close_time, status, owner_id)
VALUES
    (1, 'Victory Badminton Club', '123 Sport St, District 1, HCM', 'Sân cầu lông cao cấp, điều hòa mát lạnh', 'https://example.com/victory.jpg', 4.5, 50000, '07:00:00', '22:00:00', 'OPEN', 'owner-1'),
    (2, 'Sân cầu lông Huy Thắng', 'Khu 12, xã Long Đức, huyện Long Thành, tỉnh Đồng Nai', 'Sân rộng rãi, giá rẻ', 'https://example.com/huythang.jpg', 4.2, 40000, '08:00:00', '23:00:00', 'OPEN', 'owner-1'),
    (3, 'Sân cầu lông Hưng Sport', 'Khu 15, xã Long Đức, huyện Long Thành, tỉnh Đồng Nai', 'Sân mới xây, thiết bị hiện đại', 'https://example.com/hungsport.jpg', 4.8, 60000, '06:00:00', '22:00:00', 'OPEN', 'owner-1'),
    (4, 'Champion Badminton Center', '456 Victory Rd, District 7, HCM', 'Sân chuyên nghiệp cho thi đấu', 'https://example.com/champion.jpg', 4.7, 70000, '06:00:00', '23:00:00', 'OPEN', 'owner-2')
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 4. Insert Courts
-- ==========================================
INSERT INTO court (id, name, status, deleted, location_id)
VALUES
    (1, 'Sân số 1', 'ACTIVE', false, 1),
    (2, 'Sân số 2', 'ACTIVE', false, 1),
    (3, 'Sân VIP', 'MAINTENANCE', false, 1),
    (4, 'Sân A', 'ACTIVE', false, 2),
    (5, 'Sân B', 'ACTIVE', false, 2),
    (6, 'Court 1', 'ACTIVE', false, 3),
    (7, 'Court 2', 'ACTIVE', false, 3),
    (8, 'Court 3', 'ACTIVE', false, 3),
    (9, 'Pro Court 1', 'ACTIVE', false, 4),
    (10, 'Pro Court 2', 'ACTIVE', false, 4)
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 5. Insert Bookings
-- ==========================================
INSERT INTO booking (id, booking_date, start_hours, start_time_slot, end_time_slot, total_price, status, payment_method, created_at, updated_at, version, player_id, court_id)
VALUES
    (1, '2025-12-09', '[8, 9]', 8, 10, 100000, 'CONFIRMED', 'CASH', '2025-12-08 10:00:00', '2025-12-08 10:00:00', 0, 'player-1', 1),
    (2, '2025-12-09', '[13, 14]', 13, 15, 100000, 'PENDING', 'VNPAY', '2025-12-08 11:00:00', '2025-12-08 11:00:00', 0, 'player-1', 1),
    (3, '2025-12-09', '[17, 18]', 17, 19, 100000, 'CONFIRMED', 'CASH', '2025-12-08 12:00:00', '2025-12-08 12:00:00', 0, 'player-2', 2),
    (4, '2025-12-10', '[8, 9]', 8, 10, 80000, 'PENDING', 'CASH', '2025-12-08 13:00:00', '2025-12-08 13:00:00', 0, 'player-1', 4),
    (5, '2025-12-10', '[14, 15]', 14, 16, 80000, 'COMPLETED', 'VNPAY', '2025-12-07 10:00:00', '2025-12-07 10:00:00', 0, 'player-2', 5),
    (6, '2025-12-08', '[9, 10]', 9, 11, 100000, 'COMPLETED', 'CASH', '2025-12-06 10:00:00', '2025-12-06 10:00:00', 0, 'player-1', 1),
    (7, '2025-12-07', '[15, 16]', 15, 17, 100000, 'CANCELED', 'CASH', '2025-12-05 10:00:00', '2025-12-05 10:00:00', 0, 'player-2', 2)
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 6. Insert Promotions
-- ==========================================
INSERT INTO promotion (id, location_id, code, discount_type, discount_value, start_date, end_date, description, active)
VALUES
    (1, 1, 'WELCOME10', 'PERCENT', 10, '2025-12-01', '2025-12-31', 'Giảm 10% cho khách hàng mới', true),
    (2, 1, 'NEWYEAR2026', 'FIXED', 20000, '2025-12-25', '2026-01-05', 'Giảm 20k dịp năm mới', true),
    (3, 2, 'SUMMER25', 'PERCENT', 25, '2025-06-01', '2025-08-31', 'Giảm giá mùa hè', false)
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 7. Reset Sequences (QUAN TRỌNG)
-- ==========================================
-- Cập nhật bộ đếm ID tự động (Auto Increment) để khớp với dữ liệu vừa chèn thủ công.
-- Nếu không có bước này, khi tạo mới booking bằng API sẽ bị lỗi "Duplicate Key ID" vì nó đếm lại từ 1.
SELECT setval('role_seq', (SELECT MAX(id) FROM role));
SELECT setval('location_id_seq', (SELECT MAX(id) FROM location));
SELECT setval('court_id_seq', (SELECT MAX(id) FROM court));
SELECT setval('booking_id_seq', (SELECT MAX(id) FROM booking));
SELECT setval('promotion_id_seq', (SELECT MAX(id) FROM promotion));