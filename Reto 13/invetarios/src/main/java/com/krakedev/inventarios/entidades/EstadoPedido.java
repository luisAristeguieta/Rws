package com.krakedev.inventarios.entidades;

public class EstadoPedido {
	private String codigoEstado;
	private String descripcion;
	
	
	public EstadoPedido() {}
	
	public EstadoPedido(String codigoEstado, String descripcion) {
		super();
		this.codigoEstado = codigoEstado;
		this.descripcion = descripcion;
	}
	
	
	public String getCodigoEstado() {
		return codigoEstado;
	}
	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "EstadoPedido [codigoEstado=" + codigoEstado + ", descripcion=" + descripcion + "]";
	}
	
}
