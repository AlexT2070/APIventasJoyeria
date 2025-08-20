package com.example.demo.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.JWTUtil;
import com.example.demo.repositories.UsuarioRepository;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.models.Usuario;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JWTUtil jwtUtil;
    


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreAndPwd(loginRequest.getNombre(),loginRequest.getPwd());
        
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getPwd().equals(loginRequest.getPwd())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        Usuario usuario = usuarioOpt.get();
        String token = jwtUtil.generarToken(usuario.getNombre(), usuario.getPwd());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setNombre(usuario.getNombre());
        response.setPwd(usuario.getPwd());

        return ResponseEntity.ok(response);
    }
}