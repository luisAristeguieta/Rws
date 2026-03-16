package com.krakedev.inventarios.validaciones;

import java.math.BigDecimal;

import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.excepciones.KrakeDevException;

public class ValidadorProducto {

	public static void validarDatosProducto(Producto producto) throws KrakeDevException {

		if (producto == null) {
			throw new KrakeDevException("El producto es obligatorio");
		}

		if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
			throw new KrakeDevException("El nombre del producto es obligatorio");
		}

		if (producto.getCodigoUdm() == null || producto.getCodigoUdm().getCodigoUdm() == null
				|| producto.getCodigoUdm().getCodigoUdm().trim().isEmpty()) {
			throw new KrakeDevException("El codigo de la unidad de medida es obligatorio");
		}

		if (producto.getPrecioVenta() == null) {
			throw new KrakeDevException("El precio de venta es obligatorio");
		}

		if (producto.getPrecioVenta().compareTo(BigDecimal.ZERO) < 0) {
			throw new KrakeDevException("El precio de venta no puede ser negativo");
		}

		if (producto.getCosto() == null) {
			throw new KrakeDevException("El valor del costo es obligatorio");
		}

		if (producto.getCosto().compareTo(BigDecimal.ZERO) < 0) {
			throw new KrakeDevException("El valor del costo no puede ser negativo");
		}

		if (producto.getCodigoCategoria() == null) {
			throw new KrakeDevException("La categoria del producto es obligatorio");
		}

		if (producto.getCodigoCategoria().getCodigoCategoria() <= 0) {
			throw new KrakeDevException("Debe enviar el codigo de la categoria del producto");
		}

		if (producto.getStockProducto() < 0) {
			throw new KrakeDevException("El stock no puede ser negativo");
		}
	}
}