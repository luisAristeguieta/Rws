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

	
	public Proveedor buscarProveedor(String idProveedorConsulta) throws KrakeDevException {
	    Connection con = null;
	    PreparedStatement psBuscarProveedor = null;
	    ResultSet rs = null;

	    try {
	        con = ConexionBDD.obtenerConexion();

	        psBuscarProveedor = con.prepareStatement(
	            "select p.id_proveedor, p.codigo_documento, p.nombre, p.telefono, p.correo, p.direccion "
	            + "from proveedores p "
	            + "where p.id_proveedor = ?"
	        );

	        psBuscarProveedor.setString(1, idProveedorConsulta);
	        rs = psBuscarProveedor.executeQuery();

	        if (!rs.next()) {
	            throw new KrakeDevException("No existe proveedor con el id: " + idProveedorConsulta);
	        }

	        TipoDocumento tipoDocumento = new TipoDocumento();
	        tipoDocumento.setCodigoDocumento(rs.getString("codigo_documento"));

	        Proveedor proveedor = new Proveedor();
	        proveedor.setIdProveedor(rs.getString("id_proveedor"));
	        proveedor.setTipoDocumento(tipoDocumento);
	        proveedor.setNombre(rs.getString("nombre"));
	        proveedor.setTelefono(rs.getString("telefono"));
	        proveedor.setCorreo(rs.getString("correo"));
	        proveedor.setDireccion(rs.getString("direccion"));

	        return proveedor;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new KrakeDevException("Error SQL al buscar proveedor: " + e.getMessage());
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (psBuscarProveedor != null) psBuscarProveedor.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	
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

						String idProveedor = rs.getString("id_proveedor");
						String codigoDocumento = rs.getString("codigo_documento");
						String descripcionTipoDocumento = rs.getString("descripcion");
						String nombre = rs.getString("nombre");
						String telefono = rs.getString("telefono");
						String correo = rs.getString("correo");
						String direccion = rs.getString("direccion");
						TipoDocumento td = new TipoDocumento(codigoDocumento, descripcionTipoDocumento);
						proveedor = new Proveedor(idProveedor, td, nombre, telefono, correo, direccion);

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
		        if (proveedor.getIdProveedor()== null || proveedor.getIdProveedor().isEmpty()) {
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
					psEva.setString(1, proveedor.getIdProveedor());
			            rs = psEva.executeQuery();

			            if (rs.next()) {
			                throw new KrakeDevException( "El proveedor con id " + proveedor.getIdProveedor() + " ya se encuentra registrado");
			            }

			        psInser = con.prepareStatement("insert into proveedores "
		                + "(id_proveedor, codigo_documento, nombre, telefono, correo, direccion) "
		                + "values (?, ?, ?, ?, ?, ?)");

			        psInser.setString(1, proveedor.getIdProveedor());
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
