package com.krakedev.inventarios.entidades;

import java.math.BigDecimal;

public class DetalleVenta {
	private int idDetalleVenta;
	private Ventas idCabeceraVentas;
	private Producto codigoProd;
	private int cantidad;
	private Producto costo;
	private BigDecimal subtotal;
	private BigDecimal total;
	
	
	public DetalleVenta() {}

	public DetalleVenta(int idDetalleVenta, Ventas idCabeceraVentas, Producto codigoProd, int cantidad, Producto costo,
			BigDecimal subtotal, BigDecimal total) {
		super();
		this.idDetalleVenta = idDetalleVenta;
		this.idCabeceraVentas = idCabeceraVentas;
		this.codigoProd = codigoProd;
		this.cantidad = cantidad;
		this.costo = costo;
		this.subtotal = subtotal;
		this.total = total;
	}
	
	public int getIdDetalleVenta() {
		return idDetalleVenta;
	}
	public void setIdDetalleVenta(int idDetalleVenta) {
		this.idDetalleVenta = idDetalleVenta;
	}
	public Ventas getIdCabeceraVentas() {
		return idCabeceraVentas;
	}
	public void setIdCabeceraVentas(Ventas idCabeceraVentas) {
		this.idCabeceraVentas = idCabeceraVentas;
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
	public Producto getCosto() {
		return costo;
	}
	public void setCosto(Producto costo) {
		this.costo = costo;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	} 
	
	@Override
	public String toString() {
		return "DetalleVenta [idDetalleVenta=" + idDetalleVenta + ", idCabeceraVentas=" + idCabeceraVentas
				+ ", codigoProd=" + codigoProd + ", cantidad=" + cantidad + ", costo=" + costo + ", subtotal="
				+ subtotal + ", total=" + total + "]";
	}

}
