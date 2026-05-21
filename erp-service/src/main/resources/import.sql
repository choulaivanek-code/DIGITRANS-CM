-- Populate Departments
INSERT INTO department (id, code, name, description) VALUES (1, 'DIR', 'Direction Générale', 'Administration générale d AGROCAM S.A.');
INSERT INTO department (id, code, name, description) VALUES (2, 'RH', 'Ressources Humaines', 'Gestion du personnel et des talents');
INSERT INTO department (id, code, name, description) VALUES (3, 'FIN', 'Finances et Comptabilité', 'Gestion financière, budget et comptabilité générale');
INSERT INTO department (id, code, name, description) VALUES (4, 'LOG', 'Logistique et Supply Chain', 'Gestion de la chaîne logistique et des entrepôts');
INSERT INTO department (id, code, name, description) VALUES (5, 'PROD', 'Production Agroalimentaire', 'Gestion de la production et de la transformation');

-- Populate Employees
INSERT INTO employee (id, department_id, email, first_name, hire_date, last_name, role, salary) VALUES (1, 1, 'samuel.eto@agrocam-cm.com', 'Samuel', '2020-01-15', 'Etoo', 'DIRECTOR', 2500000.0);
INSERT INTO employee (id, department_id, email, first_name, hire_date, last_name, role, salary) VALUES (2, 2, 'marie.kameni@agrocam-cm.com', 'Marie', '2021-03-10', 'Kameni', 'RH_MANAGER', 1200000.0);
INSERT INTO employee (id, department_id, email, first_name, hire_date, last_name, role, salary) VALUES (3, 3, 'jean.aboubakar@agrocam-cm.com', 'Jean', '2019-06-22', 'Aboubakar', 'FINANCE_OFFICER', 1400000.0);
INSERT INTO employee (id, department_id, email, first_name, hire_date, last_name, role, salary) VALUES (4, 4, 'paul.biya@agrocam-cm.com', 'Paul', '2022-09-01', 'Biya', 'LOGISTICS_HEAD', 1500000.0);
INSERT INTO employee (id, department_id, email, first_name, hire_date, last_name, role, salary) VALUES (5, 5, 'emilie.ngu@agrocam-cm.com', 'Emilie', '2023-02-18', 'Ngu', 'PRODUCTION_SUPERVISOR', 950000.0);

-- Populate Financial Records
INSERT INTO financial_record (id, amount, description, reference, transaction_date, type) VALUES (1, 15000000.0, 'Vente de produits agroalimentaires Douala Agency', 'REC-2026-001', '2026-05-10', 'REVENUE');
INSERT INTO financial_record (id, amount, description, reference, transaction_date, type) VALUES (2, 4500000.0, 'Achat de matières premières maïs et soja', 'EXP-2026-001', '2026-05-12', 'EXPENSE');
INSERT INTO financial_record (id, amount, description, reference, transaction_date, type) VALUES (3, 8200000.0, 'Paiement des salaires mensuels AGROCAM personnel', 'EXP-2026-002', '2026-05-15', 'EXPENSE');
INSERT INTO financial_record (id, amount, description, reference, transaction_date, type) VALUES (4, 25000000.0, 'Exportation de volailles vers la sous-région CEMAC', 'REC-2026-002', '2026-05-18', 'REVENUE');

-- Populate Suppliers
INSERT INTO supplier (id, address, company_name, contact_name, email, phone, tax_id) VALUES (1, 'Zone Industrielle de Bassa, Douala', 'AGRO-FEED Cameroon', 'Moussa Ousmanou', 'contact@agrofeed-cm.com', '+237 677 88 99 00', 'M09181234567A');
INSERT INTO supplier (id, address, company_name, contact_name, email, phone, tax_id) VALUES (2, 'Quartier Bastos, Yaoundé', 'Grain-Cam S.A.', 'Florence Eteki', 'info@graincam.cm', '+237 699 11 22 33', 'M03151244589X');
INSERT INTO supplier (id, address, company_name, contact_name, email, phone, tax_id) VALUES (3, 'Route de Bafoussam, Mbouda', 'Fermes de l Ouest Co.', 'Albert Fotso', 'afotso@fermesouest.com', '+237 233 44 55 66', 'M12201265893B');
