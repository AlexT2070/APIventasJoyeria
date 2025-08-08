package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoResponseDTO {
	Integer idPago;
	Integer idventa;
	String nombreCliente;
	BigDecimal monto;
	String metodoPago;
	LocalDateTime fecha;
	
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	
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
