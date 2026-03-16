package com.krakedev.inventarios.entidades;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Ventas {
	 private int idCabeceraVentas;
	 private String fecha;
	 private BigDecimal subtotal;
	 private BigDecimal iva;
	 private BigDecimal total;
	 
	 private ArrayList <DetalleVenta> detalles;
	 
	 public Ventas() {}

	 public Ventas(int idCabeceraVentas, String fecha, BigDecimal subtotal, BigDecimal iva, BigDecimal total) {
		super();
		this.idCabeceraVentas = idCabeceraVentas;
		this.fecha = fecha;
		this.subtotal = subtotal;
		this.iva = iva;
		this.total = total;
	}
	 
	 public int getIdCabeceraVentas() {
		 return idCabeceraVentas;
	 }
	 public void setIdCabeceraVentas(int idCabeceraVentas) {
		 this.idCabeceraVentas = idCabeceraVentas;
	 }
	 public String getFecha() {
		 return fecha;
	 }
	 public void setFecha(String fecha) {
		 this.fecha = fecha;
	 }
	 public BigDecimal getSubtotal() {
		 return subtotal;
	 }
	 public void setSubtotal(BigDecimal subtotal) {
		 this.subtotal = subtotal;
	 }
	 public BigDecimal getIva() {
		 return iva;
	 }
	 public void setIva(BigDecimal iva) {
		 this.iva = iva;
	 }
	 public BigDecimal getTotal() {
		 return total;
	 }
	 public void setTotal(BigDecimal total) {
		 this.total = total;
	 }

	 public ArrayList<DetalleVenta> getDetalles() {
		return detalles;
	}

	 public void setDetalles(ArrayList<DetalleVenta> detalles) {
		 this.detalles = detalles;
	 }

	 @Override
	 public String toString() {
		return "Ventas [idCabeceraVentas=" + idCabeceraVentas + ", fecha=" + fecha + ", subtotal=" + subtotal + ", iva="
				+ iva + ", total=" + total + "]";
	 }
	    	
}
