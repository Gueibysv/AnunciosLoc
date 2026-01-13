package com.anunciosloc.anunciosloc.dto;

public record LoginResponseDTO(
        String token,
        Long userId,
        String username,
        String password
) {}
