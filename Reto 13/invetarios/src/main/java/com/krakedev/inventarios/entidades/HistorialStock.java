package com.krakedev.inventarios.entidades;

import java.time.LocalDateTime;

public class HistorialStock {
	private int codigoStockSerial;
	private LocalDateTime fecha;
	private String referencia;
	private Producto producto;
	private int cantidadStock;
	
	public HistorialStock() {}

	public HistorialStock(int codigoStockSerial, LocalDateTime fecha, String referencia, Producto producto, int cantidadStock) {
		super();
		this.codigoStockSerial = codigoStockSerial;
		this.fecha = fecha;
		this.referencia = referencia;
		this.producto = producto;
		this.cantidadStock = cantidadStock;
	}

	public int getCodigoStockSerial() {
		return codigoStockSerial;
	}

	public void setCodigoStockSerial(int codigoStockSerial) {
		this.codigoStockSerial = codigoStockSerial;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidadStock() {
		return cantidadStock;
	}

	public void setCantidadStock(int cantidadStock) {
		this.cantidadStock = cantidadStock;
	}

	@Override
	public String toString() {
		return "HistorialStock [codigoStockSerial=" + codigoStockSerial + ", fecha=" + fecha + ", referencia="
				+ referencia + ", producto=" + producto + ", cantidadStock=" + cantidadStock + "]";
	}
	
	
	
}