package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.entidades.TipoDocumento;
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
					ps = con.prepareStatement(
						    "select pv.id_proveedor, pv.codigo_documento, td.descripcion, "
						    + "       pv.nombre, pv.telefono, pv.correo, pv.direccion "
						    + "from proveedores pv "
						    + "inner join tipo_documento td on pv.codigo_documento = td.codigo_documento "
						    + "where upper(pv.nombre) like ?");
					
					ps.setString(1, "%"+subcadena.toUpperCase()+"%");
					
					rs = ps.executeQuery();
	
					// Bucle tipo while:

					while (rs.next()) {

						String idproveedor = rs.getString("id_proveedor");
						String codigodocumento = rs.getString("codigo_documento");
						String descripcionTipoDocumento = rs.getString("descripcion");
						String nombre = rs.getString("nombre");
						String telefono = rs.getString("telefono");
						String correo = rs.getString("correo");
						String direccion = rs.getString("direccion");
						TipoDocumento td = new TipoDocumento(codigodocumento, descripcionTipoDocumento);
						proveedor = new Proveedor(idproveedor, td, nombre, telefono, correo, direccion);

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
			
			// Agregar proveedor: 
			
			public void agregarProveedor(Proveedor proveedor) throws KrakeDevException {
				
				// Validaciones en la inserccion que no sea null ninguno de los campos obligartorios: 
				
				if (proveedor == null) {
		            throw new KrakeDevException("Proveedor es null");
		        }
		        if (proveedor.getIdproveedor() == null || proveedor.getIdproveedor().isEmpty()) {
		            throw new KrakeDevException("id_proveedor es obligatorio");
		        }
		        if (proveedor.getTipoDocumento() == null || proveedor.getTipoDocumento().getCodigoDocumento().isEmpty()) {
		            throw new KrakeDevException("El codigo del documento es obligatorio");
		        }
		        if (proveedor.getNombre() == null || proveedor.getNombre().isEmpty()) {
		            throw new KrakeDevException("El noombre del proveedor es obligatorio");
		        }
		        if (proveedor.getTelefono() == null || proveedor.getTelefono().isEmpty()) {
		        	throw new KrakeDevException("El numero telefonico del proveedor es obligatorio");
		        }
		        if (proveedor.getCorreo() == null || proveedor.getCorreo().isEmpty()) {
		        	throw new KrakeDevException("El correo del proveedor es obligatorio");
		        }
		        if (proveedor.getDireccion() == null || proveedor.getDireccion().isEmpty()) {
		        	throw new KrakeDevException("La direccion del proveedor es obligatorio");
		        }
		        
		        
		        
				Connection con = null;
				PreparedStatement psEva = null; // Se agrega para validar si existe el proveedor
				PreparedStatement psInser = null; //  Se agrega para insertarlo sino existe duplicado
				ResultSet rs = null;

				try {
					con = ConexionBDD.obtenerConexion();

					// Se valida duplicado del proveedor:
					psEva = con.prepareStatement("select 1 from proveedores where id_proveedor = ?");
					psEva.setString(1, proveedor.getIdproveedor());
			            rs = psEva.executeQuery();

			            if (rs.next()) {
			                throw new KrakeDevException( "El proveedor con id " + proveedor.getIdproveedor() + " ya se encuentra registrado");
			            }

			        psInser = con.prepareStatement("insert into proveedores "
		                + "(id_proveedor, codigo_documento, nombre, telefono, correo, direccion) "
		                + "values (?, ?, ?, ?, ?, ?)");

			        psInser.setString(1, proveedor.getIdproveedor());
			        psInser.setString(2, proveedor.getTipoDocumento().getCodigoDocumento());
			        psInser.setString(3, proveedor.getNombre());
			        psInser.setString(4, proveedor.getTelefono());
			        psInser.setString(5, proveedor.getCorreo());
			        psInser.setString(6, proveedor.getDireccion());

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
