CREATE TABLE IF NOT EXISTS sales_points (
    id BINARY(16) NOT NULL,
    name VARCHAR(200) NOT NULL,
    street VARCHAR(255),
    city VARCHAR(100),
    postal_code VARCHAR(10),
    province VARCHAR(100),
    phone_number VARCHAR(20),
    opens_at TIME NOT NULL,
    closes_at TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
