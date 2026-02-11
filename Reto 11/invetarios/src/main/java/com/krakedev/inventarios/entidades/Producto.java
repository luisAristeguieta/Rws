package com.krakedev.inventarios.entidades;

import java.math.BigDecimal;

public class Producto {
	private int codigoProd;
	private String nombre;
	private UnidadDeMedidas codigoUdm;
	private BigDecimal precioVenta;
	private boolean iva;
	private BigDecimal costo;
	private Categoria codigoCategoria;
	private int stockProducto;
	
	public Producto() {	}

	public Producto(int codigoProd, String nombre, UnidadDeMedidas codigoUdm, BigDecimal precioVenta, boolean iva,
			BigDecimal costo, Categoria codigoCategoria, int stockProducto) {
		super();
		this.codigoProd = codigoProd;
		this.nombre = nombre;
		this.codigoUdm = codigoUdm;
		this.precioVenta = precioVenta;
		this.iva = iva;
		this.costo = costo;
		this.codigoCategoria = codigoCategoria;
		this.stockProducto = stockProducto;
	}

	public int getCodigoProd() {
		return codigoProd;
	}

	public void setCodigoProd(int codigoProd) {
		this.codigoProd = codigoProd;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public UnidadDeMedidas getCodigoUdm() {
		return codigoUdm;
	}

	public void setCodigoUdm(UnidadDeMedidas codigoUdm) {
		this.codigoUdm = codigoUdm;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public boolean isIva() {
		return iva;
	}

	public void setIva(boolean iva) {
		this.iva = iva;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public Categoria getCodigoCategoria() {
		return codigoCategoria;
	}

	public void setCodigoCategoria(Categoria codigoCategoria) {
		this.codigoCategoria = codigoCategoria;
	}

	public int getStockProducto() {
		return stockProducto;
	}

	public void setStockProducto(int stockProducto) {
		this.stockProducto = stockProducto;
	}

	@Override
	public String toString() {
		return "Producto [codigoProd=" + codigoProd + ", nombre=" + nombre + ", codigoUdm=" + codigoUdm
				+ ", precioVenta=" + precioVenta + ", iva=" + iva + ", costo=" + costo + ", codigoCategoria="
				+ codigoCategoria + ", stockProducto=" + stockProducto + "]";
	}

	
	
}
