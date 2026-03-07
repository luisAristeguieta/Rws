package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.EstadoPedido;
import com.krakedev.inventarios.entidades.Pedidos;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class PedidosBDD {

	
	
	public void recibirPedido(Pedidos pedido) throws KrakeDevException {
		Connection con = null;
		PreparedStatement psCabecera = null; // Consulta en tabla cabecera
		PreparedStatement psDetalle = null; // Consulta detalle pedidos
		PreparedStatement psCosto = null; // Consulta para valor del producto internamente
		PreparedStatement psActualizarDetalle = null; // Actualizo detalle pedido
		PreparedStatement psActualizarCabecera = null; // Actualizo estado pedido
		ResultSet rs = null;

		try {
			// validar objeto
			if (pedido == null) {
				throw new KrakeDevException("El pedido es null");}

			if (pedido.getIdCabeceraPedidos() <= 0) {
				throw new KrakeDevException("Debe ingresar un id de cabecera valido");}

			if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
				throw new KrakeDevException("Debe enviar al menos un detalle");}

			con = ConexionBDD.obtenerConexion();
			con.setAutoCommit(false); // Para que en la base de datos no se guarda inmediatamente 
			// hasta que verificar en las tablas involucradas esten correctas hasta la confirmacion por diferentes transacciones.
			

			// Valido que exista el id en la cabecera y este en estado Solicitado sino manejo excepcion
			psCabecera = con.prepareStatement(
				"select id_cabecera_pedidos, codigo_estado " +
				"from cabecera_pedidos " +
				"where id_cabecera_pedidos = ?");
			
			psCabecera.setInt(1, pedido.getIdCabeceraPedidos());
			rs = psCabecera.executeQuery();

			if (!rs.next()) {
				throw new KrakeDevException("El id de la cabecera del pedido no existe");}

			String estadoActual = rs.getString("codigo_estado");
			if (!estadoActual.equals("S")) {
				throw new KrakeDevException("El pedido ya se encuentra Recibido");}
		
			rs.close();
			psCabecera.close();

			// Si existe el id de la cabecera recorro el detalles y valido que exista el id del pedido 
			// y que la cantidad recibida no sea 0 sino existe sino manejo excepcion
			for (int i = 0; i < pedido.getDetalles().size(); i++) {
				DetallePedido detalle = pedido.getDetalles().get(i);

				if (detalle.getIdPedidos() <= 0) {
					throw new KrakeDevException("El id del pedido es invalido");}

				if (detalle.getCantidadRecibida() < 0) {
					throw new KrakeDevException("Verificar el valor de la cantidad recibida");}

				psDetalle = con.prepareStatement(
					"select id_pedidos, codigo_prod " +
					"from detalle_pedidos " +
					"where id_pedidos = ? " +
					"and id_cabecera_pedidos = ?");
				
				psDetalle.setInt(1, detalle.getIdPedidos());
				psDetalle.setInt(2, pedido.getIdCabeceraPedidos());
				rs = psDetalle.executeQuery();

				if (!rs.next()) {
					throw new KrakeDevException("El pedido con Id " + detalle.getIdPedidos() + " no existe o no pertenece al pedido");
				}
				
				// Busco el codigo del producto internamente para extraer el valor del costo y realizar el calculo del subtotal:
				int codigoProducto = rs.getInt("codigo_prod");

				rs.close();
				psDetalle.close();

				// extraigo el valor del costo del producto directamente con el codigo del producto consultado: 
				psCosto = con.prepareStatement(
					"select costo from producto where codigo_prod = ?");
				psCosto.setInt(1, codigoProducto);
				rs = psCosto.executeQuery();

				if (!rs.next()) {
					throw new KrakeDevException("No existe el producto del detalle " + detalle.getIdPedidos());}

				BigDecimal costo = rs.getBigDecimal("costo");
				BigDecimal cantidad = new BigDecimal(detalle.getCantidadRecibida());
				BigDecimal subtotal = costo.multiply(cantidad);

				rs.close();
				psCosto.close();

				// Si todo marcha ok, actulizo las tablas el detalle del pedido y luego el estado de la cabecera:
				// actualiz0 detalle del pedido:
				psActualizarDetalle = con.prepareStatement(
					"update detalle_pedidos " +
					"set cantidad_recibida = ?, subtotal = ? " +
					"where id_pedidos = ?");
				
				psActualizarDetalle.setInt(1, detalle.getCantidadRecibida());
				psActualizarDetalle.setBigDecimal(2, subtotal);
				psActualizarDetalle.setInt(3, detalle.getIdPedidos());

				int filasDetalle = psActualizarDetalle.executeUpdate();

				if (filasDetalle == 0) {
					throw new KrakeDevException("No se pudo actualizar el detalle " + detalle.getIdPedidos());}

				psActualizarDetalle.close();
			}

			// actualizo cabecera
			psActualizarCabecera = con.prepareStatement(
				"update cabecera_pedidos " +
				"set codigo_estado = ? " +
				"where id_cabecera_pedidos = ?");
			
			psActualizarCabecera.setString(1, "R");
			psActualizarCabecera.setInt(2, pedido.getIdCabeceraPedidos());

			int filasCabecera = psActualizarCabecera.executeUpdate();

			if (filasCabecera == 0) {
				throw new KrakeDevException("No se pudo actualizar la cabecera del pedido");}

			con.commit(); // Procedo a guardar si esta todo ok en las tablas involucradas con las transacciones realizadas.

		} catch (Exception e) {
			try {
				if (con != null) {
					con.rollback();} // Si alguna validacion o transaccion falla, no guardar nada de lo ejecutado hasta donde falla. 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (e instanceof KrakeDevException) {
				throw (KrakeDevException)e;
			} else {
				throw new KrakeDevException("Error al recibir pedido: " + e.getMessage());
			}
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (psCabecera != null) try { psCabecera.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (psDetalle != null) try { psDetalle.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (psCosto != null) try { psCosto.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (psActualizarDetalle != null) try { psActualizarDetalle.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (psActualizarCabecera != null) try { psActualizarCabecera.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	
	
	
	
    public void agregarPedidos(Pedidos pedido) throws KrakeDevException {

        if (pedido == null) {
            throw new KrakeDevException("Pedido es null");
        }

        if (pedido.getIdProveedor() == null
                || pedido.getIdProveedor().getIdProveedor() == null
                || pedido.getIdProveedor().getIdProveedor().isEmpty()) {
            throw new KrakeDevException("Identificacion del Proveedor obligartorio");
        }

        if (pedido.getCodigoEstado() == null
                || pedido.getCodigoEstado().getCodigoEstado() == null
                || pedido.getCodigoEstado().getCodigoEstado().isEmpty()) {
            EstadoPedido estadoDefecto = new EstadoPedido();
            estadoDefecto.setCodigoEstado("S");
            pedido.setCodigoEstado(estadoDefecto);
        }

        // fecha por defecto hoy
        if (pedido.getFecha() == null || pedido.getFecha().isEmpty()) {
            pedido.setFecha(LocalDate.now().toString()); // "yyyy-MM-dd"
        }
        
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
        	   throw new KrakeDevException("Detalles del pedido es obligatorio");
        	}

        System.out.println(">>> Buscando el error...");
        
        Connection con = null;
        PreparedStatement psInser = null;
        PreparedStatement psDet = null;
        ResultSet rs = null;
        int idGenerado = 0;

        try {
            con = ConexionBDD.obtenerConexion();

            psInser = con.prepareStatement(
            	    "insert into cabecera_pedidos (codigo_estado, fecha, id_proveedor) " +
            	    "values (?, ?, ?) returning id_cabecera_pedidos");

            	psInser.setString(1, pedido.getCodigoEstado().getCodigoEstado());

            	// Se agregaria la fecha manualmente si se desea, pero con formato (yyyy-mm-dd) / Convertiria pata verlo en json
            	if (pedido.getFecha() == null || pedido.getFecha().isEmpty()) {
            	    pedido.setFecha(LocalDate.now().toString());
            	}
            	psInser.setDate(2, java.sql.Date.valueOf(pedido.getFecha()));
            	psInser.setString(3, pedido.getIdProveedor().getIdProveedor());

            	// Si uso returning por return_generated_keys  en vez de obtner con get, ejecuto consulta y asigno en la columna
            	// correcta usando el nombre de la columna de pgAdmin. Se puede usar Statement.RETURN_GENERATED_KEYS si la columna
            	// seria la primera o que sea determinada en cual se encuentra
            	rs = psInser.executeQuery();

            	if (rs.next()) {
            	    idGenerado = rs.getInt("id_cabecera_pedidos");
            	    pedido.setIdCabeceraPedidos(idGenerado);
            	    System.out.println("Codigo generado: " + idGenerado);
            	}
            	
            	ArrayList<DetallePedido> detallesPedido = pedido.getDetalles();
            	DetallePedido det;
            	
            	for (int i =0;i<detallesPedido.size();i++) {
            		det = detallesPedido.get(i);
            		psDet =con.prepareStatement("insert into detalle_pedidos "
            				+ "(id_cabecera_pedidos, codigo_prod, cantidad, cantidad_recibida, subtotal) "
            				+ "values (?, ?, ?, ?, ?)");
            		psDet.setInt(1, idGenerado); // Aca java me marca error de la variable creada en retorno? 
            		psDet.setInt(2, det.getCodigoProd().getCodigoProd());
            		psDet.setInt(3, det.getCantidad());
            		psDet.setInt(4, det.getCantidadRecibida());
                    // subtotal = Precio unitario del producto * cantidad (solicitada)
            		BigDecimal precioUnitario = det.getCodigoProd().getCosto();
            		BigDecimal cantidad = new BigDecimal(det.getCantidad());
            		BigDecimal subtotal = precioUnitario.multiply(cantidad);
            		psDet.setBigDecimal(5, subtotal);
            		
            		psDet.executeUpdate();
            		
            	}
            	
        } catch (SQLException e) {
            e.printStackTrace();
            throw new KrakeDevException("Error al insertar Pedido " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (psDet != null) try { psDet.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (psInser != null) try { psInser.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public ArrayList<Pedidos> consultarPedidos() throws KrakeDevException {

        ArrayList<Pedidos> pedidos = new ArrayList<Pedidos>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionBDD.obtenerConexion();
            ps = con.prepareStatement(
                    "select id_cabecera_pedidos, codigo_estado, fecha, id_proveedor " +
                    "from cabecera_pedidos " +
                    "order by id_cabecera_pedidos");

            rs = ps.executeQuery();

            while (rs.next()) {
                int idCabeceraPedidos = rs.getInt("id_cabecera_pedidos");
                String codigoEstado = rs.getString("codigo_estado");
                String idProveedor = rs.getString("id_proveedor");

                // pasar date -> string yyyy-MM-dd
                java.sql.Date fechaSql = rs.getDate("fecha");
                String fecha = (fechaSql != null) ? fechaSql.toLocalDate().toString() : null;

                EstadoPedido ep = new EstadoPedido();
                ep.setCodigoEstado(codigoEstado);

                Proveedor pr = new Proveedor();
                pr.setIdProveedor(idProveedor);

                Pedidos pedido = new Pedidos(idCabeceraPedidos, ep, fecha, pr);
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new KrakeDevException("Error al consultar : " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return pedidos;
    }
}