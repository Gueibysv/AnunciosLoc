package com.anunciosloc.anunciosloc.model;

import java.util.HashMap;
import java.util.Map;


import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // Perfil dinâmico
    @ElementCollection
    @CollectionTable(name = "user_profile_keys")
    @MapKeyColumn(name = "chave")
    @Column(name = "valor")
    private Map<String, String> chaves = new HashMap<>();
}
