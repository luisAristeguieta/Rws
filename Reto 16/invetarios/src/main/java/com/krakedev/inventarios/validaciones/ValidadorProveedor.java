package com.krakedev.inventarios.validaciones;

import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.excepciones.KrakeDevException;

public class ValidadorProveedor {

	public static void validarDatosProveedor(Proveedor proveedor) throws KrakeDevException {

		if (proveedor == null) {
			throw new KrakeDevException("Debe enviar el proveedor");
		}

		if (proveedor.getIdProveedor() == null || proveedor.getIdProveedor().trim().isEmpty()) {
			throw new KrakeDevException("Debe enviar la identificacion del proveedor");
		}

		if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
			throw new KrakeDevException("Debe enviar el nombre del proveedor");
		}

		if (proveedor.getTelefono() == null || proveedor.getTelefono().trim().isEmpty()) {
			throw new KrakeDevException("Debe enviar el telefono del proveedor");
		}

		if (proveedor.getCorreo() == null || proveedor.getCorreo().trim().isEmpty()) {
			throw new KrakeDevException("Debe enviar el correo del proveedor");
		}

		if (proveedor.getDireccion() == null || proveedor.getDireccion().trim().isEmpty()) {
			throw new KrakeDevException("Debe enviar la direccion del proveedor");
		}
	}
}
