package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;


public class ProveedoresBDD {

	// Busquedad por objetos:
	
			public ArrayList<Proveedor> buscar(String subcadena) throws KrakeDevException {

				ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();

				Connection con = null; // Variables tipo: Connection / PreparedStatement / ResultSet
				PreparedStatement ps = null;
				ResultSet rs = null;
				Proveedor proveedor = null;

				try {
					con = ConexionBDD.obtenerConexion();
					ps = con.prepareStatement("select * from proveedores where upper(nombre) like ? ");
					ps.setString(1, "%"+subcadena.toUpperCase()+"%");
					
					rs = ps.executeQuery();
	
					// Bucle tipo while:

					while (rs.next()) {

						String idproveedor = rs.getString("id_proveedor");
						String codigodocumento = rs.getString("codigo_documento");
						String nombre = rs.getString("nombre");
						String telefono = rs.getString("telefono");
						String correo = rs.getString("correo");
						String direccion = rs.getString("direccion");
						proveedor = new Proveedor(idproveedor, codigodocumento, nombre,telefono,correo,direccion);

						proveedores.add(proveedor);

					}

				} catch (KrakeDevException e) {
					e.printStackTrace();
					throw e;
				} catch (SQLException e) {
					e.printStackTrace();
					throw new KrakeDevException("Error al consultar : " + e.getMessage());
				}

				return proveedores;
			}
}
