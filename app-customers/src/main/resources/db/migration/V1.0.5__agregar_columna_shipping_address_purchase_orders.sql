ALTER TABLE purchase_orders ADD COLUMN shipping_address VARCHAR(255);

UPDATE purchase_orders
SET shipping_address = 'Calle Ficticia 123, Ciudad Ejemplo'
WHERE id = 1;

UPDATE purchase_orders
SET shipping_address = 'Avenida Siempre Viva 456, Springfield'
WHERE id = 2;

UPDATE purchase_orders
SET shipping_address = 'Calle Gran Vía 789, Madrid'
WHERE id = 3;

UPDATE purchase_orders
SET shipping_address = 'Boulevard de la 5ta Avenida 1000, Nueva York'
WHERE id = 4;

UPDATE purchase_orders
SET shipping_address = 'Calle de la Paz 101, Bogotá'
WHERE id = 5;
