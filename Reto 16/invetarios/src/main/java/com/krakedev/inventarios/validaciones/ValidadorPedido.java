package com.krakedev.inventarios.validaciones;

import com.krakedev.inventarios.entidades.Pedidos;
import com.krakedev.inventarios.excepciones.KrakeDevException;

public class ValidadorPedido {

	public static void validarDatosPedido(Pedidos pedido) throws KrakeDevException {

		if (pedido == null) {
			throw new KrakeDevException("Debe enviar el pedido");
		}

		if (pedido.getCodigoEstado() == null) {
			throw new KrakeDevException("Debe enviar el estado del pedido");
		}

		if (pedido.getCodigoEstado().getCodigoEstado() == null
				|| pedido.getCodigoEstado().getCodigoEstado().trim().isEmpty()) {
			throw new KrakeDevException("Debe enviar el codigo del estado del pedido");
		}

		if (pedido.getIdProveedor() == null) {
			throw new KrakeDevException("Debe enviar el proveedor");
		}

		if (pedido.getIdProveedor().getIdProveedor() == null
				|| pedido.getIdProveedor().getIdProveedor().trim().isEmpty()) {
			throw new KrakeDevException("Debe enviar la identificacion del proveedor");
		}
	}
}