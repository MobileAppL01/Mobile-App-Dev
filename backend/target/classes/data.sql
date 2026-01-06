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
INSERT INTO t_user (id, password, first_name, last_name, email, phone, role, avatar)
VALUES
    ('1', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'Super', 'Admin', 'admin@test.com', '0901111111','ADMIN', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg'),
    ('2', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'John', 'Owner', 'owner@test.com', '0902222222', 'OWNER', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg'),
    ('3', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'Jane', 'Owner2', 'owner2@test.com', '0902222223', 'OWNER', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg'),
    ('4', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'Alice', 'Player', 'alice@test.com', '0903333333', 'PLAYER', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg'),
    ('5', '$2a$10$ZMVBA1/J88dw3XxJvsZa8.JlFjCQpIF6kwZHscH9k5G24K1omFN.a', 'Bob', 'Player2', 'bob@test.com', '0903333334', 'PLAYER', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg')
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 3. Insert Locations (với ảnh mẫu)
-- ==========================================
INSERT INTO location (id, name, address, description, image, rating, price_per_hour, open_time, close_time, status, owner_id)
VALUES
    (1, 'Victory Badminton Club', '123 Sport St, District 1, HCM', 'Sân cầu lông cao cấp, điều hòa mát lạnh', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', 4.5, 50000, '07:00:00', '22:00:00', 'OPEN', '2'),
    (2, 'Sân cầu lông Huy Thắng', 'Khu 12, xã Long Đức, huyện Long Thành, tỉnh Đồng Nai', 'Sân rộng rãi, giá rẻ', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', 4.2, 40000, '08:00:00', '23:00:00', 'OPEN', '2'),
    (3, 'Sân cầu lông Hưng Sport', 'Khu 15, xã Long Đức, huyện Long Thành, tỉnh Đồng Nai', 'Sân mới xây, thiết bị hiện đại', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', 4.8, 60000, '06:00:00', '22:00:00', 'OPEN', '2'),
    (4, 'Champion Badminton Center', '456 Victory Rd, District 7, HCM', 'Sân chuyên nghiệp cho thi đấu', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', 4.7, 70000, '06:00:00', '23:00:00', 'OPEN', '3')
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
-- 7. Reset Sequences (QUAN TRỌNG)
-- ==========================================
-- Cập nhật bộ đếm ID tự động (Auto Increment) để khớp với dữ liệu vừa chèn thủ công.
-- Nếu không có bước này, khi tạo mới booking bằng API sẽ bị lỗi "Duplicate Key ID" vì nó đếm lại từ 1.
SELECT setval('t_user_id_seq', (SELECT MAX(id) FROM t_user));
SELECT setval('location_id_seq', (SELECT MAX(id) FROM location));
SELECT setval('court_id_seq', (SELECT MAX(id) FROM court));
SELECT setval('booking_id_seq', (SELECT MAX(id) FROM booking));
SELECT setval('promotion_id_seq', (SELECT MAX(id) FROM promotion));

-- ==========================================
-- 8. Insert Banner Images (ảnh đầu trang danh sách sân)
-- ==========================================
INSERT INTO banner_images (id, title, description, public_id, secure_url, link_url, display_order, is_active, created_at, updated_at)
VALUES
    (1, 'Khuyến mãi mùa hè', 'Giảm giá 20% cho tất cả các sân', 'banners/summer2026', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', '/promotions', 1, true, NOW(), NOW()),
    (2, 'Sân mới khai trương', 'Champion Badminton Center - Sân chuyên nghiệp', 'banners/newcourt', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', '/locations/4', 2, true, NOW(), NOW()),
    (3, 'Đặt sân online', 'Đặt sân dễ dàng, thanh toán tiện lợi', 'banners/booking', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', '/booking', 3, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 9. Insert Location Images (gallery ảnh sân)
-- ==========================================
INSERT INTO location_images (id, location_id, public_id, secure_url, is_primary, created_at)
VALUES
    (1, 1, 'locations/1/img1', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', true, NOW()),
    (2, 1, 'locations/1/img2', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', false, NOW()),
    (3, 2, 'locations/2/img1', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', true, NOW()),
    (4, 2, 'locations/2/img2', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', false, NOW()),
    (5, 3, 'locations/3/img1', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', true, NOW()),
    (6, 4, 'locations/4/img1', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', true, NOW()),
    (7, 4, 'locations/4/img2', 'https://res.cloudinary.com/dkgggrdmf/image/upload/v1767705124/review-san-danh-cau-long-tphcm_1_tq8qgq.jpg', false, NOW())
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 10. Insert Reviews (đánh giá mẫu)
-- ==========================================
INSERT INTO review (id, user_id, location_id, rating, content, created_at)
VALUES
    (1, 4, 1, 5, 'Sân rất đẹp, nhân viên thân thiện!', NOW()),
    (2, 5, 1, 4, 'Sân tốt, giá hợp lý', NOW()),
    (3, 4, 2, 4, 'Sân rộng rãi, thoáng mát', NOW()),
    (4, 5, 3, 5, 'Thiết bị hiện đại, rất thích!', NOW())
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 11. Insert Review Comments (bình luận mẫu)
-- ==========================================
INSERT INTO review_comment (id, review_id, user_id, content, parent_comment_id, created_at)
VALUES
    (1, 1, 2, 'Cảm ơn bạn đã đánh giá!', NULL, NOW()),
    (2, 1, 4, 'Sẽ quay lại lần nữa', NULL, NOW()),
    (3, 1, 5, 'Đồng ý với bạn!', 2, NOW()),
    (4, 2, 2, 'Cảm ơn bạn, hẹn gặp lại!', NULL, NOW())
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 12. Reset thêm sequences cho các bảng mới
-- ==========================================
SELECT setval('banner_images_id_seq', (SELECT COALESCE(MAX(id), 1) FROM banner_images));
SELECT setval('location_images_id_seq', (SELECT COALESCE(MAX(id), 1) FROM location_images));
SELECT setval('review_id_seq', (SELECT COALESCE(MAX(id), 1) FROM review));
SELECT setval('review_comment_id_seq', (SELECT COALESCE(MAX(id), 1) FROM review_comment));

