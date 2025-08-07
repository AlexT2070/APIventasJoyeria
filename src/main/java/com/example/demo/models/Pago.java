package com.example.demo.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;


@Entity
@Table(name = "pago")
public class Pago {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPago;
	
	@ManyToOne
	@JoinColumn(name = "id_venta")
	private Venta venta;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;
	
	private BigDecimal monto;
	
	@Enumerated(EnumType.STRING)
	private metodoPago metodoPago;
	
	public enum metodoPago {
		efectivo, tarjeta
	}
	
	public metodoPago getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(metodoPago metodoPago) {
		this.metodoPago = metodoPago;
	}

	private LocalDateTime fecha;
	
	public Integer getIdPago() {
		return idPago;
	}

	public void setIdPago(Integer idPago) {
		this.idPago = idPago;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

}
