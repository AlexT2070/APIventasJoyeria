package com.example.demo.dto;

public class DetalleVentaRequestDTO {
	private Integer IdProducto;
	private Integer cantidad;
	
	public Integer getIdProducto() {
		return IdProducto;
	}
	public void setIdProducto(Integer idProducto) {
		IdProducto = idProducto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}