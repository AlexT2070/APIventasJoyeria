package com.example.demo.models;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idDetalleVenta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_venta", nullable = false)
	private Venta venta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_producto", nullable = false)
	private Producto producto;
	
	private Integer cantidad;
	private BigDecimal precioUnitatario;
	private BigDecimal ivaPorcentaje;
	private BigDecimal subtotalLinea;
	private BigDecimal ivaMontoLinea;
	private BigDecimal totalLinea;
	
	public Integer getIdDetalleVenta() {
		return idDetalleVenta;
	}
	public void setIdDetalleVenta(Integer idDetalleVenta) {
		this.idDetalleVenta = idDetalleVenta;
	}
	public Venta getVenta() {
		return venta;
	}
	public void setVenta(Venta venta) {
		this.venta = venta;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getPrecioUnitatario() {
		return precioUnitatario;
	}
	public void setPrecioUnitatario(BigDecimal precioUnitatario) {
		this.precioUnitatario = precioUnitatario;
	}
	public BigDecimal getIvaPorcentaje() {
		return ivaPorcentaje;
	}
	public void setIvaPorcentaje(BigDecimal ivaPorcentaje) {
		this.ivaPorcentaje = ivaPorcentaje;
	}
	public BigDecimal getSubtotalLinea() {
		return subtotalLinea;
	}
	public void setSubtotalLinea(BigDecimal subtotalLinea) {
		this.subtotalLinea = subtotalLinea;
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
