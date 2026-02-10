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

public class ProductoBDD {
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
}
