package com.anunciosloc.anunciosloc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record AdRequestDTO(

    @NotBlank(message = "Texto do anúncio é obrigatório")
    String texto,

    @NotBlank(message = "Editor é obrigatório")
    String editor,

    @NotBlank(message = "ID do local é obrigatório")
    long localDestinoId,

    @NotNull(message = "Hora de publicação é obrigatória")
    Long horaPublicacao,

    @NotBlank(message = "Tipo de política é obrigatório")
    String politicaTipo,

    Map<String, String> politicaRestricoes,

    @NotBlank(message = "Modo de entrega é obrigatório")
    String modoEntrega
) {}
