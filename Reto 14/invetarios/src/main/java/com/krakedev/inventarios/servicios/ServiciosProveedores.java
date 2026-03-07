package com.krakedev.inventarios.servicios;


import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.ProveedoresBDD;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.excepciones.KrakeDevException;


@Path("proveedores")

public class ServiciosProveedores {
	
	@Path("buscar/{sub}")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response buscar (@PathParam("sub") String subcadena){
		ProveedoresBDD proBDD= new ProveedoresBDD();
		ArrayList<Proveedor> listaProveedores= null;
		
		try {
			listaProveedores = proBDD.buscar(subcadena);
			return Response.ok(listaProveedores).build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("agregarProveedor")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response agregarProveedor(Proveedor proveedor) {
		ProveedoresBDD proBDD= new ProveedoresBDD();
		try {
			proBDD.agregarProveedor(proveedor);
			return Response.ok().build(); // Marcaria 200 en postman
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build(); // Se realizan modificaciones en este metodo de void a Response. Marcaria 500 en postman.
		}
		
	}
	
	
	
}

