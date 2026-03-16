package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.Pedidos;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;
import com.krakedev.inventarios.validaciones.ValidadorCategoria;


public class CategoriaBDD {

public ArrayList<Categoria> consultarCategoria() throws KrakeDevException {
		
	ArrayList<Categoria> categorias = new ArrayList<Categoria>();
	Connection con = null;
	PreparedStatement psBuscarCategoria= null;
	ResultSet rs = null;
		
	// Idea general: Recupera la tabla existente y muestra en formato json
		
		try {
			con = ConexionBDD.obtenerConexion();
			
			psBuscarCategoria = con.prepareStatement(
				    "select c.codigo_categoria, c.nombre, c.categoria_padre, cp.nombre as nombre_padre " +
				    "from categoria c " +
				    "left join categoria cp on c.categoria_padre = cp.codigo_categoria " +
				    "order by c.codigo_categoria");
						    
            rs = psBuscarCategoria.executeQuery();
			
            while (rs.next()) {
            	int codigoCategoria = rs.getInt("codigo_categoria");
            	String nombre = rs.getString("nombre");
            	
            	if (nombre == null ) {
            		nombre = "Sin Nombre";
            	}
            	
            	int codigoCategoriaPadre = rs.getInt("categoria_padre");
            	
            	Categoria padre = null;
            	
            	if (!rs.wasNull() ) {
            		String nombrePadre = rs.getString("nombre_padre");
            		if (nombrePadre == null) {
    					nombrePadre = "Sin Nombre";
    				}

            		padre = new Categoria();
    				padre.setCodigoCategoria(codigoCategoriaPadre);
    				padre.setNombre(nombrePadre);
            	}
            	
            	Categoria categoria = new Categoria(codigoCategoria, nombre, padre);
    			categorias.add(categoria);
            }
			 
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error SQL en la categoria: " + e.getMessage()); // Error en la sql 
		} finally {
			try {
				if (rs != null) rs.close();
				if (psBuscarCategoria != null) psBuscarCategoria.close();
				if (con != null) con.close();
			} catch (SQLException e) { 
				e.printStackTrace();}
		}
		
		return categorias;
	}



	
	public void actualizarCategoria(Categoria categoria) throws KrakeDevException {
		
		Connection con = null;
		PreparedStatement psBuscarCategoria= null;
		PreparedStatement psCategoriaPadre = null;
		PreparedStatement psActualizar = null;
		ResultSet rs = null;
		
		// Idea general: valido que exista el codigo de categoria
		// valido categoria padre o asigno null sino se envia permitiendole
		// Actualiza la categoria
		ValidadorCategoria.validarDatosCategoria(categoria);
		
		try {
			con = ConexionBDD.obtenerConexion();
			
			// Verifico que la categoria exista por su codigo: 
			psBuscarCategoria = con.prepareStatement(
					"select codigo_categoria from categoria where codigo_categoria = ?"
				);
			psBuscarCategoria.setInt(1, categoria.getCodigoCategoria());
			rs = psBuscarCategoria.executeQuery();

			if (!rs.next()) {
				throw new KrakeDevException("No existe categoria con el codigo " + categoria.getCodigoCategoria());
			}

			rs.close();
			psBuscarCategoria.close();
				
			// Permito valores null usando Integer en vez de int en categoria padre, que se pueda asigna y verifico que exista: 
			Integer codigoPadre = null;
			
			if (categoria.getCategoriaPadre() != null) {
				
				codigoPadre = categoria.getCategoriaPadre().getCodigoCategoria();
				
				// Valido de su existencia en la misma tabla ya dado el caso que sen envie:
				psCategoriaPadre = con.prepareStatement( "select codigo_categoria from categoria where codigo_categoria = ?");
			
				psCategoriaPadre.setInt(1,codigoPadre);
				rs =  psCategoriaPadre.executeQuery();
				
				if(!rs.next()) {
					throw new KrakeDevException("La categoria padre con codigo " + codigoPadre + " no existe");
				}
			
				rs.close();
				psCategoriaPadre.close();
			}
			
			// Previa verificacion, procedo a actualizar en categoria: 
			
			psActualizar = con.prepareStatement("update categoria set nombre = ?, categoria_padre = ? where codigo_categoria = ?");
			
			psActualizar.setString(1,categoria.getNombre());
			 if (codigoPadre == null) {
				 psActualizar.setNull(2, java.sql.Types.INTEGER);
				} else {
					psActualizar.setInt(2, codigoPadre);
				}
			 psActualizar.setInt(3, categoria.getCodigoCategoria());
			 
			 psActualizar.executeUpdate();
			 
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error SQL en la categoria: " + e.getMessage()); // Error en la sql 
		} finally {
			try {
				if (rs != null) rs.close();
				if (psBuscarCategoria != null) psBuscarCategoria.close();
				if (psActualizar != null) psActualizar.close();
				if (psCategoriaPadre != null) psCategoriaPadre.close();
				if (con != null) con.close();
			} catch (SQLException e) { 
				e.printStackTrace();}
		}
		
		
	}
	
	
	public void agregarCategoria(Categoria categoria) throws KrakeDevException {
		Connection con = null;
		PreparedStatement psInser = null;
		PreparedStatement psBuscarCategoria = null;
		ResultSet rs = null;
		
		ValidadorCategoria.validarDatosCategoria(categoria);
		
		try {
			con = ConexionBDD.obtenerConexion();
			
			// Permito valores null usando Integer en vez de int en categoria padre, que se pueda asigna y verifico que exista: 
			Integer codigoPadre = null;
			
			if (categoria.getCategoriaPadre() != null) {
				
				codigoPadre = categoria.getCategoriaPadre().getCodigoCategoria();
				
				// Valido de su existencia en la misma tabla ya dado el caso que sen envie:
				psBuscarCategoria = con.prepareStatement( "select codigo_categoria from categoria where codigo_categoria = ?");
			
				psBuscarCategoria.setInt(1,codigoPadre);
				rs =  psBuscarCategoria.executeQuery();
				
				if(!rs.next()) {
					throw new KrakeDevException("La categoria padre con codigo " + codigoPadre + " no existe");
				}
				
				rs.close();
				psBuscarCategoria.close();
			}
			// Previa verificacion, procedo a insertar en categoria: 
			
			psInser = con.prepareStatement("insert into categoria (nombre, categoria_padre) values (?, ?)");
			
			 psInser.setString(1,categoria.getNombre());
			 if (codigoPadre == null) {
				    psInser.setNull(2, java.sql.Types.INTEGER);
				} else {
				    psInser.setInt(2, codigoPadre);
				}
			
			 psInser.executeUpdate();
			 
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error SQL en la categoria: " + e.getMessage()); // Error en la sql 
		} finally {
			try {
				if (rs != null) rs.close();
				if (psBuscarCategoria != null) psBuscarCategoria.close();
				if (psInser != null) psInser.close();
				if (con != null) con.close();
			} catch (SQLException e) { 
				e.printStackTrace();}
		}
	}
}
