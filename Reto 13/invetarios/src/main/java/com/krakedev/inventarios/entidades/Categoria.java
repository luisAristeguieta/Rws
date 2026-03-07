package com.krakedev.inventarios.entidades;

public class Categoria {
	private int codigoCategoria;
	private String nombre;
	private Categoria categoriaPadre;
	
	public Categoria() {}
	
	public Categoria(int codigoCategoria, String nombre, Categoria categoriaPadre) {
		super();
		this.codigoCategoria = codigoCategoria;
		this.nombre = nombre;
		this.categoriaPadre = categoriaPadre;
	}
	
	public int getCodigoCategoria() {
		return codigoCategoria;
	}
	public void setCodigoCategoria(int codigoCategoria) {
		this.codigoCategoria = codigoCategoria;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Categoria getCategoriaPadre() {
		return categoriaPadre;
	}
	public void setCategoriaPadre(Categoria categoriaPadre) {
		this.categoriaPadre = categoriaPadre;
	}

	@Override
	public String toString() {
		return "Categoria [codigoCategoria=" + codigoCategoria + ", nombre=" + nombre + ", categoriaPadre="
				+ categoriaPadre + "]";
	}
}
