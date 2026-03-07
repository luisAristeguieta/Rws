package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class TipoDocumentoBDD {
	
	public ArrayList<TipoDocumento> mostrarDocumento() throws KrakeDevException {

		ArrayList<TipoDocumento> documentos = new ArrayList<TipoDocumento>();

		Connection con = null; // Variables tipo: Connection / PreparedStatement / ResultSet
		PreparedStatement ps = null;
		ResultSet rs = null;
		TipoDocumento documento = null;

		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("select * from tipo_documento");
			
			rs = ps.executeQuery();

			// Bucle tipo while:

			while (rs.next()) {

			    String codigodocumento = rs.getString("codigo_documento");
			    String descripcion = rs.getString("descripcion");

			    documento = new TipoDocumento(codigodocumento, descripcion);
			    documentos.add(documento);

			}

		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar : " + e.getMessage());
		}

		return documentos;
	}
}


