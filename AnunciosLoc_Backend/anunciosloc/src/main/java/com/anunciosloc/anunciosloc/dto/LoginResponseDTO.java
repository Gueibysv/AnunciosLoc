package com.anunciosloc.anunciosloc.dto;

public record LoginResponseDTO(
        Long userId,
        String username,
        String password
) {}
