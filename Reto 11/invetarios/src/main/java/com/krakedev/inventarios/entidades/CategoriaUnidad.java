package com.krakedev.inventarios.entidades;

public class CategoriaUnidad {
	private String codigoUnidad;
	private String nombre;
	
	public CategoriaUnidad() {}
	
	public CategoriaUnidad(String codigoUnidad, String nombre) {
		super();
		this.codigoUnidad = codigoUnidad;
		this.nombre = nombre;
	}
	
	public String getCodigoUnidad() {
		return codigoUnidad;
	}
	public void setCodigoUnidad(String codigoUnidad) {
		this.codigoUnidad = codigoUnidad;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "CategoriaUnidad [codigoUnidad=" + codigoUnidad + ", nombre=" + nombre + "]";
	}	
	
}
