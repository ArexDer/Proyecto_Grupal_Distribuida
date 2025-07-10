CREATE INDEX idx_email ON customers (email);
CREATE INDEX idx_customer_id ON purchase_orders (customer_id);
CREATE INDEX idx_isbn ON line_items (isbn);
CREATE INDEX idx_status ON purchase_orders (status);
CREATE INDEX idx_placed_on ON purchase_orders (placed_on);