DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_prefix VARCHAR(10) NULL,
    phone_number VARCHAR(20) NULL,
    gender VARCHAR(20) NULL,
    address VARCHAR(255) NULL,
    city VARCHAR(100) NULL,
    state VARCHAR(100) NULL,
    zip_code VARCHAR(20) NULL,
    country VARCHAR(100) NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
