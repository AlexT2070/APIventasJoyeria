package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public class VentaResponseDTO {
	
	private Integer idVenta;
	private String nombreCliente;
	private LocalDateTime fecha;
	private BigDecimal totalVenta;
	private BigDecimal totalIva;
	private String estado;
	private String metodoPago;
	private List<DetalleVentaResponseDTO> detalles;
	
	
	public List<DetalleVentaResponseDTO> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<DetalleVentaResponseDTO> detalles) {
		this.detalles = detalles;
	}
	public Integer getIdVenta() {
		return idVenta;
	}
	public void setIdVenta(Integer idVenta) {
		this.idVenta = idVenta;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String string) {
		this.nombreCliente = string;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	public BigDecimal getTotalVenta() {
		return totalVenta;
	}
	public void setTotalVenta(BigDecimal totalVenta) {
		this.totalVenta = totalVenta;
	}
	public BigDecimal getTotalIva() {
		return totalIva;
	}
	public void setTotalIva(BigDecimal totalIva) {
		this.totalIva = totalIva;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String string) {
		this.estado = string;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(String string) {
		this.metodoPago = string;
	}
	
	
	
	
	public static class DetalleVentaResponseDTO{
		private String nombreProducto;
		private Integer cantidad;
		private BigDecimal precioUnitario;
		private BigDecimal ivaPorcentaje;
		private BigDecimal ivaMontoLinea;
		private BigDecimal totalLinea;
		
		public String getNombreProducto() {
			return nombreProducto;
		}
		public void setNombreProducto(String nombreProducto) {
			this.nombreProducto = nombreProducto;
		}
		public Integer getCantidad() {
			return cantidad;
		}
		public void setCantidad(Integer cantidad) {
			this.cantidad = cantidad;
		}
		public BigDecimal getPrecioUnitario() {
			return precioUnitario;
		}
		public void setPrecioUnitario(BigDecimal precioUnitario) {
			this.precioUnitario = precioUnitario;
		}
		public BigDecimal getIvaPorcentaje() {
			return ivaPorcentaje;
		}
		public void setIvaPorcentaje(BigDecimal ivaPorcentaje) {
			this.ivaPorcentaje = ivaPorcentaje;
		}
		public BigDecimal getIvaMontoLinea() {
			return ivaMontoLinea;
		}
		public void setIvaMontoLinea(BigDecimal ivaMontoLinea) {
			this.ivaMontoLinea = ivaMontoLinea;
		}
		public BigDecimal getTotalLinea() {
			return totalLinea;
		}
		public void setTotalLinea(BigDecimal totalLinea) {
			this.totalLinea = totalLinea;
		}
		
	}
	
}
