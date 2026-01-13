package com.anunciosloc.models;

import java.util.HashMap;
import java.util.Map;

public class Perfil {
    private String username;
    private Map<String, String> chaves;

    public Perfil(String username) {
        this.username = username;
        this.chaves = new HashMap<>();
    }

    // Getters e Setters
    public String getUsername() { return username; }
    public Map<String, String> getChaves() { return chaves; }

    public void adicionarChave(String chave, String valor) {
        chaves.put(chave, valor);
    }

    public void removerChave(String chave) {
        chaves.remove(chave);
    }
}
