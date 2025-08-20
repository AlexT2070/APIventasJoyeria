package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ClienteRequestDTO;
import com.example.demo.dto.ClienteResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.services.ClienteService;

@RestController
@RequestMapping(("/api/clientes"))
public class ClienteController {
	private final ClienteService clienteService;
	private final ClienteRepository clienteRepository;
	
	
	public ClienteController(ClienteService clienteService, ClienteRepository clienteRepository) {
		this.clienteService = clienteService;
		this.clienteRepository = clienteRepository;
	}
	
	@GetMapping("/readAllCliente")
	public ResponseEntity<List<ClienteResponseDTO>> getAllCliente(){
		List<ClienteResponseDTO> clientes = clienteService.readAllCliente();
		if(clientes.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(clientes);
	}
	

	
	@GetMapping("/readCliente/{id}")
	public ResponseEntity<ClienteResponseDTO> readCliente(@PathVariable("id") Integer id){
		try {
			ClienteResponseDTO cliente = clienteService.readClientePorId(id);
			return ResponseEntity.ok(cliente);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	
	@PostMapping("/createCliente")
	public ResponseEntity<ClienteResponseDTO> createProducto(@RequestBody ClienteRequestDTO clienteRequestDTO){
		try {
			ClienteResponseDTO clienteNuevo = clienteService.createCliente(clienteRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(clienteNuevo);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	@PutMapping("/updateCliente/{idCliente}")
	public ResponseEntity<ClienteResponseDTO> updateCliente(@PathVariable Integer idCliente, @RequestBody ClienteRequestDTO clienteRequestDTO) {
		try {
			ClienteResponseDTO clienteActualizado = clienteService.updatePago(idCliente, clienteRequestDTO);
			return ResponseEntity.ok(clienteActualizado);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	
	@DeleteMapping("/deleteCliente/{id}")
	public ResponseEntity<Void> deleteCliente(@PathVariable("id") Integer id){
		if(clienteRepository.existsById(id)) {
			clienteRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();		
			  }
	}
}
