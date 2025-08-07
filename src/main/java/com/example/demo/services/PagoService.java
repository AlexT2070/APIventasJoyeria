package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PagoResponseDTO;
import com.example.demo.models.Pago;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.repositories.PagoRepository;

@Service
public class PagoService {
	private final PagoRepository pagoRepository;
	
	
	@Autowired
	public PagoService(PagoRepository pagoRepository, ClienteRepository clienteRepository) {
		this.pagoRepository = pagoRepository;
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
		dto.setMetodoPago(pago.getMetodoPago().name());
		dto.setFecha(pago.getFecha());
		return dto;
	}
}
