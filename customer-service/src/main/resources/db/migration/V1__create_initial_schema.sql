CREATE TABLE IF NOT EXISTS customers (
    id BINARY(16) NOT NULL,
    document_type VARCHAR(3) NOT NULL,
    document_value VARCHAR(20) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    phone_number VARCHAR(20),
    email VARCHAR(255) NOT NULL,
    street VARCHAR(255),
    city VARCHAR(100),
    postal_code VARCHAR(10),
    province VARCHAR(100),
    reward_points INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_customers_email (email),
    UNIQUE KEY uk_customers_document (document_type, document_value)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS customer_frequent_sales_points (
    customer_id BINARY(16) NOT NULL,
    sales_point_id BINARY(16) NOT NULL,
    PRIMARY KEY (customer_id, sales_point_id),
    CONSTRAINT fk_cfsp_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
