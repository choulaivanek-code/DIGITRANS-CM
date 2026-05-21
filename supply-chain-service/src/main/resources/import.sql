-- Populate Warehouses
INSERT INTO warehouse (id, capacity, location, name) VALUES (1, 100000, 'Zone Portuaire, Douala', 'Entrepôt Portuaire Douala');
INSERT INTO warehouse (id, capacity, location, name) VALUES (2, 50000, 'Quartier Industriel Mvan, Yaoundé', 'Entrepôt Central Yaoundé');
INSERT INTO warehouse (id, capacity, location, name) VALUES (3, 30000, 'Sortie Ouest, Bafoussam', 'Entrepôt Régional Bafoussam');

-- Populate Products
INSERT INTO product (id, category, name, sku, stock_quantity, unit_price) VALUES (1, 'Cacao', 'Fèves de Cacao Brut (Grade A)', 'SKU-COCOA-001', 15000, 2200.0);
INSERT INTO product (id, category, name, sku, stock_quantity, unit_price) VALUES (2, 'Café', 'Café Arabica de Foumban', 'SKU-COFFEE-001', 8000, 3500.0);
INSERT INTO product (id, category, name, sku, stock_quantity, unit_price) VALUES (3, 'Alimentaire', 'Poulet Entier AGROCAM Surgelé', 'SKU-CHICK-001', 25000, 1800.0);
INSERT INTO product (id, category, name, sku, stock_quantity, unit_price) VALUES (4, 'Alimentaire', 'Aliment Volailles Démarrage', 'SKU-FEED-001', 50000, 450.0);

-- Populate Shipments
INSERT INTO shipment (id, arrival_date, departure_date, destination_address, origin_warehouse_id, status, tracking_number) VALUES (1, '2026-05-15', '2026-05-12', 'SavoirManger Bastos, Yaoundé', 1, 'DELIVERED', 'TRK-2026-000001');
INSERT INTO shipment (id, arrival_date, departure_date, destination_address, origin_warehouse_id, status, tracking_number) VALUES (2, NULL, '2026-05-18', 'SavoirManger Garoua, Commercial', 1, 'IN_TRANSIT', 'TRK-2026-000002');
INSERT INTO shipment (id, arrival_date, departure_date, destination_address, origin_warehouse_id, status, tracking_number) VALUES (3, NULL, '2026-05-20', 'SavoirManger Bafoussam, Tamdja', 2, 'SHIPPED', 'TRK-2026-000003');

-- Populate Tracking Events
INSERT INTO tracking_event (id, event_location, event_time, status_description, shipment_id) VALUES (1, 'Douala Port Terminal', '2026-05-12 08:30:00', 'Expédition chargée et en route', 1);
INSERT INTO tracking_event (id, event_location, event_time, status_description, shipment_id) VALUES (2, 'Yaoundé Mvan Entrepôt', '2026-05-14 14:20:00', 'Arrivée à l entrepôt de transit', 1);
INSERT INTO tracking_event (id, event_location, event_time, status_description, shipment_id) VALUES (3, 'SavoirManger Bastos, Yaoundé', '2026-05-15 11:00:00', 'Colis livré avec succès', 1);
INSERT INTO tracking_event (id, event_location, event_time, status_description, shipment_id) VALUES (4, 'Douala Port Terminal', '2026-05-18 09:00:00', 'Départ du camion vers Garoua', 2);
INSERT INTO tracking_event (id, event_location, event_time, status_description, shipment_id) VALUES (5, 'Bertoua Point de Passage', '2026-05-20 16:45:00', 'Transit de sécurité Douane', 2);
