package com.anunciosloc.anunciosloc.controller;


import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.anunciosloc.anunciosloc.dto.AdRequestDTO;
import com.anunciosloc.anunciosloc.model.Advertisement;
import com.anunciosloc.anunciosloc.model.Location;
import com.anunciosloc.anunciosloc.repository.AdRepository;

import com.anunciosloc.anunciosloc.repository.LocationRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private static final Logger log = LoggerFactory.getLogger(AdController.class);

    private final AdRepository adRepo;
    private final LocationRepository locationRepo;

   @PostMapping
public Advertisement create(@RequestBody AdRequestDTO req) {

    log.info("➡️ Criar anúncio para local {}", req.localDestinoId());

    Location local = locationRepo.findById(req.localDestinoId())
        .orElseThrow(() -> {
            log.warn("❌ Local não encontrado: {}", req.localDestinoId());
            return new IllegalArgumentException("LocalDestinoId inválido");
        });

    Advertisement ad = new Advertisement();
    ad.setTexto(req.texto());
    ad.setEditor(req.editor());
    ad.setHoraPublicacao(req.horaPublicacao());
    ad.setLocalDestino(local);
    ad.setPoliticaTipo(req.politicaTipo());
    ad.setPoliticaRestricoes(req.politicaRestricoes());
    ad.setModoEntrega(req.modoEntrega());

    Advertisement saved = adRepo.save(ad);

    log.info("✅ Anúncio criado com ID {}", saved.getId());
    return saved;
}



    @GetMapping
    public List<Advertisement> list() {
        log.info("📥 Listagem de anúncios solicitada");
        return adRepo.findAll();
    }
}
