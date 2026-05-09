-- =============================================================
-- Seed: 5 Sales Points
-- UUIDs match those used in seed_customers.sql
-- Compatible with MySQL · utf8mb4_unicode_ci
-- =============================================================

SET NAMES utf8mb4;

INSERT INTO sales_points
    (id, name, street, city, postal_code, province, phone_number, opens_at, closes_at, status)
VALUES
(
    UUID_TO_BIN('a1000000-0000-0000-0000-000000000001'),
    'Sucursal Buenos Aires Centro',
    'Av. Corrientes 1234',
    'Buenos Aires',
    'C1043',
    'Buenos Aires',
    '+54-11-4000-0001',
    '08:00:00',
    '20:00:00',
    'active'
),
(
    UUID_TO_BIN('a1000000-0000-0000-0000-000000000002'),
    'Sucursal Córdoba Nueva Córdoba',
    'Av. Hipólito Yrigoyen 567',
    'Córdoba',
    'X5000',
    'Córdoba',
    '+54-351-400-0002',
    '09:00:00',
    '21:00:00',
    'active'
),
(
    UUID_TO_BIN('a1000000-0000-0000-0000-000000000003'),
    'Sucursal Rosario Centro',
    'Bv. Oroño 890',
    'Rosario',
    'S2000',
    'Santa Fe',
    '+54-341-400-0003',
    '08:30:00',
    '20:30:00',
    'active'
),
(
    UUID_TO_BIN('a1000000-0000-0000-0000-000000000004'),
    'Sucursal Mendoza Godoy Cruz',
    'Av. San Martín 321',
    'Godoy Cruz',
    'M5501',
    'Mendoza',
    '+54-261-400-0004',
    '09:00:00',
    '19:00:00',
    'active'
),
(
    UUID_TO_BIN('a1000000-0000-0000-0000-000000000005'),
    'Sucursal Bariloche Centro',
    'Av. 12 de Octubre 654',
    'Bariloche',
    'R8400',
    'Río Negro',
    '+54-294-400-0005',
    '10:00:00',
    '19:00:00',
    'active'
);

-- End of seed
