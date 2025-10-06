-- 10 Admins
INSERT INTO users (id, email, enabled, name, password) VALUES
(2, 'admin1@silvermoon.com', 1, 'Admin One', '$2a$10$hashedpass'),
(3, 'admin2@silvermoon.com', 1, 'Admin Two', '$2a$10$hashedpass'),
(4, 'admin3@silvermoon.com', 1, 'Admin Three', '$2a$10$hashedpass'),
(5, 'admin4@silvermoon.com', 1, 'Admin Four', '$2a$10$hashedpass'),
(6, 'admin5@silvermoon.com', 1, 'Admin Five', '$2a$10$hashedpass'),
(7, 'admin6@silvermoon.com', 1, 'Admin Six', '$2a$10$hashedpass'),
(8, 'admin7@silvermoon.com', 1, 'Admin Seven', '$2a$10$hashedpass'),
(9, 'admin8@silvermoon.com', 1, 'Admin Eight', '$2a$10$hashedpass'),
(10, 'admin9@silvermoon.com', 1, 'Admin Nine', '$2a$10$hashedpass'),
(11, 'admin10@silvermoon.com', 1, 'Admin Ten', '$2a$10$hashedpass');

-- 30 Users
INSERT INTO users (id, email, enabled, name, password) VALUES
(12, 'user1@silvermoon.com', 1, 'User One', '$2a$10$hashedpass'),
(13, 'user2@silvermoon.com', 1, 'User Two', '$2a$10$hashedpass'),
(14, 'user3@silvermoon.com', 1, 'User Three', '$2a$10$hashedpass'),
(15, 'user4@silvermoon.com', 1, 'User Four', '$2a$10$hashedpass'),
(16, 'user5@silvermoon.com', 1, 'User Five', '$2a$10$hashedpass'),
(17, 'user6@silvermoon.com', 1, 'User Six', '$2a$10$hashedpass'),
(18, 'user7@silvermoon.com', 1, 'User Seven', '$2a$10$hashedpass'),
(19, 'user8@silvermoon.com', 1, 'User Eight', '$2a$10$hashedpass'),
(20, 'user9@silvermoon.com', 1, 'User Nine', '$2a$10$hashedpass'),
(21, 'user10@silvermoon.com', 1, 'User Ten', '$2a$10$hashedpass'),
(22, 'user11@silvermoon.com', 1, 'User Eleven', '$2a$10$hashedpass'),
(23, 'user12@silvermoon.com', 1, 'User Twelve', '$2a$10$hashedpass'),
(24, 'user13@silvermoon.com', 1, 'User Thirteen', '$2a$10$hashedpass'),
(25, 'user14@silvermoon.com', 1, 'User Fourteen', '$2a$10$hashedpass'),
(26, 'user15@silvermoon.com', 1, 'User Fifteen', '$2a$10$hashedpass'),
(27, 'user16@silvermoon.com', 1, 'User Sixteen', '$2a$10$hashedpass'),
(28, 'user17@silvermoon.com', 1, 'User Seventeen', '$2a$10$hashedpass'),
(29, 'user18@silvermoon.com', 1, 'User Eighteen', '$2a$10$hashedpass'),
(30, 'user19@silvermoon.com', 1, 'User Nineteen', '$2a$10$hashedpass'),
(31, 'user20@silvermoon.com', 1, 'User Twenty', '$2a$10$hashedpass'),
(32, 'user21@silvermoon.com', 1, 'User Twenty-One', '$2a$10$hashedpass'),
(33, 'user22@silvermoon.com', 1, 'User Twenty-Two', '$2a$10$hashedpass'),
(34, 'user23@silvermoon.com', 1, 'User Twenty-Three', '$2a$10$hashedpass'),
(35, 'user24@silvermoon.com', 1, 'User Twenty-Four', '$2a$10$hashedpass'),
(36, 'user25@silvermoon.com', 1, 'User Twenty-Five', '$2a$10$hashedpass'),
(37, 'user26@silvermoon.com', 1, 'User Twenty-Six', '$2a$10$hashedpass'),
(38, 'user27@silvermoon.com', 1, 'User Twenty-Seven', '$2a$10$hashedpass'),
(39, 'user28@silvermoon.com', 1, 'User Twenty-Eight', '$2a$10$hashedpass'),
(40, 'user29@silvermoon.com', 1, 'User Twenty-Nine', '$2a$10$hashedpass'),
(41, 'user30@silvermoon.com', 1, 'User Thirty', '$2a$10$hashedpass');

-- Admins
INSERT INTO user_roles (user_id, role_id) VALUES
(2, 1), (3, 1), (4, 1), (5, 1), (6, 1),
(7, 1), (8, 1), (9, 1), (10, 1), (11, 1);

-- Regular Users
INSERT INTO user_roles (user_id, role_id) VALUES
(12, 2), (13, 2), (14, 2), (15, 2), (16, 2),
(17, 2), (18, 2), (19, 2), (20, 2), (21, 2),
(22, 2), (23, 2), (24, 2), (25, 2), (26, 2),
(27, 2), (28, 2), (29, 2), (30, 2), (31, 2),
(32, 2), (33, 2), (34, 2), (35, 2), (36, 2),
(37, 2), (38, 2), (39, 2), (40, 2), (41, 2);
