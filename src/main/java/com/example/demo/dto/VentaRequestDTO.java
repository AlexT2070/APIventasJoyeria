package com.example.demo.dto;

import java.util.List;

public class VentaRequestDTO {
	private Integer idCliente;
	private List<DetalleVentaRequestDTO> detalles;
	private String metodoPago;
	private String estado;
	
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public List<DetalleVentaRequestDTO> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<DetalleVentaRequestDTO> detalles) {
		this.detalles = detalles;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
}
