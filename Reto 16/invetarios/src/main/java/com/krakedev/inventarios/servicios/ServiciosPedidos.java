package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.PedidosBDD;
import com.krakedev.inventarios.bdd.ProveedoresBDD;
import com.krakedev.inventarios.entidades.Pedidos;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("pedidos")
public class ServiciosPedidos {

	@PUT
	@Path("recibir")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response recibirPedido(Pedidos pedido) {
		try {
			PedidosBDD ped = new PedidosBDD();
			ped.recibirPedido(pedido);
			return Response.ok("OK").build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	
    @Path("registrarPedidos")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response registrarPedidos(Pedidos pedido) {
        PedidosBDD pedidoBDD = new PedidosBDD();

        try {
            pedidoBDD.agregarPedidos(pedido);
            return Response.ok("OK").build();
        } catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
    }

    @GET
    @Path("consultar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarPedidos() {
        PedidosBDD bdd = new PedidosBDD();
        try {
            ArrayList<Pedidos> lista = bdd.consultarPedidos();
            return Response.ok(lista).build();
        } catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
    }
    
    @Path("proveedor/{idProveedor}")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response buscar (@PathParam("idProveedor") String subcadena){
    	PedidosBDD pedidosbdd = new PedidosBDD();
		ArrayList<Pedidos> listaPedidos= null;
		
		try {
			listaPedidos = pedidosbdd.consultarPedidoProveedor(subcadena);
			return Response.ok(listaPedidos).build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build(); // Mostrara donde falla en postman
		}
	}
    
}