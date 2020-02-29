/* Populate tables */
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Iván','Rojo', 'ivanrojo@mail.com','2020-02-19', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Karen','Lopéz', 'karenlopez@mail.com','2020-02-19', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Elizabeth','Rodriguez', 'elirod@mail.com','2020-02-20', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Javier','Marias', 'karenlopez@mail.com','2020-02-19', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Gabriel','García', 'karenlopez@mail.com','2020-02-19', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Mario','Vargallosas', 'karenlopez@mail.com','2020-02-19', '');



/* POPULATE TABLE PRODUCTOS */

INSERT INTO productos (nombre, precio, create_at) VALUES ('Panasonic Pantalla LCD',8599.99, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Sony Camara digital DSC-W320B',2050.50, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Apple iPod Shuffle',599.39, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Sony Notebook z110',15999.99, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('HP Multifuncional F2280',659.99, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Bianchi Bicicleta Rodada 26´',1500, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Mica Comoda 5 cajones',1100.50, NOW());

/* POPULATES FACTURAS */
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Factura equipos de oficina',null,1, NOW());
INSERT INTO facturas_items (cantidad,factura_id,producto_id) VALUES(1,1,1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(2,1,4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1,1,5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1,1,7);

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Factura bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (3,2,6);
