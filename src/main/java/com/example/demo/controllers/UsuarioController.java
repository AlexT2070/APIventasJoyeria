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
	
	@GetMapping("/readUsuarios")
	public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id){
		Optional <Usuario> usuario = usuarioRepository.findById(id);
		return usuario.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping("/createUsuarios")
	public Usuario createUsuario(@RequestBody Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	@PutMapping("/updateUsuariios")
	public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioDetails){
		Optional <Usuario> usuario = usuarioRepository.findById(id);
		if(usuario.isPresent()) {
			Usuario updateUsuario = usuario.get();
			updateUsuario.setNombre(usuarioDetails.getNombre());
			updateUsuario.setPwd(usuarioDetails.getPwd());
			return ResponseEntity.ok(usuarioRepository.save(updateUsuario));
		}else {
			return ResponseEntity.notFound().build();
			}
	}
	
	public ResponseEntity<Void> deteleteUsuario(@PathVariable Integer id){
		if(usuarioRepository.existsById(id)) {
			usuarioRepository.existsById(id);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();
			}
		}
}
