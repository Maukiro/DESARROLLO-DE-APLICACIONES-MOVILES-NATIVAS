package com.ApiCrud.API.CRUD.controller;

import com.ApiCrud.API.CRUD.model.Usuario;
import com.ApiCrud.API.CRUD.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ✅ GET /api/usuarios/email/{email}
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerPorEmail(email);

        return usuarioOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/foto/{email}")
    public ResponseEntity<?> actualizarFotoPerfil(
            @PathVariable String email,
            @RequestParam("foto") MultipartFile foto) {

        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerPorEmail(email);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Usuario usuario = usuarioOpt.get();

            // Simulación de guardado en algún servidor o servicio externo
            String urlFoto = "https://tu-servidor.com/uploads/" + foto.getOriginalFilename();

            // Guardar URL en BD
            usuario.setFotoPerfil(urlFoto);
            usuarioService.guardar(usuario);

            return ResponseEntity.ok("Foto actualizada con éxito");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar foto");
        }
    }

    @PutMapping("/actualizar/{emailOriginal}")
    public ResponseEntity<Usuario> actualizarNombreYCorreo(
            @PathVariable String emailOriginal,
            @RequestParam String nombre,
            @RequestParam String emailNuevo) {

        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorEmail(emailOriginal);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setNombre(nombre);
        usuario.setEmail(emailNuevo);

        usuarioService.guardar(usuario);

        return ResponseEntity.ok(usuario);
    }



}
