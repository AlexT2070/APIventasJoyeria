package com.example.demo.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PagoRequestDTO;
import com.example.demo.dto.PagoResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Pago;
import com.example.demo.models.Venta;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.repositories.PagoRepository;
import com.example.demo.repositories.VentaRepository;

@Service
public class PagoService {
	private final PagoRepository pagoRepository;
	private final VentaRepository ventaRepository;
	private final ClienteRepository clienteRepository;
	
	
	@Autowired
	public PagoService(PagoRepository pagoRepository, ClienteRepository clienteRepository, VentaRepository ventaRepository) {
		this.pagoRepository = pagoRepository;
		this.clienteRepository = clienteRepository;
		this.ventaRepository = ventaRepository;
	}
	
	@Transactional
	public Pago RegisterPago(PagoRequestDTO pagoRequestDTO) {
		Venta venta = ventaRepository.findById(pagoRequestDTO.getIdVenta())
				.orElseThrow(() -> new RuntimeException("Venta no encontrada"));
		
		if(venta.getTotalVenta() == null) {
			venta.setTotalPagado(BigDecimal.ZERO);
		}
		if(venta.getTotalPagado() == null) {
			venta.setTotalPagado(BigDecimal.ZERO);
		}
		
		Cliente cliente = clienteRepository.findById(pagoRequestDTO.getIdCliente())
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
		
		if(venta.getEstado() == Venta.Estado.pagado) {
			throw new RuntimeException("La venta ya esta completamente pagada");
		}
		
		BigDecimal saldoPendiente = venta.getTotalVenta().subtract(
				venta.getTotalPagado() != null ? venta.getTotalPagado() : BigDecimal.ZERO
				);
		
		
		if(pagoRequestDTO.getMonto().compareTo(saldoPendiente) > 0) {
			throw new RuntimeException("El monto de pago excede el saldo pendiente" + saldoPendiente);
		}
		
		Pago pago = new Pago();
		pago.setVenta(venta);
		pago.setCliente(cliente);
		pago.setMonto(pagoRequestDTO.getMonto());
	    pago.setMetodoPago(Pago.metodoPago.valueOf(pagoRequestDTO.getMetodoPago()));
	    pago.setFecha(pagoRequestDTO.getFecha() != null ? pagoRequestDTO.getFecha() : LocalDateTime.now());
	    
	    BigDecimal nuevoTotalPagado = venta.getTotalPagado() != null ?
	    		venta.getTotalPagado().add(pagoRequestDTO.getMonto()):
	    		pagoRequestDTO.getMonto();
	    venta.setTotalPagado(nuevoTotalPagado);
	    
	    
	    if(venta.isCompletamentePagado()) {
	    	venta.setEstado(Venta.Estado.pagado);
	    }else {
	    	venta.setEstado(Venta.Estado.parcial);
	    }
	    ventaRepository.save(venta);
	    return pagoRepository.save(pago);
	}
	
	
	
	
	
	
	@Transactional(readOnly = true)
	public List<PagoResponseDTO> getPagosporCliente(Integer idCliente){
		List<Pago> pagos = pagoRepository.findByClienteIdCliente(idCliente);
		return pagos.stream()
				.map(this::convertToPagoResponseDTO)
				.collect(Collectors.toList());
	}
	
	private PagoResponseDTO convertToPagoResponseDTO(Pago pago) {
		PagoResponseDTO dto = new PagoResponseDTO();
		dto.setIdPago(pago.getIdPago());
		dto.setNombreCliente(pago.getCliente().getNombreCliente()+ " "+ pago.getCliente().getApellidos());
		dto.setMonto(pago.getMonto());
		dto.setMetodoPago(pago.getMetodoPago().name());
		dto.setFecha(pago.getFecha());
		return dto;
	}
}
