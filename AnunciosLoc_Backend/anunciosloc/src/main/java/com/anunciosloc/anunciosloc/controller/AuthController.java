package com.anunciosloc.anunciosloc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.anunciosloc.anunciosloc.Auth.*;


import com.anunciosloc.anunciosloc.dto.LoginResponseDTO;
import com.anunciosloc.anunciosloc.model.User;
import com.anunciosloc.anunciosloc.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

     private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginResponseDTO req) {

        log.info("Tentativa de login | username={}", req.username());

        User user = userRepo.findByUsername(req.username())
                .orElseThrow(() -> {
                    log.warn("Login falhou: utilizador não encontrado");
                    return new RuntimeException("Utilizador não encontrado");
                });

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            log.warn("Login falhou: password incorreta | username={}", req.username());
            throw new RuntimeException("Password incorreta");
        }

        String token = jwtService.generateToken(user);

        log.info("Login bem-sucedido | userId={}", user.getId());

        return new LoginResponseDTO(token, user.getId(), user.getUsername(), user.getPassword());
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        log.info("📝 Pedido de registo recebido");
        log.info("➡ Username: {}", user.getUsername());

        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            log.warn("⚠ Username já existe: {}", user.getUsername());
            throw new RuntimeException("Username já existe");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        log.info("✅ Utilizador registado com sucesso: {}", user.getUsername());
        return userRepo.save(user);
    }
}
