package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;
import com.krakedev.inventarios.validaciones.ValidadorCategoria;

public class CategoriaBDD2 {
	
	public void actualizarCategoria(Categoria categoria) throws KrakeDevException {
		
		ValidadorCategoria.validarDatosCategoria(categoria);
		
		Connection con = null;
		PreparedStatement psBuscarCategoria = null;
		PreparedStatement psBuscarCategoriaPadre = null;
		PreparedStatement psActualizarCategoria = null;
		
		ResultSet rs = null;

		try {
			con = ConexionBDD.obtenerConexion();
			
			// Se resuleve 1 que exista el codigo de la categoria enviado para actualizar: 
			
			int codigoConsultado = categoria.getCodigoCategoria(); 			
			psBuscarCategoria = con.prepareStatement( "select codigo_categoria from categoria where codigo_categoria = ?");
			
			psBuscarCategoria.setInt(1, codigoConsultado);
			rs = psBuscarCategoria.executeQuery();
			
			if (!rs.next()) {
				throw new KrakeDevException("No existe el codigo: " + codigoConsultado);
			}
			
			rs.close();
			psBuscarCategoria.close();
			
			// Sabiendo que existe el codigo de la categoria a actualizar y sigue viva el metodo, permito valores null en 
			// codigo de categoria padre: 
			
			Categoria objetoCodigoPadre = categoria.getCategoriaPadre();
			Integer codigoPadreConsultado = null;

			if (objetoCodigoPadre != null) {
				codigoPadreConsultado = categoria.getCategoriaPadre().getCodigoCategoria();
			
			psBuscarCategoriaPadre = con.prepareStatement( "select codigo_categoria from categoria where codigo_categoria = ?");;
			psBuscarCategoriaPadre.setInt(1, codigoPadreConsultado);
			
			rs = psBuscarCategoriaPadre.executeQuery();
			
			if (!rs.next()) {
				throw new KrakeDevException("No existe el codigo de categoria: " + codigoPadreConsultado);
			}
			
			rs.close();
			psBuscarCategoriaPadre.close();

			} 

			
			psActualizarCategoria = con.prepareStatement( "update categoria set nombre = ?, categoria_padre = ? where codigo_categoria = ?");
			psActualizarCategoria.setString(1, categoria.getNombre());
			if(categoria.getCategoriaPadre() == null) {
				psActualizarCategoria.setNull(2, java.sql.Types.INTEGER);
			} else {
				psActualizarCategoria.setInt(2, codigoPadreConsultado); } // Requiere validacion: que exista la categoria y que pueda ser null (2)
			psActualizarCategoria.setInt(3,codigoConsultado); // Requiere validacion que exista (1)
			
			int filasActualizadas = psActualizarCategoria.executeUpdate(); //
			
			if (filasActualizadas == 0) {
				throw new KrakeDevException("No se pudo actualizar la categoria");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al actualizar producto: " + e.getMessage());
		} finally {
			try {
				if (rs != null) rs.close();
				//if (psBuscarCategoria != null) psBuscarCategoria.close();
				if (psBuscarCategoriaPadre != null) psBuscarCategoriaPadre.close();
				if (psActualizarCategoria != null) psActualizarCategoria.close();
				if (con != null) con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}	
		}
	}
		
	
	
	
	
	public void agregarCategoria(Categoria categoria) throws KrakeDevException {
		
		Connection con = null;
		PreparedStatement psBuscarCategoria = null;
		PreparedStatement psAgregarCategoria = null;
		
		ResultSet rs = null;
		try {
			con = ConexionBDD.obtenerConexion();
			
			// Sabiendo que el valor de la categoria padres puede ser null sino se envia por defecto null:
			// Valido que exista: 
			
			// Verifico codigo existente: 
			
			Integer codigoPadre = null;
			
			if (categoria.getCategoriaPadre() != null) {
				
				codigoPadre = categoria.getCategoriaPadre().getCodigoCategoria();
				
				psBuscarCategoria = con.prepareStatement( "select codigo_categoria from categoria where codigo_categoria = ?");
				psBuscarCategoria.setInt(1, categoria.getCodigoCategoria());
				
				rs = psBuscarCategoria.executeQuery();
				
				if (!rs.next()) {
					throw new KrakeDevException("No existe el codigo" + codigoPadre + "para asignar en codigo padre");
				}
				
				rs.close();
				psBuscarCategoria.close();
			}
			
			psAgregarCategoria = con.prepareStatement("insert into categoria "
	                + "(nombre, categoria_padre) "
	                + "values (?, ?)");
			
			psAgregarCategoria.setString(1, categoria.getNombre());
			
			// Estando aca hay que asignar null o asignar un codigo existente con la validacion de arriba
			if(codigoPadre == null) {
				psAgregarCategoria.setNull(2,java.sql.Types.INTEGER);
			} else {
				psAgregarCategoria.setInt(2, codigoPadre);
			}

			psAgregarCategoria.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al actualizar producto: " + e.getMessage()); // Error en la sql
		} finally {
			try {
				if (rs != null) rs.close();
				if (psBuscarCategoria != null) psBuscarCategoria.close();
				if (psAgregarCategoria != null) psAgregarCategoria.close();
				if (con != null) con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}	
		}
	}		
}

