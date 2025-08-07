package com.example.demo.dto;

import java.time.LocalDateTime;

public class PagoResponseDTO {
	Integer idPago;
	Integer idventa;
	String nombreCliente;
	String metodoPago;
	LocalDateTime fecha;
	
	
	public Integer getIdPago() {
		return idPago;
	}
	public void setIdPago(Integer idPago) {
		this.idPago = idPago;
	}
	public Integer getIdventa() {
		return idventa;
	}
	public void setIdventa(Integer idventa) {
		this.idventa = idventa;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(String string) {
		this.metodoPago = string;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
}
