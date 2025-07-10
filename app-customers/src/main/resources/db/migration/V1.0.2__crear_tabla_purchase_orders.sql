CREATE TABLE purchase_orders
(
    id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL,
    total INTEGER,
    status INTEGER DEFAULT 0,
    placed_on TIMESTAMP NOT NULL,
    delivered_on TIMESTAMP,
    CONSTRAINT fk_purchase_orders_customers FOREIGN KEY (customer_id) REFERENCES customers (id)
);

INSERT INTO purchase_orders (customer_id, total, status, placed_on, delivered_on)
VALUES
    (1, 100, 0, '2025-07-10 10:00:00', NULL),
    (2, 150, 1, '2025-07-09 15:00:00', '2025-07-10 10:30:00'),
    (3, 200, 0, '2025-07-11 11:00:00', NULL),
    (4, 250, 1, '2025-07-12 12:00:00', '2025-07-13 09:00:00'),
    (5, 300, 0, '2025-07-14 14:00:00', NULL);
