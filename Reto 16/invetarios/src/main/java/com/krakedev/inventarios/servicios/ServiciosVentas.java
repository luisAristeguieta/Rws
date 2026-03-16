package com.krakedev.inventarios.servicios;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.VentasBDD;
import com.krakedev.inventarios.entidades.Ventas;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("ventas")

public class ServiciosVentas {

	@Path("agregarVantas")
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response agregarVantas(Ventas venta) {
        VentasBDD ventasBDD = new VentasBDD();

        try {
        	ventasBDD.agregarVantas(venta);
            return Response.ok("OK").build();
        } catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
    }	
}
