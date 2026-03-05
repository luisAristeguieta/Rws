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