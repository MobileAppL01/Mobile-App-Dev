INSERT INTO t_user (id, username, password, first_name, last_name, email, phone, user_role)
VALUES
    ('user-uuid-1', 'admin_user', 'pass123', 'Super', 'Admin', 'admin@test.com', '0901111111', null), -- ADMIN
    ('user-uuid-2', 'court_owner', 'pass123', 'John', 'Owner', 'owner@test.com', '0902222222', null), -- OWNER
    ('user-uuid-3', 'badminton_player', 'pass123', 'Alice', 'Player', 'alice@test.com', '0903333333', null); -- PLAYER

-- ==========================================
-- Insert Location
-- ==========================================
INSERT INTO location (id,name, address, rating, price_per_hour, onwer_id)
VALUES
    (1,'Victory Badminton Club', '123 Sport St, District 1', 4.5, 50000, 'user-uuid-2');

-- ==========================================
-- Insert Courts
-- ==========================================
INSERT INTO court (id,name, status, location_id)
VALUES
    (1,'Court A', 'Active', 1), -- Location ID 1
    (2,'Court B', 'Maintenance', 1);

-- ==========================================
-- Insert Booking
-- ==========================================
INSERT INTO booking (id,booking_date, start_time_slot, end_time_slot, total_price, status, player_id, court_id)
VALUES
    (1,'2023-12-01', 8, 10, 100000, 'Confirmed', 'user-uuid-3', 1);