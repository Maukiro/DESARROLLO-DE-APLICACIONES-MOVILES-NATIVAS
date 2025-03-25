package com.ApiCrud.API.CRUD.service;

import com.ApiCrud.API.CRUD.dto.RegistroUsuarioDTO;
import com.ApiCrud.API.CRUD.model.Rol;
import com.ApiCrud.API.CRUD.model.Usuario;
import com.ApiCrud.API.CRUD.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(RegistroUsuarioDTO dto, Rol role) {
        System.out.println("Registrando usuario");
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRole(role);
        usuario.setFotoPerfil("https://ui-avatars.com/api/?name=" + dto.getNombre().replace(" ", "+"));

        return usuarioRepository.save(usuario);
    }

    // ✅ Este método es el que necesitas para el login y para obtener datos
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

}
