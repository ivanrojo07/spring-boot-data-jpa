/* Populate tables */
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Iván','Rojo', 'ivanrojo@mail.com','2020-02-19', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Karen','Lopéz', 'karenlopez@mail.com','2020-02-19', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Elizabeth','Rodriguez', 'elirod@mail.com','2020-02-20', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Javier','Marias', 'karenlopez@mail.com','2020-02-19', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Gabriel','García', 'karenlopez@mail.com','2020-02-19', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Mario','Vargallosas', 'karenlopez@mail.com','2020-02-19', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Andres', 'Guzman', 'profesor@bolsadeideas.com', '2017-08-01', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('John', 'Doe', 'john.doe@gmail.com', '2017-08-02', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Linus', 'Torvalds', 'linus.torvalds@gmail.com', '2017-08-03', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Jane', 'Doe', 'jane.doe@gmail.com', '2017-08-04', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '2017-08-05', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Erich', 'Gamma', 'erich.gamma@gmail.com', '2017-08-06', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Richard', 'Helm', 'richard.helm@gmail.com', '2017-08-07', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Ralph', 'Johnson', 'ralph.johnson@gmail.com', '2017-08-08', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('John', 'Vlissides', 'john.vlissides@gmail.com', '2017-08-09', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('James', 'Gosling', 'james.gosling@gmail.com', '2017-08-010', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Bruce', 'Lee', 'bruce.lee@gmail.com', '2017-08-11', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Johnny', 'Doe', 'johnny.doe@gmail.com', '2017-08-12', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('John', 'Roe', 'john.roe@gmail.com', '2017-08-13', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Jane', 'Roe', 'jane.roe@gmail.com', '2017-08-14', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Richard', 'Doe', 'richard.doe@gmail.com', '2017-08-15', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Janie', 'Doe', 'janie.doe@gmail.com', '2017-08-16', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Phillip', 'Webb', 'phillip.webb@gmail.com', '2017-08-17', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Stephane', 'Nicoll', 'stephane.nicoll@gmail.com', '2017-08-18', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Sam', 'Brannen', 'sam.brannen@gmail.com', '2017-08-19', '');  
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Juergen', 'Hoeller', 'juergen.Hoeller@gmail.com', '2017-08-20', ''); 
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Janie', 'Roe', 'janie.roe@gmail.com', '2017-08-21', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('John', 'Smith', 'john.smith@gmail.com', '2017-08-22', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Joe', 'Bloggs', 'joe.bloggs@gmail.com', '2017-08-23', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('John', 'Stiles', 'john.stiles@gmail.com', '2017-08-24', '');
INSERT INTO clientes (nombre, apellido, email, created_at, foto) VALUES('Richard', 'Roe', 'stiles.roe@gmail.com', '2017-08-25', '');



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


/* CREAMOS USUARIOS CON ROLES */
INSERT INTO public.users (id, username, password, enabled) VALUES (1, 'ivan', '$2a$10$c3u/VGzedwuLUUMmDWCr9u2rvyRLExtOO.w2yavH1hEzDM/eEMlcS', true);
INSERT INTO public.users (id, username, password, enabled) VALUES (2, 'admin', '$2a$10$tKUVuDgwpzafZyFnkfDC3O7cr1FV0ihQtg5LwgGB6qpUyo4t7zU1y', true);

INSERT INTO authorities (user_id, authority) VALUES (1,'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (2,'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (2,'ROLE_ADMIN');
