package com.krakedev.inventarios.entidades;

import java.util.ArrayList;

public class Pedidos {
    private int idCabeceraPedidos;
    private EstadoPedido codigoEstado;
    private String fecha;
    private Proveedor idProveedor;

    private ArrayList <DetallePedido> detalles;
    	
    public Pedidos() {}

    public Pedidos(int idCabeceraPedidos, EstadoPedido codigoEstado, String fecha, Proveedor idProveedor) {
        this.idCabeceraPedidos = idCabeceraPedidos;
        this.codigoEstado = codigoEstado;
        this.fecha = fecha;
        this.idProveedor = idProveedor;
    }

    public int getIdCabeceraPedidos() {
        return idCabeceraPedidos;
    }
    public void setIdCabeceraPedidos(int idCabeceraPedidos) {
        this.idCabeceraPedidos = idCabeceraPedidos;
    }

    public EstadoPedido getCodigoEstado() {
        return codigoEstado;
    }
    public void setCodigoEstado(EstadoPedido codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }
    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
    }

    
    public ArrayList<DetallePedido> getDetalles() {
		return detalles;
	}

	public void setDetalles(ArrayList<DetallePedido> detalles) {
		this.detalles = detalles;
	}

	@Override
    public String toString() {
        return "Pedidos [idCabeceraPedidos=" + idCabeceraPedidos + ", codigoEstado=" + codigoEstado + ", fecha=" + fecha
                + ", idProveedor=" + idProveedor + "]";
    }
}