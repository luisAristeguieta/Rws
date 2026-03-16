package com.krakedev.inventarios.entidades;

import java.math.BigDecimal;

public class DetallePedido {
	private int idPedidos;
	private Pedidos idCabeceraPedidos;
	private Producto codigoProd;
	private int cantidad;
	private BigDecimal subtotal;
	private int cantidadRecibida;
	
	public DetallePedido() {
	}
	
	public DetallePedido(int idPedidos, Pedidos idCabeceraPedidos, Producto codigoProd, int cantidad,
			BigDecimal subtotal, int cantidadRecibida) {
		super();
		this.idPedidos = idPedidos;
		this.idCabeceraPedidos = idCabeceraPedidos;
		this.codigoProd = codigoProd;
		this.cantidad = cantidad;
		this.subtotal = subtotal;
		this.cantidadRecibida = cantidadRecibida;
	}
	public int getIdPedidos() {
		return idPedidos;
	}
	public void setIdPedidos(int idPedidos) {
		this.idPedidos = idPedidos;
	}
	public Pedidos getIdCabeceraPedidos() {
		return idCabeceraPedidos;
	}
	public void setIdCabeceraPedidos(Pedidos idCabeceraPedidos) {
		this.idCabeceraPedidos = idCabeceraPedidos;
	}
	public Producto getCodigoProd() {
		return codigoProd;
	}
	public void setCodigoProd(Producto codigoProd) {
		this.codigoProd = codigoProd;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public int getCantidadRecibida() {
		return cantidadRecibida;
	}
	public void setCantidadRecibida(int cantidadRecibida) {
		this.cantidadRecibida = cantidadRecibida;
	}
	
	@Override
	public String toString() {
		return "DetallePedido [idPedidos=" + idPedidos + ", idCabeceraPedidos=" + idCabeceraPedidos + ", codigoProd="
				+ codigoProd + ", cantidad=" + cantidad + ", subtotal=" + subtotal + ", cantidadRecibida="
				+ cantidadRecibida + "]";
	}
	
}
