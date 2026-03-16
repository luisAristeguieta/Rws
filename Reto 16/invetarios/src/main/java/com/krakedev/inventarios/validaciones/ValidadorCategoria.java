package com.krakedev.inventarios.validaciones;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.excepciones.KrakeDevException;

public class ValidadorCategoria {

	public static void validarDatosCategoria(Categoria categoria) throws KrakeDevException {

		if (categoria == null) {
			throw new KrakeDevException("La categoria es obligatorio");
		}
		
		if (categoria.getCodigoCategoria() <= 0) {
			throw new KrakeDevException("El codigo de la categoria debe ser mayor a 0");
		}
		
		if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
			throw new KrakeDevException("El nombre de la categoria es obligatorio");
		}
		
		if (categoria.getCategoriaPadre() != null) {
			if (categoria.getCategoriaPadre().getCodigoCategoria() <= 0) {
				throw new KrakeDevException("El codigo de la categoria padre debe ser mayor a 0");
			}
			if (categoria.getCategoriaPadre().getCodigoCategoria() == categoria.getCodigoCategoria()) {
				throw new KrakeDevException("Una categoria no puede ser padre de si misma");
			}
		}
	}
}