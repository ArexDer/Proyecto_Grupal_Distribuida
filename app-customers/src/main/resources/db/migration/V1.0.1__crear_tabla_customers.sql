CREATE TABLE customers
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    email VARCHAR(64) UNIQUE NOT NULL,
    version INTEGER
);

INSERT INTO customers (name, email, version)
VALUES
    ('Juan Pérez', 'juan.perez@example.com', 1),
    ('María López', 'maria.lopez@example.com', 1),
    ('Carlos García', 'carlos.garcia@example.com', 1),
    ('Ana Martínez', 'ana.martinez@example.com', 1),
    ('Luis Rodríguez', 'luis.rodriguez@example.com', 1);
