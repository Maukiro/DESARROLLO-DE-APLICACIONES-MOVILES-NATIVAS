package com.ApiCrud.API.CRUD.controller;

import com.ApiCrud.API.CRUD.dto.RegistroUsuarioDTO;
import com.ApiCrud.API.CRUD.dto.LoginDTO;
import com.ApiCrud.API.CRUD.model.Rol;
import com.ApiCrud.API.CRUD.model.Usuario;
import com.ApiCrud.API.CRUD.security.JwtUtil;
import com.ApiCrud.API.CRUD.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody RegistroUsuarioDTO dto,
                                                    @RequestParam Rol role) {
        Usuario nuevoUsuario = usuarioService.registrarUsuario(dto, role);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerPorEmail(loginDTO.getEmail());

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        Usuario usuario = usuarioOptional.get();

        if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRole().name());
        return ResponseEntity.ok(token);
    }
}
