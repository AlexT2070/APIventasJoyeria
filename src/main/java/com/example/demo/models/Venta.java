package com.example.demo.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence. *;

@Entity
@Table(name = "venta")
public class Venta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idVenta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente", nullable = false)
	private Cliente cliente;

	private LocalDateTime fecha;
	@Column(name= "total_venta", nullable = false)
	private BigDecimal totalVenta;
	
	@Column(name = "total_iva", nullable = false)
	private BigDecimal totalIva;
	
	
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	public enum Estado{
		pagado, pendiente
	}
	
	
	@Enumerated(EnumType.STRING)
	private Metodo_pago metodo_pago;
	
	public enum Metodo_pago{
		efectivo, tarjeta
	}
	
	@OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<DetalleVenta> detalles = new ArrayList<>();
	
	public void addDetalle(DetalleVenta detalle) {
		detalles.add(detalle);
		detalle.setVenta(this);
	}
	
	public void removeDetalle(DetalleVenta detalle) {
		detalles.remove(detalle);
		detalle.setVenta(null);
	}
	
	
	public Integer getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Integer idVenta) {
		this.idVenta = idVenta;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Metodo_pago getMetodo_pago() {
		return metodo_pago;
	}

	public void setMetodo_pago(Metodo_pago metodo_pago) {
		this.metodo_pago = metodo_pago;
	}

	public List<DetalleVenta> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleVenta> detalles) {
		this.detalles = detalles;
	}

}
