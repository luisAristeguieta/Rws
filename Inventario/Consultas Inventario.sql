

select * from detalle_venta; -- ok 
select * from cabecera_ventas; -- tiene unique
select * from historial_stock; -- tiene unique
select * from detalle_pedidos; -- ok
select * from cabecera_pedidos; -- ok 
select * from estado_pedido; -- ok 
select * from proveedores; -- ok 
select * from tipo_documento; -- ok 
select * from producto; -- ok 
select * from categoria; -- ok
select * from unidades_de_medidas; -- ok 
select * from categoria_unidad;
-- Hay 12 tablas 






-- Busquedad de pedidos por id del proveedor:
select pr.id_proveedor, pr.codigo_documento, pr.nombre, cp.id_cabecera_pedidos, cp.fecha, cp.codigo_estado, dp.codigo_prod, pro.nombre as nombre_producto, cat.nombre as nombre_categoria,dp.cantidad_recibida, dp.subtotal 
from cabecera_pedidos cp
inner join proveedores pr on cp.id_proveedor = pr.id_proveedor
inner join detalle_pedidos dp on cp.id_cabecera_pedidos = dp.id_cabecera_pedidos
inner join producto pro on dp.codigo_prod = pro.codigo_prod
inner join categoria cat on pro.codigo_categoria = cat.codigo_categoria
where  cp.id_proveedor = '1792285747'
order by cp.id_proveedor;

-- Busquedad del proveedor con id: 
select * from proveedores where id_proveedor = '1792285747'


-- Busquedad del product con id: 
select * from producto where codigo_prod = 4








select cp.id_cabecera_pedidos, cp.fecha, cp.codigo_estado,
       pr.id_proveedor, pr.codigo_documento, pr.nombre
from cabecera_pedidos cp
inner join proveedores pr on cp.id_proveedor = pr.id_proveedor
where cp.id_proveedor = '1792285747001'
order by cp.id_cabecera_pedidos;

select dp.id_cabecera_pedidos, dp.codigo_prod,
       pro.nombre as nombre_producto,
       cat.nombre as nombre_categoria,
       dp.cantidad_recibida, dp.subtotal
from detalle_pedidos dp
inner join producto pro on dp.codigo_prod = pro.codigo_prod
left join categoria cat on pro.codigo_categoria = cat.codigo_categoria
where dp.id_cabecera_pedidos = '1'
order by dp.codigo_prod;






select  
    pr.id_proveedor,  
    pr.codigo_documento,  
    pr.nombre,  
    cp.id_cabecera_pedidos,  
    cp.fecha,  
    cp.codigo_estado,  
    dp.codigo_prod,  
    pro.nombre as nombre_producto,  
    cat.nombre as nombre_categoria,  
    dp.cantidad_recibida,  
    dp.subtotal  
from cabecera_pedidos cp  
inner join proveedores pr  
    on cp.id_proveedor = pr.id_proveedor  
inner join detalle_pedidos dp  
    on cp.id_cabecera_pedidos = dp.id_cabecera_pedidos  
inner join producto pro  
    on dp.codigo_prod = pro.codigo_prod  
left join categoria cat  
    on pro.codigo_categoria = cat.codigo_categoria  
where cp.id_proveedor = '1792285747'
order by cp.id_cabecera_pedidos, dp.codigo_prod;


		
select pr.id_proveedor, 
       pr.codigo_documento, 
       pr.nombre, 
       cp.id_cabecera_pedidos, 
       cp.codigo_estado, 
       dp.codigo_prod, 
       pro.nombre as nombre_producto, 
       cat.nombre as nombre_categoria,
       dp.cantidad_recibida, 
       dp.subtotal 
from cabecera_pedidos cp
left join proveedores pr on cp.id_proveedor = pr.id_proveedor
inner join detalle_pedidos dp on cp.id_cabecera_pedidos = dp.id_cabecera_pedidos
inner join producto pro on dp.codigo_prod = pro.codigo_prod
inner join categoria cat on pro.codigo_categoria = cat.codigo_categoria
order by cp.id_proveedor;


select 
    pr.id_proveedor,
    pr.codigo_documento,
    pr.nombre as nombre_proveedor,
    cp.id_cabecera_pedidos,
    cp.codigo_estado,
    sum(dp.cantidad_recibida) as total_cantidad,
    sum(dp.subtotal) as total_pedido
from cabecera_pedidos cp
inner join proveedores pr 
    on cp.id_proveedor = pr.id_proveedor
inner join detalle_pedidos dp 
    on cp.id_cabecera_pedidos = dp.id_cabecera_pedidos
group by 
    pr.id_proveedor,
    pr.codigo_documento,
    pr.nombre,
    cp.id_cabecera_pedidos,
    cp.codigo_estado
order by cp.id_cabecera_pedidos;


-- Mostrar detalles de todas las tablas: 

SELECT table_name,column_name,data_type,udt_name,character_maximum_length,numeric_precision,numeric_scale,
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_schema = 'public'
ORDER BY table_name, ordinal_position;


SELECT setval('categoria_codigo_categoria_seq', 
              (SELECT MAX(codigo_categoria) FROM categoria));
			  
SELECT constraint_name, constraint_type
FROM information_schema.table_constraints
WHERE table_name = 'cabecera_pedidos';

-- Mostrar todos los datos y constraint de tablas en especifico: 

SELECT 
    c.table_name,
    c.column_name,
    c.data_type,
    c.udt_name,
    c.column_default,
    tc.constraint_type
FROM information_schema.columns c
LEFT JOIN information_schema.key_column_usage kcu
       ON c.table_name = kcu.table_name
       AND c.column_name = kcu.column_name
LEFT JOIN information_schema.table_constraints tc
       ON kcu.constraint_name = tc.constraint_name
       AND tc.table_schema = 'public'
WHERE c.table_schema = 'public'
AND c.table_name IN ('detalle_pedidos') -- 'detalle_pedidos'
ORDER BY c.table_name, c.ordinal_position;


-- Mostrar todos los datos y constraint de todas las tablas: 

SELECT 
    c.table_name,
    c.column_name,
    c.data_type,
    c.udt_name,
    c.column_default,
    tc.constraint_type
FROM information_schema.columns c
LEFT JOIN information_schema.key_column_usage kcu
       ON c.table_name = kcu.table_name
       AND c.column_name = kcu.column_name
LEFT JOIN information_schema.table_constraints tc
       ON kcu.constraint_name = tc.constraint_name
       AND tc.table_schema = 'public'
WHERE c.table_schema = 'public'
ORDER BY c.table_name, c.ordinal_position;


-- Mostrar todos los datos y constraint de tablas en especifico: 

SELECT 
    c.table_name,
    c.column_name,
    c.data_type,
    c.udt_name,
    c.column_default,
    tc.constraint_type
FROM information_schema.columns c
LEFT JOIN information_schema.key_column_usage kcu
       ON c.table_name = kcu.table_name
       AND c.column_name = kcu.column_name
LEFT JOIN information_schema.table_constraints tc
       ON kcu.constraint_name = tc.constraint_name
       AND tc.table_schema = 'public'
WHERE c.table_schema = 'public'
AND c.table_name IN ('producto', 'proveedores')
ORDER BY c.table_name, c.ordinal_position;

-- Mostrar informacion de tablas con nombre de la tabla y nombre de la columnna: 
select table_name, column_name
from information_schema.columns
where table_name in ('unidades_de_medidas','categoria','producto','cabecera_pedidos','detalle_pedidos','cabecera_ventas','detalle_venta','historial_stock','estado_pedido','proveedores','tipo_documento')
order by table_name, column_name, ;




-- Mostrar tablas, columnas, tipo de datos, permite null, accion columna, constraint pk and fk: 
SELECT 
    c.table_name,
    c.column_name,
    c.data_type,
    c.is_nullable,
    c.column_default,
    tc.constraint_type
FROM information_schema.columns c
LEFT JOIN information_schema.key_column_usage kcu
    ON c.column_name = kcu.column_name
    AND c.table_name = kcu.table_name
LEFT JOIN information_schema.table_constraints tc
    ON kcu.constraint_name = tc.constraint_name
WHERE c.table_schema = 'public'
ORDER BY c.table_name, c.ordinal_position;




ALTER TABLE cabecera_pedidos
ADD CONSTRAINT cabecera_pedidos_codigo_estado_fkey
FOREIGN KEY (codigo_estado)
REFERENCES estado_pedido(codigo_estado)
ON UPDATE CASCADE;
ON DELETE RESTRICT;










