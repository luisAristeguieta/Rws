package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.DetalleVenta;
import com.krakedev.inventarios.entidades.Ventas;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class VentasBDD {

	public void agregarVantas(Ventas venta) throws KrakeDevException {
		
		// Conexiones y relaciones con sql:
		
        Connection con = null;
        PreparedStatement psInser = null;
        PreparedStatement psDet = null;
        PreparedStatement psIva = null;
        PreparedStatement psActualizarCabecera = null; 
        PreparedStatement psInsertarHistorial = null;
        ResultSet rs = null;
        ResultSet rsIva = null;
        int idGenerado = 0;
        
        
        try {
        
        	// validaciones: 
    		if (venta == null) {
    			throw new KrakeDevException("El registro de venta es null");
    		}

    		if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
    			throw new KrakeDevException("Debe enviar al menos un detalle");
    		}

    		con = ConexionBDD.obtenerConexion();
    		con.setAutoCommit(false); // Para transacciones de varias tablas // todo o nada.
            
        	// Cabecera ventas en cero, autogenerador serial y retorno guardado: 
        	psInser = con.prepareStatement(
            	    "insert into cabecera_ventas (fecha, subtotal, iva, total) " +
            	    "values (?, ?, ?, ?) returning codigo_ventas");
        	
        	java.sql.Timestamp fechaActual = java.sql.Timestamp.valueOf(LocalDateTime.now());
        	psInser.setTimestamp(1, fechaActual);
    		psInser.setBigDecimal(2, BigDecimal.ZERO);
    		psInser.setBigDecimal(3, BigDecimal.ZERO);
    		psInser.setBigDecimal(4, BigDecimal.ZERO);


        	// Uso returning y no Statement.RETURN_GENERATED_KEYS: 
            rs = psInser.executeQuery();

            if (rs.next()) {
            	idGenerado = rs.getInt("codigo_ventas");
            	venta.setIdCabeceraVentas(idGenerado);
            	System.out.println("Codigo generado: " + idGenerado);
            } else {
            	throw new KrakeDevException("No se pudo recuperar el codigo generado de la venta");
            }
            
            rs.close();
            rs = null;
            
			// detalle de ventas, previamente creando una array list en la entidad Ventas y post detalle de ventas: 
            
            ArrayList<DetalleVenta> detallesVenta = venta.getDetalles();
            DetalleVenta det;
            
            // Creo variables de sumatoria de la cabecera ventas y luego actulizo: 
            BigDecimal subtotalCabecera = BigDecimal.ZERO;
            BigDecimal ivaCabecera = BigDecimal.ZERO;
            BigDecimal totalCabecera = BigDecimal.ZERO;
            
            
            for (int i =0;i<detallesVenta.size();i++) {
            	det = detallesVenta.get(i);
            	psDet =con.prepareStatement("insert into detalle_venta "
        				+ "(codigo_ventas, codigo_prod, cantidad, precio_unitario, subtotal, total) "
        				+ "values (?, ?, ?, ?, ?, ?)");
            	
            	psDet.setInt(1, idGenerado);
            	int codigoPro = det.getCodigoProd().getCodigoProd();
            	psDet.setInt(2, codigoPro);
            	psDet.setInt(3, det.getCantidad());
            	BigDecimal cantidadBig= new BigDecimal(det.getCantidad());
            	BigDecimal precioVenta = det.getCosto().getPrecioVenta();
            	psDet.setBigDecimal(4, precioVenta);
            	
            	// Consulta si el precio tiene iva en la tabla producto para asignacion del valor subtotal y total: 
            	psIva = con.prepareStatement(
            	        "select iva from producto where codigo_prod = ?");
            	psIva.setInt(1, codigoPro);
            	rsIva = psIva.executeQuery();
            	
            	boolean tieneIva = false;
            	
            	if (rsIva.next()) {
    				tieneIva = rsIva.getBoolean("iva");
    			} else {
    				throw new KrakeDevException("No existe el producto con el codigo: " + codigoPro);
    			}
            	
            	// Cierro y asigno sino al finaly: 
            	rsIva.close();
    			rsIva = null;
    			psIva.close();
    			psIva = null;
    			

    			BigDecimal subtotalDetalle = precioVenta.multiply(cantidadBig);
    			BigDecimal ivaDetalle = BigDecimal.ZERO;
    			BigDecimal totalDetalle = subtotalDetalle;
            	
            	if (tieneIva) {
            		BigDecimal baseIva = new BigDecimal("0.12");
    				ivaDetalle = subtotalDetalle.multiply(baseIva);
    				totalDetalle = subtotalDetalle.add(ivaDetalle);
    			}
            	
            	psDet.setBigDecimal(5, subtotalDetalle);
    			psDet.setBigDecimal(6, totalDetalle);
            	
    			// subtotalCabecera += subtotalDetalle; Contadores con big son otro estilo 
    			subtotalCabecera = subtotalCabecera.add(subtotalDetalle);
    			ivaCabecera = ivaCabecera.add(ivaDetalle);
    			totalCabecera = totalCabecera.add(totalDetalle);
    			
    			
            	psDet.executeUpdate();
            	psDet.close();
            	psDet = null;
            	
            	// Historial stock para el registro individual de cda venta: agregar el negativo.
            	psInsertarHistorial = con.prepareStatement(
            			"insert into historial_stock (fecha, referencia, codigo_prod, cantidad_stock) " +
            			"values (?, ?, ?, ?)");

            		java.sql.Timestamp fechaHistorial = java.sql.Timestamp.valueOf(LocalDateTime.now());
            		String referencia = "Venta " + idGenerado;
            		int cantidadStock = det.getCantidad() * -1;

            		psInsertarHistorial.setTimestamp(1, fechaHistorial);
            		psInsertarHistorial.setString(2, referencia);
            		psInsertarHistorial.setInt(3, codigoPro);
            		psInsertarHistorial.setInt(4, cantidadStock);

            		int filasHistorial = psInsertarHistorial.executeUpdate();

            		if (filasHistorial == 0) {
            			throw new KrakeDevException("No se pudo registrar el historial de stock del producto " + codigoPro);
            		}

            		psInsertarHistorial.close();
            		psInsertarHistorial = null;
            	
            	
            } 
            
            // actulizo fuera del for con los contadores previs:
            psActualizarCabecera = con.prepareStatement(
        			"update cabecera_ventas " +
        			"set subtotal = ?, iva = ?, total = ? " +
        			"where codigo_ventas = ?");

        		psActualizarCabecera.setBigDecimal(1, subtotalCabecera);
        		psActualizarCabecera.setBigDecimal(2, ivaCabecera);
        		psActualizarCabecera.setBigDecimal(3, totalCabecera);
        		psActualizarCabecera.setInt(4, venta.getIdCabeceraVentas());

        		int filasCabecera = psActualizarCabecera.executeUpdate();

        		if (filasCabecera == 0) {
        			throw new KrakeDevException("No se pudo actualizar la cabecera de la venta");
        		}
            
        		
            con.commit(); // Todo o nada
            
        } catch (Exception e) {
			try {if (con != null) {con.rollback();} // Todo o nada.
			} catch (Exception ex) { ex.printStackTrace();}
			if (e instanceof KrakeDevException) {
				throw (KrakeDevException)e;
			} else {
				throw new KrakeDevException("Error al recibir venta: " + e.getMessage());
			}
        } finally {
        	if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        	if (rsIva != null) try { rsIva.close(); } catch (SQLException e) { e.printStackTrace(); }
        	if (psIva != null) try { psIva.close(); } catch (SQLException e) { e.printStackTrace(); }
        	if (psDet != null) try { psDet.close(); } catch (SQLException e) { e.printStackTrace(); }
        	if (psActualizarCabecera != null) try { psActualizarCabecera.close(); } catch (SQLException e) { e.printStackTrace(); }
        	if (psInsertarHistorial != null) try { psInsertarHistorial.close(); } catch (SQLException e) { e.printStackTrace(); }
        	if (psInser != null) try { psInser.close(); } catch (SQLException e) { e.printStackTrace(); }
        	if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        	
	}
	
}
