package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Usuario;
import com.example.demo.repositories.UsuarioRepository;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/readAllUsuarios")
	public List<Usuario> getAllUsuario(){
		return usuarioRepository.findAll();
		}
	
	@GetMapping("/readUsuarios/{id}")
	public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id){
		Optional <Usuario> usuario = usuarioRepository.findById(id);
		return usuario.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping("/createUsuarios")
	public Usuario createUsuario(@RequestBody Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	@PutMapping("/updateUsuarios/{id}")
	public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioDetails){
		Optional <Usuario> usuarioOp = usuarioRepository.findById(id);
		 if(usuarioOp.isPresent()) {
		        Usuario usuarioExistente = usuarioOp.get();
    
		        if (usuarioDetails.getNombre() != null) {
		            usuarioExistente.setNombre(usuarioDetails.getNombre());
		        }
		        if (usuarioDetails.getPwd() != null) {
		            usuarioExistente.setPwd(usuarioDetails.getPwd());
		        }

		        return ResponseEntity.ok(usuarioRepository.save(usuarioExistente));
		    } else {
		        return ResponseEntity.notFound().build();
		    }
	}
	
	@DeleteMapping("/deleteUsuarios/{id}")
	public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id){
		if(usuarioRepository.existsById(id)) {
			usuarioRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();
			}
		}
}
