package com.anunciosloc.anunciosloc.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.anunciosloc.anunciosloc.dto.LocationRequestDTO;
import com.anunciosloc.anunciosloc.model.Location;
import com.anunciosloc.anunciosloc.model.TipoCoordenada;
import com.anunciosloc.anunciosloc.model.User;
import com.anunciosloc.anunciosloc.repository.LocationRepository;
import com.anunciosloc.anunciosloc.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    private final LocationRepository locationRepo;
    private final UserRepository userRepo;

    @PostMapping
public Location create(@RequestBody LocationRequestDTO dto) {
    log.info("Criar local: {}", dto.nome());

    User user = userRepo.findByUsername(dto.username())
        .orElseThrow(() -> new RuntimeException("User não encontrado"));

    Location loc = new Location();
    loc.setNome(dto.nome());
    loc.setTipoCoordenada(TipoCoordenada.valueOf(dto.tipoCoordenada()));
    loc.setLatitude(dto.latitude());
    loc.setLongitude(dto.longitude());
    loc.setRaioMetros(dto.raioMetros());
    loc.setWifiSSIDs(dto.wifiSSIDs());
    loc.setOwner(user);

    return locationRepo.save(loc);
}

    @GetMapping
    public List<Location> list() {
        log.info("📤 Listagem de locais solicitada");
        return locationRepo.findAll();
    }
}
