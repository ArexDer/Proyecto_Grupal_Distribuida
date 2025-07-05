-- ***********************************************************************
-- Script: V2__ingreso_datos.sql
-- Descripción: Inserta registros de ejemplo en las tablas authors, books,
-- inventory y books_authors.
-- Autor: TuNombre
-- Fecha: 2025-05-19
-- ***********************************************************************

-- =========================
-- Insertar más autores
-- =========================
INSERT INTO authors (name, version) VALUES ('Gabriel García Márquez', 1);
INSERT INTO authors (name, version) VALUES ('Isabel Allende', 1);

-- =========================
-- Insertar libros
-- =========================
INSERT INTO books (isbn, title, price, version) VALUES ('ISBN-001', 'Cien Años de Soledad', 25.50, 1);
INSERT INTO books (isbn, title, price, version) VALUES ('ISBN-002', 'La Casa de los Espíritus', 20.00, 1);
INSERT INTO books (isbn, title, price, version) VALUES ('ISBN-003', 'Rayuela', 22.90, 1);
INSERT INTO books (isbn, title, price, version) VALUES ('ISBN-004', 'Pedro Páramo', 18.75, 1);
INSERT INTO books (isbn, title, price, version) VALUES ('ISBN-005', 'El Aleph', 19.95, 1);

-- =========================
-- Insertar inventario
-- =========================
INSERT INTO inventory (isbn, sold, supplied) VALUES ('ISBN-001', 50, 100);
INSERT INTO inventory (isbn, sold, supplied) VALUES ('ISBN-002', 30, 80);
INSERT INTO inventory (isbn, sold, supplied) VALUES ('ISBN-003', 20, 50);
INSERT INTO inventory (isbn, sold, supplied) VALUES ('ISBN-004', 40, 90);
INSERT INTO inventory (isbn, sold, supplied) VALUES ('ISBN-005', 25, 60);

-- =========================
-- Relacionar libros con autores
-- =========================
-- ISBN-001: Gabriel García Márquez (id=4)
INSERT INTO books_authors (books_isbn, authors_id) VALUES ('ISBN-001', 4);

-- ISBN-002: Isabel Allende (id=5)
INSERT INTO books_authors (books_isbn, authors_id) VALUES ('ISBN-002', 5);

-- ISBN-003: Autor FFF (id=1)
INSERT INTO books_authors (books_isbn, authors_id) VALUES ('ISBN-003', 1);

-- ISBN-004: AUTOR 222 (id=2)
INSERT INTO books_authors (books_isbn, authors_id) VALUES ('ISBN-004', 2);

-- ISBN-005: Autor Ni idea (id=3)
INSERT INTO books_authors (books_isbn, authors_id) VALUES ('ISBN-005', 3);
