package com.ApiCrud.API.CRUD.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/protegido")
    public ResponseEntity<String> endpointProtegido() {
        return ResponseEntity.ok("Bienvenido, este es un endpoint protegido");
    }
}
