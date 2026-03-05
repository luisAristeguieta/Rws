package com.krakedev.inventarios.entidades;

public class UnidadDeMedidas {
	private String codigoUdm;
	private CategoriaUnidad codigoUnidad;
	private String descripcion;
	
	public UnidadDeMedidas() {}
	
	public UnidadDeMedidas(String codigoUdm, CategoriaUnidad codigoUnidad, String descripcion) {
		super();
		this.codigoUdm = codigoUdm;
		this.codigoUnidad = codigoUnidad;
		this.descripcion = descripcion;
	}
	
	public String getCodigoUdm() {
		return codigoUdm;
	}
	public void setCodigoUdm(String codigoUdm) {
		this.codigoUdm = codigoUdm;
	}
	public CategoriaUnidad getCodigoUnidad() {
		return codigoUnidad;
	}
	public void setCodigoUnidad(CategoriaUnidad codigoUnidad) {
		this.codigoUnidad = codigoUnidad;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "UnidadDeMedidas [codigoUdm=" + codigoUdm + ", codigoUnidad=" + codigoUnidad + ", descripcion="
				+ descripcion + "]";
	}
	
}
