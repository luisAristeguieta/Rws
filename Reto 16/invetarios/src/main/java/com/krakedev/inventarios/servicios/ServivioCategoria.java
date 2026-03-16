package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.CategoriaBDD;
import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("categoria")

public class ServivioCategoria {

	@Path("agregar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response agregarCategoria(Categoria categoria) {
		CategoriaBDD catBDD = new CategoriaBDD();
		try {
			catBDD.agregarCategoria(categoria);
			return Response.ok("Categoria creada correctamente").build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@Path("actualizar")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response actualizarCategoria(Categoria categoria) {
		CategoriaBDD catBDD = new CategoriaBDD();
		try {
			catBDD.actualizarCategoria(categoria);
			return Response.ok("Categoria actualizada correctamente").build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("recuperar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarCategoria() {
		CategoriaBDD catBDD = new CategoriaBDD();
		try {
			ArrayList<Categoria> lista = catBDD.consultarCategoria();
			return Response.ok(lista).build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
}