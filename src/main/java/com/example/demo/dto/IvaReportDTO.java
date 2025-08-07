package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class IvaReportDTO {
	private BigDecimal totalIva;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private String mensaje;
	
	public BigDecimal getTotalIva() {
		return totalIva;
	}
	public void setTotalIva(BigDecimal totalIva) {
		this.totalIva = totalIva;
	}
	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public LocalDateTime getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
