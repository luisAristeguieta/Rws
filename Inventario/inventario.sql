-- Limpio tabla existente de acuedo a dependencias y creaciones:

drop table if exists detalle_venta;
drop table if exists cabecera_ventas;
drop table if exists historial_stock;
drop table if exists detalle_pedidos;
drop table if exists cabecera_pedidos;
drop table if exists estado_pedido;
drop table if exists proveedores;
drop table if exists tipo_documento;
drop table if exists producto;
drop table if exists categoria;
drop table if exists unidades_de_medidas;
drop table if exists categoria_unidad;

-- Crea tablas de acuerdo a dependecias con referencias en fk:

create table categoria(
	codigo_categoria serial primary key,
	nombre varchar(100) not null,
	categoria_padre int references categoria(codigo_categoria)
	);

create table categoria_unidad(
	codigo_unidad char(1) primary key,
	nombre varchar(100) not null
	);

create table unidades_de_medidas(
	codigo_udm char(2) primary key,
	codigo_unidad char(1) references categoria_unidad(codigo_unidad),
	descripcion varchar(100) not null
	);

create table producto(
	codigo_prod serial primary key,
	nombre varchar(100) not null,
	codigo_udm char(2) references unidades_de_medidas(codigo_udm),
	precio_venta money not null,
	iva boolean not null,
	costo money not null,
	codigo_categoria int references categoria(codigo_categoria)
	);

create table tipo_documento(
	codigo_documento char(1) primary key,
	descripcion varchar(100) not null
	);

create table proveedores(
	id_proveedor char(3) primary key,
	codigo_documento char(1) references tipo_documento(codigo_documento),
	nombre varchar(100) not null,
	telefono char(10) not null,
	correo varchar(100) not null,
	direccion varchar(100) not null	
	);

create table estado_pedido(
	codigo_estado char(1) primary key,
	descripcion varchar(100) not null
	);
	
create table cabecera_pedidos(
	id_cabecera_pedidos char(3) primary key,
	id_proveedor char(3) references proveedores(id_proveedor),
	fecha date not null,
	codigo_estado char(1) references estado_pedido(codigo_estado)
	);

create table detalle_pedidos(
	id_pedidos char(3) primary key,
	id_cabecera_pedidos char(3) references cabecera_pedidos(id_cabecera_pedidos),
	codigo_prod int references producto(codigo_prod),
	cantidad int,
	subtotal money
	);

create table historial_stock(
	codigo_stock char(3) primary key,
	fecha timestamp,
	codigo_prod int references producto(codigo_prod),
	cantidad_stock int
	);

create table cabecera_ventas(
	codigo_ventas char(4) primary key,
	fecha timestamp,
	subtotal money,
	iva money,
	total money
	);

create table detalle_venta(
	codigo_vv char(4) primary key,
	codigo_ventas char (4) references cabecera_ventas(codigo_ventas),
	codigo_prod int references producto(codigo_prod),
	cantidad int,
	precio_unitario money,
	subtotal money,
	total money
	);

-- Realiza inserts de acuerdo con dependencias: 

insert into categoria_unidad values
('u','Unidad(es)'),
('v','Volumen'),
('p','Masa');

insert into unidades_de_medidas values
('ml','v','mililitro'),
('l','v','litro'),
('g','p','gramos'),
('kg','p','kilogramos'),
('u','u','unidad'),
('lb','p','libras');

insert into categoria (codigo_categoria,nombre,categoria_padre) values
(1,'Materia Prima',null),
(2,'Proteina',1),
(3,'Salsas',1),
(4,'Punto De Venta',null),
(5,'Bebidas',4),
(6,'Con Alcohol',5),
(7,'Sin Alcohol',5);

insert into tipo_documento values
('c','Cedula'),
('r','Ruc');

insert into proveedores values
('001','c','Santiago','998761273','test1@gmail.com','Tumbaco'),
('002','r','Santiago 2','998761273','test2@gmail.com','Cumbaya');

insert into producto
(codigo_prod,nombre,codigo_udm,precio_venta,iva,costo,codigo_categoria) values
(1,'Coca Pequeña','u',0.5804,true,0.379,7),
(2,'Salsa de Tomate','kg',0.95,true,0.8736,3),
(3,'Mostaza','kg',0.95,true,0.89,3),
(4,'Fuze Tea','u',0.8,true,0.7,7);


insert into estado_pedido values
('s','Solicitado'),
('r','Recibido');

insert into cabecera_pedidos values
('c01','001','2026-01-18','r'),
('c02','002','2026-01-16','r');

insert into detalle_pedidos values
('p01','c01',1,100,37.29),
('p02','c01',4,50,11.80),
('p03','c02',1,10,3.73);

insert into historial_stock values
('s01','2026-01-18 10:00:00',1,100),
('s02','2026-01-18 10:00:00',4,50);

insert into cabecera_ventas values
('v01','2026-01-18 17:40:00',3.26,0.39,3.65);

insert into detalle_venta values
('vv01','v01',1,5,0.58,2.90,3.25),
('vv02','v01',4,1,0.38,0.36,0.40);



select * from producto;

select * from proveedores where upper(nombre) like '%S%';

select *
from proveedores pv
inner join tipo_documento td
    on pv.codigo_documento = td.codigo_documento      -- condición de unión
where upper(nombre) like '%S%';          -- filtros adicionales

select id_proveedor from proveedores where id_proveedor like '%0%';

select * from proveedores where id_proveedor = '003';

alter table Producto add column stock_producto int;
update Producto set stock_producto = 100 where codigo_prod =1;
update Producto set stock_producto = 0 where codigo_prod =2;
update Producto set stock_producto = 0 where codigo_prod =3;
update Producto set stock_producto = 50 where codigo_prod =4;

select pr.codigo_prod, pr.nombre, pr.codigo_udm, udm.descripcion as descripcion_udm, 
	cast(pr.precio_venta as decimal(6,3)) as precio_venta, pr.iva, cast(pr.costo as decimal(6,3)) as costo, 
	pr.codigo_categoria, ca.nombre as nombre_categoria, pr.stock_producto 
from producto pr 
inner join unidades_de_medidas udm on pr.codigo_udm = udm.codigo_udm 
inner join categoria ca on pr.codigo_categoria = ca.codigo_categoria -- condición de unión
where upper(pr.nombre) like '%S%';
