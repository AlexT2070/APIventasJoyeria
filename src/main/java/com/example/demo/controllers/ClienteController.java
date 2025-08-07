package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Cliente;
import com.example.demo.repositories.ClienteRepository;

@RestController
@RequestMapping(("/api/clientes"))
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@GetMapping("/readAllCliente")
	public List<Cliente> getAllClientes(){
		return clienteRepository.findAll();
	}
	
	@GetMapping("/readCliente")
	public ResponseEntity<Cliente> getClienteById(@PathVariable Integer id){
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping("/createCliente")
	public Cliente createCliente(@RequestBody Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	@PutMapping("/updateCliente")
	public ResponseEntity<Cliente> updateCliente(@PathVariable Integer id, @RequestBody Cliente clienteDetails) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if(cliente.isPresent()) {
			Cliente updatedCliente = cliente.get();
			updatedCliente.setNombreCliente(clienteDetails.getNombreCliente());
			updatedCliente.setApellidos(clienteDetails.getApellidos());
			updatedCliente.setCorreo(clienteDetails.getCorreo());
			updatedCliente.setDireccion(clienteDetails.getDireccion());
			updatedCliente.setTelefono(clienteDetails.getTelefono());
			updatedCliente.setDireccion(clienteDetails.getDireccion());
			return ResponseEntity.ok(clienteRepository.save(updatedCliente));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/deleteCliente")
	public ResponseEntity<Void> deleteCliente(@PathVariable Integer id){
		if(clienteRepository.existsById(id)) {
			clienteRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();		
			  }
	}
}
