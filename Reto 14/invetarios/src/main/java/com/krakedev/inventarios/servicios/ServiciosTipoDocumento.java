package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.TipoDocumentoBDD;
import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path ("tiposDocumentos")
public class ServiciosTipoDocumento {

	@Path("mostrarDocumentos")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response mostrarDocumentos (){
		TipoDocumentoBDD tipoDocBDD= new TipoDocumentoBDD();
		ArrayList<TipoDocumento> listaDocumento= null;
		
		try {
			listaDocumento = tipoDocBDD.mostrarDocumento();
			return Response.ok(listaDocumento).build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	

}
