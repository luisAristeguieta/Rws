select * from producto;

-- 1) Agregar nuevo id serial
alter table cabecera_pedidos add column id_cabecera_pedidos_serial serial;

-- 2) Asegurar unicidad (para poder referenciarla)
alter table cabecera_pedidos add constraint uq_cabecera_pedidos_serial unique (id_cabecera_pedidos_serial);

-- 1) Agregar nuevo id serial para detalle
alter table detalle_pedidos add column id_pedidos_serial serial;

-- 2) Agregar nueva FK a cabecera serial
alter table detalle_pedidos add column id_cabecera_pedidos_serial int;

-- 3) Rellenar el nuevo campo FK usando la relación antigua
update detalle_pedidos dp
set id_cabecera_pedidos_serial = cp.id_cabecera_pedidos_serial
from cabecera_pedidos cp
where dp.id_cabecera_pedidos = cp.id_cabecera_pedidos;

-- 4) Crear FK nueva
alter table detalle_pedidos
add constraint fk_detalle_cabecera_serial
foreign key (id_cabecera_pedidos_serial)
references cabecera_pedidos(id_cabecera_pedidos_serial);

------------------------------------------------------------------

alter table cabecera_ventas add column codigo_ventas_serial serial;
alter table cabecera_ventas add constraint uq_cabecera_ventas_serial unique (codigo_ventas_serial);

------------------------------------------------------------------

-- 1) Nuevo id serial de detalle_venta
alter table detalle_venta add column codigo_vv_serial serial;

-- 2) Nueva FK a cabecera_ventas serial
alter table detalle_venta add column codigo_ventas_serial int;

-- 3) Rellenar FK nueva usando la relación antigua
update detalle_venta dv
set codigo_ventas_serial = cv.codigo_ventas_serial
from cabecera_ventas cv
where dv.codigo_ventas = cv.codigo_ventas;

-- 4) Crear FK nueva
alter table detalle_venta
add constraint fk_detalleventa_cabeceraventa_serial
foreign key (codigo_ventas_serial)
references cabecera_ventas(codigo_ventas_serial);

------------------------------------------------------------------

alter table historial_stock add column codigo_stock_serial serial;
alter table historial_stock add constraint uq_historial_stock_serial unique (codigo_stock_serial);

------------------------------------------------------------------

alter table cabecera_pedidos drop constraint cabecera_pedidos_pkey;
alter table cabecera_pedidos add constraint cabecera_pedidos_pkey primary key (id_cabecera_pedidos_serial);

------------------------------------------------------------------
alter table detalle_pedidos drop constraint detalle_pedidos_pkey;
alter table detalle_pedidos add constraint detalle_pedidos_pkey primary key (id_pedidos_serial);


------------------------------------------------------------------

select conname, conrelid::regclass as tabla_hija
from pg_constraint
where confrelid = 'cabecera_ventas'::regclass
  and contype = 'f';

alter table detalle_venta
drop constraint detalle_venta_codigo_ventas_fkey;

alter table detalle_venta
drop constraint fk_detalleventa_cabeceraventa_serial;

alter table cabecera_ventas
drop constraint cabecera_ventas_pkey;

alter table cabecera_ventas
add constraint cabecera_ventas_pkey
primary key (codigo_ventas_serial);

alter table detalle_venta
add constraint fk_detalleventa_cabeceraventa_serial
foreign key (codigo_ventas_serial)
references cabecera_ventas(codigo_ventas_serial);

select count(*)
from detalle_venta
where codigo_ventas_serial is null;



------------------------------------------------------------------
alter table cabecera_ventas drop constraint cabecera_ventas_pkey;
alter table cabecera_ventas add constraint cabecera_ventas_pkey primary key (codigo_ventas_serial);

------------------------------------------------------------------

alter table detalle_venta drop constraint detalle_venta_pkey;
alter table detalle_venta add constraint detalle_venta_pkey primary key (codigo_vv_serial);

------------------------------------------------------------------

alter table historial_stock drop constraint historial_stock_pkey;
alter table historial_stock add constraint historial_stock_pkey primary key (codigo_stock_serial);


------------------------------------------------------------------

-- detalle_pedidos: quitar FK vieja y columna vieja
alter table detalle_pedidos drop constraint detalle_pedidos_id_cabecera_pedidos_fkey;
alter table detalle_pedidos drop column id_cabecera_pedidos;

-- cabecera_pedidos: quitar id viejo
alter table cabecera_pedidos drop column id_cabecera_pedidos;

-- detalle_venta: quitar FK vieja y columna vieja
alter table detalle_venta drop constraint detalle_venta_codigo_ventas_fkey;
alter table detalle_venta drop column codigo_ventas;

-- cabecera_ventas: quitar id viejo
alter table cabecera_ventas drop column codigo_ventas;

-- historial_stock: quitar id viejo
alter table historial_stock drop column codigo_stock;

-- detalle_pedidos: quitar id viejo
alter table detalle_pedidos drop column id_pedidos;

-- detalle_venta: quitar id viejo
alter table detalle_venta drop column codigo_vv;

------------------------------------------------------------------

alter table detalle_pedidos
drop column if exists id_cabecera_pedidos;

------------------------------------------------------------------

alter table detalle_pedidos
drop column if exists id_pedidos;


alter table cabecera_pedidos
drop column if exists id_cabecera_pedidos;

alter table detalle_venta
drop column if exists codigo_ventas;

alter table detalle_venta
drop column if exists codigo_vv;

alter table cabecera_ventas
drop column if exists codigo_ventas;


alter table historial_stock
drop column if exists codigo_stock;


select table_name, column_name
from information_schema.columns
where table_name in ('cabecera_pedidos','detalle_pedidos','cabecera_ventas','detalle_venta','historial_stock')
order by table_name, column_name;


ALTER TABLE proveedores ALTER COLUMN id_proveedor TYPE varchar(13);