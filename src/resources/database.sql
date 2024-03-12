-- Create inventory_db database (MySQL)
CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

-- Create warehouses table
CREATE TABLE IF NOT EXISTS warehouses (
    name VARCHAR(255) PRIMARY KEY,
    min_product INT,
    max_product INT
);

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    name VARCHAR(255),
    warehouse VARCHAR(255),
    total_qty INT,
    remaining_qty INT,
    PRIMARY KEY (name, warehouse),
    FOREIGN KEY (warehouse) REFERENCES warehouses(name)
);

-- Insert sample data into warehouses table
INSERT INTO warehouses (name, min_product, max_product) VALUES
('Plant 1', 10, 100),
('Plant 2', 5, 50),
('Plant 3', 7, 75);

-- Insert sample data into products table
INSERT INTO products (name, total_qty, remaining_qty, warehouse) VALUES
('Coke', 100, 50, 'Plant 1'),
('Sprite', 45, 3, 'Plant 2'),
('Dr. Pepper', 90, 85, 'Plant 3'),
('Canada Dry', 95, 50, 'Plant 2');
