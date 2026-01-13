package com.anunciosloc.anunciosloc.dto;

import java.util.List;

public record LocationRequestDTO(
    String nome,
    String tipoCoordenada,
    Double latitude,
    Double longitude,
    Double raioMetros,
    List<String> wifiSSIDs,
    String username
) {}
