CREATE EXTENSION IF NOT EXISTS unaccent;
-- ==========================================
-- 1. Insert Roles
-- ==========================================
-- INSERT INTO role (id, name, description)
-- VALUES (1, 'ADMIN', 'Administrator'), (2, 'OWNER', 'Court Owner'), (3, 'PLAYER', 'Badminton Player')
-- ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 2. Insert Users (với avatar)
-- ==========================================
INSERT INTO t_user (id, password, full_name, email, phone, role)
VALUES
    ('1', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'Super Admin', 'admin@test.com', '0901111111','ADMIN'),
    ('2', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'John Owner', 'owner@test.com', '0902222222', 'OWNER'),
    ('3', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'Jane Owner2', 'owner2@test.com', '0902222223', 'OWNER'),
    ('4', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'Alice Player', 'alice@test.com', '0903333333', 'PLAYER'),
    ('5', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'Bob Player2', 'bob@test.com', '0903333334', 'PLAYER'),
    ('6', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'Charlie Owner3', 'owner3@test.com', '0904444444', 'OWNER'),
    ('7', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'David Owner4', 'owner4@test.com', '0905555555', 'OWNER')
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 3. Insert Locations (với ảnh mẫu)
-- ==========================================
INSERT INTO location (id, name, address, description, image, rating, price_per_hour, open_time, close_time, status, owner_id)
VALUES
    (1, 'Victory Badminton Club', '123 Sport St, Quận 1, Thành phố Hồ Chí Minh', 'Sân cầu lông cao cấp, điều hòa mát lạnh, ngay trung tâm Quận 1.', 'https://example.com/victory.jpg', 4.5, 50000, '07:00:00', '22:00:00', 'OPEN', '2'),
    (2, 'Sân cầu lông Huy Thắng', 'Khu 12, xã Long Đức, Huyện Long Thành, Tỉnh Đồng Nai', 'Sân rộng rãi, thoáng mát, chỗ để xe rộng rãi.', 'https://example.com/huythang.jpg', 4.2, 40000, '08:00:00', '23:00:00', 'OPEN', '2'),
    (3, 'Sân cầu lông Hưng Sport', 'QL51, Thị trấn Long Thành, Huyện Long Thành, Tỉnh Đồng Nai', 'Sân mới xây, sàn thảm đạt chuẩn thi đấu.', 'https://example.com/hungsport.jpg', 4.8, 60000, '06:00:00', '22:00:00', 'OPEN', '2'),
    (4, 'Champion Badminton Center', '456 Victory Rd, Quận 7, Thành phố Hồ Chí Minh', 'Sân chuyên nghiệp cho thi đấu giải, ánh sáng tốt.', 'https://example.com/champion.jpg', 4.7, 70000, '06:00:00', '23:00:00', 'OPEN', '3'),
    (5, 'Sân Cầu Lông Lan Anh', '789 CMT8, Quận 10, Thành phố Hồ Chí Minh', 'Sân thoáng mát, giá bình dân, phù hợp sinh viên.', 'https://example.com/lananh.jpg', 4.3, 45000, '06:00:00', '23:00:00', 'OPEN', '2'),
    (6, 'Sân Cầu Lông 19/5', '19/5 Street, Thành phố Thủ Đức, Thành phố Hồ Chí Minh', 'Sân rộng, nhiều tiện ích, bãi xe rộng, có căn tin.', 'https://example.com/195.jpg', 4.6, 55000, '05:30:00', '22:30:00', 'OPEN', '3'),
    (7, 'Hanoi Smash Club', '123 Xuân Thủy, Quận Cầu Giấy, Thành phố Hà Nội', 'Câu lạc bộ cầu lông hàng đầu Cầu Giấy.', 'https://example.com/hanoi.jpg', 4.9, 80000, '06:00:00', '22:00:00', 'OPEN', '6'),
    (8, 'Da Nang Badminton', '456 Lê Duẩn, Quận Hải Châu, Thành phố Đà Nẵng', 'Sân cầu lông hiện đại giữa lòng Đà Nẵng.', 'https://example.com/danang.jpg', 4.5, 60000, '05:00:00', '21:00:00', 'OPEN', '7'),
    (9, 'Aeon Binh Duong Court', 'Đại Lộ Bình Dương, Thành phố Thuận An, Tỉnh Bình Dương', 'Sân cầu lông gần Aeon Mall, tiện lợi mua sắm.', 'https://example.com/binhduong.jpg', 4.4, 45000, '07:00:00', '23:00:00', 'OPEN', '6'),
    (10, 'Sân Cầu Lông BK', '268 Lý Thường Kiệt, Quận 10, Thành phố Hồ Chí Minh', 'Sân trong khuôn viên ĐHBK, rất đông vui.', 'https://example.com/bk.jpg', 4.2, 40000, '06:00:00', '21:00:00', 'OPEN', '3')
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
    (10, 'Pro Court 2', 'ACTIVE', false, 4),
    (11, 'Sân Lan Anh 1', 'ACTIVE', false, 5),
    (12, 'Sân Lan Anh 2', 'ACTIVE', false, 5),
    (13, 'Sân 19/5 A', 'ACTIVE', false, 6),
    (14, 'Sân 19/5 B', 'ACTIVE', false, 6),
    (15, 'Sân HN 1', 'ACTIVE', false, 7),
    (16, 'Sân HN 2', 'ACTIVE', false, 7),
    (17, 'Sân DN 1', 'ACTIVE', false, 8),
    (18, 'Sân BD 1', 'ACTIVE', false, 9),
    (19, 'Sân BK 1', 'ACTIVE', false, 10),
    (20, 'Sân BK 2', 'ACTIVE', false, 10)
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 5. Insert Bookings
-- ==========================================
INSERT INTO booking (id, booking_date, start_hours, start_time_slot, end_time_slot, total_price, status, payment_method, created_at, updated_at, version, player_id, court_id)
VALUES
    (1, '2025-12-09', '[8, 9]', 8, 10, 100000, 'CONFIRMED', 'CASH', '2025-12-08 10:00:00', '2025-12-08 10:00:00', 0, 4, 1),
    (2, '2025-12-09', '[13, 14]', 13, 15, 100000, 'PENDING', 'VNPAY', '2025-12-08 11:00:00', '2025-12-08 11:00:00', 0, 4, 1),
    (3, '2025-12-09', '[17, 18]', 17, 19, 100000, 'CONFIRMED', 'CASH', '2025-12-08 12:00:00', '2025-12-08 12:00:00', 0, 5, 2),
    (4, '2025-12-10', '[8, 9]', 8, 10, 80000, 'PENDING', 'CASH', '2025-12-08 13:00:00', '2025-12-08 13:00:00', 0, 4, 4),
    (5, '2025-12-10', '[14, 15]', 14, 16, 80000, 'COMPLETED', 'VNPAY', '2025-12-07 10:00:00', '2025-12-07 10:00:00', 0, 5, 5),
    (6, '2025-12-08', '[9, 10]', 9, 11, 100000, 'COMPLETED', 'CASH', '2025-12-06 10:00:00', '2025-12-06 10:00:00', 0, 4, 1),
    (7, '2025-12-07', '[15, 16]', 15, 17, 100000, 'CANCELED', 'CASH', '2025-12-05 10:00:00', '2025-12-05 10:00:00', 0, 5, 2)
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
-- 7. Insert Reviews & Comments
-- ==========================================
INSERT INTO review (id, user_id, location_id, rating, content, created_at)
VALUES
    (1, 4, 1, 5, 'Sân rất đẹp, nhân viên thân thiện.', '2025-12-01 10:00:00'),
    (2, 5, 1, 4, 'Sân hơi trơn một chút nhưng tổng thể ok.', '2025-12-02 11:00:00'),
    (3, 4, 2, 3, 'Giá rẻ nhưng không gian hơi nóng.', '2025-12-03 14:00:00'),
    (4, 5, 5, 5, 'Sân mới, đánh rất sướng.', '2025-12-05 09:00:00')
ON CONFLICT (id) DO NOTHING;

INSERT INTO review_comment (id, review_id, user_id, parent_comment_id, content, created_at)
VALUES
    (1, 1, 2, NULL, 'Cảm ơn bạn đã ủng hộ sân!', '2025-12-01 10:30:00'),
    (2, 1, 4, 1, 'Dạ vâng ạ ^^', '2025-12-01 10:45:00'),
    (3, 2, 2, NULL, 'Cảm ơn bạn góp ý, bên mình sẽ khắc phục sớm.', '2025-12-02 12:00:00'),
    (4, 3, 4, NULL, 'Hy vọng lắp thêm quạt.', '2025-12-03 14:30:00')
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 8. Reset Sequences
-- ==========================================
-- Cập nhật bộ đếm ID tự động (Auto Increment) để khớp với dữ liệu vừa chèn thủ công.
-- Nếu không có bước này, khi tạo mới booking bằng API sẽ bị lỗi "Duplicate Key ID" vì nó đếm lại từ 1.
SELECT setval('t_user_id_seq', (SELECT MAX(id) FROM t_user));
SELECT setval('location_id_seq', (SELECT MAX(id) FROM location));
SELECT setval('court_id_seq', (SELECT MAX(id) FROM court));
SELECT setval('booking_id_seq', (SELECT MAX(id) FROM booking));
SELECT setval('promotion_id_seq', (SELECT MAX(id) FROM promotion));
SELECT setval('review_id_seq', (SELECT COALESCE(MAX(id), 1) FROM review));
SELECT setval('review_comment_id_seq', (SELECT COALESCE(MAX(id), 1) FROM review_comment));
