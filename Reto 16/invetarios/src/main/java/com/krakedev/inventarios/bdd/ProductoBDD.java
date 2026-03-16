package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.UnidadDeMedidas;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;
import com.krakedev.inventarios.validaciones.ValidadorProducto;

public class ProductoBDD {
	
	public Producto buscarProducto(int codigoProductoConsulta) throws KrakeDevException {
	    Connection con = null;
	    PreparedStatement psBuscarProducto = null;
	    ResultSet rs = null;

	    try {
	        con = ConexionBDD.obtenerConexion();

	        psBuscarProducto = con.prepareStatement(
	            "select p.codigo_prod, p.nombre, p.precio_venta, p.iva, p.costo, "
	            + "p.stock_producto, c.codigo_categoria, c.nombre as nombre_categoria "
	            + "from producto p "
	            + "inner join categoria c on p.codigo_categoria = c.codigo_categoria "
	            + "where p.codigo_prod = ?"
	        );

	        psBuscarProducto.setInt(1, codigoProductoConsulta);
	        rs = psBuscarProducto.executeQuery();

	        if (!rs.next()) {
	            throw new KrakeDevException("No existe producto con el codigo: " + codigoProductoConsulta);
	        }

	        Categoria categoria = new Categoria();
	        categoria.setCodigoCategoria(rs.getInt("codigo_categoria"));
	        categoria.setNombre(rs.getString("nombre_categoria"));

	        Producto producto = new Producto();
	        producto.setCodigoProd(rs.getInt("codigo_prod"));
	        producto.setNombre(rs.getString("nombre"));
	        producto.setPrecioVenta(rs.getBigDecimal("precio_venta"));
	        producto.setIva(rs.getBoolean("iva"));
	        producto.setCosto(rs.getBigDecimal("costo"));
	        producto.setStockProducto(rs.getInt("stock_producto"));
	        producto.setCodigoCategoria(categoria);

	        return producto;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new KrakeDevException("Error SQL al buscar producto: " + e.getMessage());
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (psBuscarProducto != null) psBuscarProducto.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	public void actualizarProducto(Producto producto) throws KrakeDevException {
		Connection con = null;
		PreparedStatement psBuscarProducto = null;
		PreparedStatement psBuscarCategoria = null;
		PreparedStatement psBuscarUnidad = null;
		PreparedStatement psActualizar = null;
		ResultSet rs = null;

		// con la clase de validaciones agregada basicas: 
		ValidadorProducto.validarDatosProducto(producto);

		// valido que el codigo del producto enviado no sea negativo, posterior consulto que exista: 
		if (producto.getCodigoProd() <= 0) {
			throw new KrakeDevException("Debe enviar el codigo del producto");
		}

		try {
			con = ConexionBDD.obtenerConexion();
			
			// verifico que exista el codigo del producto producto
			psBuscarProducto = con.prepareStatement(
				"select codigo_prod from producto where codigo_prod = ?"
			);
			psBuscarProducto.setInt(1, producto.getCodigoProd());
			rs = psBuscarProducto.executeQuery();

			if (!rs.next()) {
				throw new KrakeDevException("No existe un producto con ese codigo");
			}

			rs.close();
			rs = null;
			psBuscarProducto.close();
			psBuscarProducto = null;

			// verifico que exista el codigo de la categoria:
			psBuscarCategoria = con.prepareStatement(
				"select codigo_categoria from categoria where codigo_categoria = ?"
			);
			psBuscarCategoria.setInt(1, producto.getCodigoCategoria().getCodigoCategoria());
			rs = psBuscarCategoria.executeQuery();

			if (!rs.next()) {
				throw new KrakeDevException("No existe una categoria con ese codigo");
			}

			rs.close();
			rs = null;
			psBuscarCategoria.close();
			psBuscarCategoria = null;

			// verifico que exista el codigo de la la unidad de medida
			psBuscarUnidad = con.prepareStatement(
				"select codigo_udm from unidades_de_medidas where codigo_udm = ?"
			);
			psBuscarUnidad.setString(1, producto.getCodigoUdm().getCodigoUdm());
			rs = psBuscarUnidad.executeQuery();

			if (!rs.next()) {
				throw new KrakeDevException("No existe una unidad de medida con ese codigo");
			}

			rs.close();
			rs = null;
			psBuscarUnidad.close();
			psBuscarUnidad = null;

			// si todo existe dentro de las tablas, procedo a actualizar: 
			psActualizar = con.prepareStatement(
					"update producto set nombre=?, codigo_udm=?, precio_venta=?, iva=?, "
					+ "costo=?, codigo_categoria=?, stock_producto=? "
					+ "where codigo_prod=?");

			psActualizar.setString(1, producto.getNombre());
			psActualizar.setString(2, producto.getCodigoUdm().getCodigoUdm());
			psActualizar.setBigDecimal(3, producto.getPrecioVenta());
			psActualizar.setBoolean(4, producto.isIva());
			psActualizar.setBigDecimal(5, producto.getCosto());
			psActualizar.setInt(6, producto.getCodigoCategoria().getCodigoCategoria());
			psActualizar.setInt(7, producto.getStockProducto());
			psActualizar.setInt(8, producto.getCodigoProd());

			int filasActualizadas = psActualizar.executeUpdate();

			if (filasActualizadas == 0) {
				throw new KrakeDevException("No se pudo actualizar el producto");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al actualizar producto: " + e.getMessage()); // Error en la sql
		} catch (KrakeDevException e) {
			throw e; // Error ingresado en el codigo / validaciones 
		} catch (Exception e) {
			e.printStackTrace();
			throw new KrakeDevException("Error general al actualizar producto"); // Otro errores que no sea de los de arriba
		} finally {
			try {
				if (rs != null) rs.close();
				if (psBuscarProducto != null) psBuscarProducto.close();
				if (psBuscarCategoria != null) psBuscarCategoria.close();
				if (psBuscarUnidad != null) psBuscarUnidad.close();
				if (psActualizar != null) psActualizar.close();
				if (con != null) con.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	
	
	public ArrayList<Producto> buscar(String subcadena) throws KrakeDevException {

		ArrayList<Producto> productos = new ArrayList<Producto>();

		// Variables tipo: Connection / PreparedStatement / ResultSet:
		Connection con = null; 
		PreparedStatement ps = null;
		ResultSet rs = null;
		Producto producto = null;

		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement( "select pr.codigo_prod, pr.nombre, pr.codigo_udm, udm.descripcion as descripcion_udm, "
					+ "       cast(pr.precio_venta as decimal(6,3)) as precio_venta, pr.iva, cast(pr.costo as decimal(6,3)) as costo, "
					+ "       pr.codigo_categoria, ca.nombre as nombre_categoria, pr.stock_producto "
				    + "from producto pr "
				    + "inner join unidades_de_medidas udm on pr.codigo_udm = udm.codigo_udm "
				    + "inner join categoria ca on pr.codigo_categoria = ca.codigo_categoria "
				    + "where upper(pr.nombre) like ?");
			
			ps.setString(1, "%"+subcadena.toUpperCase()+"%");
			rs = ps.executeQuery();

			// Bucle tipo while:

			while (rs.next()) {

				int codigoProd = rs.getInt("codigo_prod");
				String nombre = rs.getString("nombre");
				String codigoUdm = rs.getString("codigo_udm");
				String descripcion_udm = rs.getString("descripcion_udm");
				BigDecimal precioVenta = rs.getBigDecimal("precio_venta");
				boolean iva = rs.getBoolean("iva");
				BigDecimal costo = rs.getBigDecimal("costo");
				int codigoCategoria = rs.getInt("codigo_categoria");
				String nombreCategoria = rs.getString("nombre_categoria");
				int stockProducto = rs.getInt("stock_producto");
			
				
				UnidadDeMedidas udm = new UnidadDeMedidas();
				udm.setCodigoUdm(codigoUdm);
				udm.setDescripcion(descripcion_udm);
				
				Categoria cat = new Categoria();
				cat.setCodigoCategoria(codigoCategoria);
				cat.setNombre(nombreCategoria);
				
				producto = new Producto(codigoProd, nombre, udm, precioVenta, iva, costo, cat, stockProducto);
				
				productos.add(producto);

			}

		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar : " + e.getMessage());
		} finally {
			// Se manejeria todas las conexiones aperturadas y captaria la excepcion real en rs, ps y con
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    if (con != null) {
		        try {
		            con.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}
		
		return productos;
	}
	
	public void agregarProducto(Producto producto) throws KrakeDevException {
		
		// Validaciones en la inserccion que no sea null ninguno de los campos obligartorios: 
		
		if (producto == null) {
	        throw new KrakeDevException("Producto es null");
	    }
	    if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
	        throw new KrakeDevException("El nombre del producto es obligatorio");
	    }
	    if (producto.getCodigoUdm() == null || producto.getCodigoUdm().getCodigoUdm() == null
	            || producto.getCodigoUdm().getCodigoUdm().isEmpty()) {
	        throw new KrakeDevException("El codigo Udm es obligatorio");
	    }
	    if (producto.getPrecioVenta() == null) {
	        throw new KrakeDevException("El precio de venta es obligatorio");
	    }
	    if (producto.getCosto() == null) {
	        throw new KrakeDevException("El costo es obligatorio");
	    }
	    if (producto.getCodigoCategoria() == null || producto.getCodigoCategoria().getCodigoCategoria() <= 0) {
	        throw new KrakeDevException("La categoría es obligatoria");
	    }
	    if (producto.getStockProducto() < 0) {
	        throw new KrakeDevException("El stock no puede ser negativo");
	    }
        
        
		Connection con = null;
		PreparedStatement psEva = null; // Se agrega para validar si existe el proveedor
		PreparedStatement psInser = null; //  Se agrega para insertarlo sino existe duplicado
		ResultSet rs = null;

		try {
			con = ConexionBDD.obtenerConexion();

			// Se valida duplicado del proveedor:
			psEva = con.prepareStatement("select 1 from producto where upper(nombre) = upper(?)");
				psEva.setString(1, producto.getNombre());
	            rs = psEva.executeQuery();

	            if (rs.next()) {
	                throw new KrakeDevException(
	                    "El producto con nombre '" + producto.getNombre() + "' ya existe");
	            }

	        psInser = con.prepareStatement("insert into producto "
                + "(nombre,codigo_udm,precio_venta,iva,costo,codigo_categoria,stock_producto) "
                + "values (?, ?, ?, ?, ?, ?, ?)");

	        psInser.setString(1,producto.getNombre());
	        psInser.setString(2,producto.getCodigoUdm().getCodigoUdm());
	        psInser.setBigDecimal(3, producto.getPrecioVenta());
	        psInser.setBoolean(4, producto.isIva());
	        psInser.setBigDecimal(5, producto.getCosto());
	        psInser.setInt(6, producto.getCodigoCategoria().getCodigoCategoria());
	        psInser.setInt(7, producto.getStockProducto());

	        psInser.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar Proveedor" + e.getMessage());
		} catch (KrakeDevException e) {
			throw e;
		} finally {
			// Se manejeria todas las conexiones aperturadas y captaria la excepcion real en rs, psEva, psInser y con
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    if (psEva != null) {
		        try {
		            psEva.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    if (psInser != null) {
		        try {
		            psInser.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    if (con != null) {
		        try {
		            con.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}
	}
}
