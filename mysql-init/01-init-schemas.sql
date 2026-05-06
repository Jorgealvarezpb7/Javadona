CREATE DATABASE IF NOT EXISTS db_customers    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS db_sales_points CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS db_sales        CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS db_inventory    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'mkt'@'%' IDENTIFIED BY 'mkt_secret';

GRANT ALL PRIVILEGES ON db_customers.*    TO 'mkt'@'%';
GRANT ALL PRIVILEGES ON db_sales_points.* TO 'mkt'@'%';
GRANT ALL PRIVILEGES ON db_sales.*        TO 'mkt'@'%';
GRANT ALL PRIVILEGES ON db_inventory.*    TO 'mkt'@'%';

FLUSH PRIVILEGES;
