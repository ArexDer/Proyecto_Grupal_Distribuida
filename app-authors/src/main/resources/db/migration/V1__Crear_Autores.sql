-- ***********************************************************************
-- Script: V1__crear_tabla_authorss.sql
-- Descripción: Crea las tablas iniciales para el sistema de libros y autores,
-- incluyendo restricciones de claves primarias y foráneas.
-- Autor: TuNombre
-- Fecha: 2025-05-19
-- ***********************************************************************
-- DROP DATABASE IF EXISTS distribuidaG;
-- CREATE DATABASE distribuidaG;
-- =========================
-- LIMPIEZA DE TABLAS EXISTENTES (solo para desarrollo)
-- =========================
DROP TABLE IF EXISTS books_authors;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

-- =========================
-- 1. Tabla de autores
-- =========================
CREATE TABLE authors (
                         id INTEGER GENERATED ALWAYS AS IDENTITY,
                         name VARCHAR(255) NOT NULL,
                         version INTEGER NOT NULL,
                         CONSTRAINT author_pkey PRIMARY KEY (id)
);

-- =========================
-- Datos iniciales de autores
-- =========================
INSERT INTO authors(name, version) VALUES ('Autor FFF', 1);
INSERT INTO authors(name, version) VALUES ('AUTOR 222', 11);
INSERT INTO authors(name, version) VALUES ('Autor Ni idea', 456);

-- =========================
-- 2. Tabla de libros
-- =========================
CREATE TABLE books (
                       isbn VARCHAR(255) NOT NULL,
                       title VARCHAR(128),
                       price DECIMAL(12, 2),
                       version INTEGER,
                       CONSTRAINT pk_books PRIMARY KEY (isbn)
);

-- =========================
-- 3. Tabla de inventario de libros
-- =========================
CREATE TABLE inventory (
                           isbn VARCHAR(255) NOT NULL,
                           sold INTEGER,
                           supplied INTEGER,
                           CONSTRAINT pk_inventory PRIMARY KEY (isbn)
);

-- =========================
-- 4. Tabla de relación muchos-a-muchos entre libros y autores
-- =========================
CREATE TABLE books_authors (
                               books_isbn VARCHAR(255) NOT NULL,
                               authors_id INTEGER NOT NULL,
                               CONSTRAINT pk_books_authors PRIMARY KEY (books_isbn, authors_id)
);

-- =========================
-- 5. Restricción de clave foránea: inventory.isbn → books.isbn
-- =========================
ALTER TABLE inventory
    ADD CONSTRAINT fk_inventory_books
        FOREIGN KEY (isbn)
            REFERENCES books (isbn);

-- =========================
-- 6. Restricción de clave foránea: books_authors.authors_id → authors.id
-- =========================
ALTER TABLE books_authors
    ADD CONSTRAINT fk_books_authors_authors
        FOREIGN KEY (authors_id)
            REFERENCES authors (id);

-- =========================
-- 7. Restricción de clave foránea: books_authors.books_isbn → books.isbn
-- =========================
ALTER TABLE books_authors
    ADD CONSTRAINT fk_books_authors_books
        FOREIGN KEY (books_isbn)
            REFERENCES books (isbn);
