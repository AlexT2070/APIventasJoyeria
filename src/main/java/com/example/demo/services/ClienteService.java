package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ClienteRequestDTO;
import com.example.demo.dto.ClienteResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Venta;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.repositories.VentaRepository;

@Service
public class ClienteService {
	private final ClienteRepository clienteRepository;
	
	
	public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}
	
	public ClienteResponseDTO updatePago(Integer idCliente, ClienteRequestDTO clienteRequestDTO) {
		Optional<Cliente> clienteOp = clienteRepository.findById(idCliente);
		if(clienteOp.isEmpty()) {
			throw new RuntimeException("Cliente no encontrado con ID: "+ idCliente);
		}
		
		
		Cliente clienteExistente = clienteOp.get();
		clienteExistente.setNombreCliente(clienteRequestDTO.getNombreCliente());
		clienteExistente.setApellidos(clienteRequestDTO.getApellidos());
		clienteExistente.setCorreo(clienteRequestDTO.getCorreo());
		clienteExistente.setTelefono(clienteRequestDTO.getTelefono());
		clienteExistente.setDireccion(clienteRequestDTO.getDireccion());
		
		Cliente clienteActualizado = clienteRepository.save(clienteExistente);
		
		return convertToClienteResponseDTO(clienteActualizado);
	}
	
	public ClienteResponseDTO createCliente(ClienteRequestDTO clienteRequestDTO) {
		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(clienteRequestDTO.getIdCliente());
		cliente.setNombreCliente(clienteRequestDTO.getNombreCliente());
		cliente.setApellidos(clienteRequestDTO.getApellidos());
		cliente.setCorreo(clienteRequestDTO.getCorreo());
		cliente.setTelefono(clienteRequestDTO.getTelefono());
		cliente.setDireccion(clienteRequestDTO.getDireccion());
		
		Cliente clienteGuardado = clienteRepository.save(cliente);
		return convertToClienteResponseDTO(clienteGuardado);
	}
	
	public ClienteResponseDTO readClientePorId(Integer idCliente) {
		Optional<Cliente> clienteOp = clienteRepository.findById(idCliente);
		if(clienteOp.isEmpty()) {
			throw new RuntimeException("Cliente no eoncontrado con ID: "+ idCliente);
		}
		return convertToClienteResponseDTO(clienteOp.get());
	}
	
	public List<ClienteResponseDTO> readAllCliente() {
		List<Cliente> clientes = clienteRepository.findAll();
		return clientes.stream()
				.map(this:: convertToClienteResponseDTO)
				.collect(Collectors.toList());
	}
	
	

	private ClienteResponseDTO convertToClienteResponseDTO(Cliente cliente) {
		ClienteResponseDTO dto = new ClienteResponseDTO();
		dto.setIdCliente(cliente.getIdCliente());
		dto.setNombreCliente(cliente.getNombreCliente());
		dto.setApellidos(cliente.getApellidos());
		dto.setCorreo(cliente.getCorreo());
		dto.setTelefono(cliente.getTelefono());
		dto.setDireccion(cliente.getDireccion());
		return dto;
	}
}
