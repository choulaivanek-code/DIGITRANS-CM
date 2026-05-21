-- Populate Restaurants "SavoirManger"
INSERT INTO restaurant (id, address, city, name, phone) VALUES (1, 'Rue Akwa, en face de l Douala Bar', 'Douala', 'SavoirManger Douala', '+237 671 22 33 44');
INSERT INTO restaurant (id, address, city, name, phone) VALUES (2, 'Avenue Bastos, près de l Ambassade', 'Yaoundé', 'SavoirManger Yaoundé', '+237 691 55 66 77');
INSERT INTO restaurant (id, address, city, name, phone) VALUES (3, 'Quartier Tamdja, Route Principale', 'Bafoussam', 'SavoirManger Bafoussam', '+237 672 88 99 00');
INSERT INTO restaurant (id, address, city, name, phone) VALUES (4, 'Secteur Commercial, face à la Grande Mosquée', 'Garoua', 'SavoirManger Garoua', '+237 693 11 22 33');
INSERT INTO restaurant (id, address, city, name, phone) VALUES (5, 'Quartier Baladji II, Route de l Aéroport', 'Ngaoundéré', 'SavoirManger Ngaoundéré', '+237 673 44 55 66');

-- Populate Customers
INSERT INTO customer (id, email, full_name, loyalty_points, phone, registered_date) VALUES (1, 'marc.ngu@gmail.com', 'Marc Ngu', 150, '+237 675 44 88 12', '2025-01-10');
INSERT INTO customer (id, email, full_name, loyalty_points, phone, registered_date) VALUES (2, 'sylvie.bella@yahoo.fr', 'Sylvie Bella', 320, '+237 695 11 22 33', '2025-02-15');
INSERT INTO customer (id, email, full_name, loyalty_points, phone, registered_date) VALUES (3, 'ahmadou.taman@outlook.com', 'Ahmadou Taman', 85, '+237 677 33 55 77', '2025-03-20');
INSERT INTO customer (id, email, full_name, loyalty_points, phone, registered_date) VALUES (4, 'patricia.fotso@gmail.com', 'Patricia Fotso', 540, '+237 699 88 77 66', '2025-04-05');

-- Populate Orders
INSERT INTO customer_order (id, customer_id, restaurant_id, total_amount, status, order_date) VALUES (1, 1, 1, 15000.0, 'COMPLETED', '2026-05-18');
INSERT INTO customer_order (id, customer_id, restaurant_id, total_amount, status, order_date) VALUES (2, 2, 2, 28500.0, 'COMPLETED', '2026-05-19');
INSERT INTO customer_order (id, customer_id, restaurant_id, total_amount, status, order_date) VALUES (3, 3, 3, 9000.0, 'PENDING', '2026-05-20');
INSERT INTO customer_order (id, customer_id, restaurant_id, total_amount, status, order_date) VALUES (4, 4, 4, 35000.0, 'COMPLETED', '2026-05-20');

-- Populate Feedbacks
INSERT INTO feedback (id, customer_id, order_id, rating, comment, submitted_date) VALUES (1, 1, 1, 5, 'Le Ndolé et le poulet DG étaient fantastiques, service impeccable !', '2026-05-18');
INSERT INTO feedback (id, customer_id, order_id, rating, comment, submitted_date) VALUES (2, 2, 2, 4, 'Très bon repas à Bastos, cadre agréable et propre.', '2026-05-19');
INSERT INTO feedback (id, customer_id, order_id, rating, comment, submitted_date) VALUES (3, 4, 4, 5, 'Excellent service de livraison sur Garoua. Je recommande !', '2026-05-20');
