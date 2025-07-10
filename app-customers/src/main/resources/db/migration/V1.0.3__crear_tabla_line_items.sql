CREATE TABLE line_items
(
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    quantity INTEGER,
    isbn VARCHAR(64) NOT NULL,
    CONSTRAINT fk_line_items_purchase_orders FOREIGN KEY (order_id) REFERENCES purchase_orders (id)
);

INSERT INTO line_items (order_id, quantity, isbn)
VALUES
    (1, 2, '978-3-16-148410-0'),
    (1, 1, '978-1-4028-9462-6'),
    (2, 3, '978-0-14-044913-6'),
    (3, 1, '978-1-56619-909-4'),
    (4, 2, '978-0-307-26817-9');
