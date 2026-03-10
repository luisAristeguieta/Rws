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

import com.krakedev.inventarios.bdd.ProductoBDD;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("productos")

public class ServiciosProductos {

	@Path("buscar/{sub}")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response buscar (@PathParam("sub") String subcadena){
		ProductoBDD proBDD= new ProductoBDD();
		ArrayList<Producto> listaProductos= null;
		
		try {
			listaProductos = proBDD.buscar(subcadena);
			return Response.ok(listaProductos).build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("agregarProducto")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response agregarProducto(Producto producto) {
		ProductoBDD proBDD= new ProductoBDD();
		try {
			proBDD.agregarProducto(producto);
			return Response.ok().build(); // Marcaria 200 en postman
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build(); // Se realizan modificaciones en este metodo de void a Response. Marcaria 500 en postman.
		}
		
	}
	
	
}
