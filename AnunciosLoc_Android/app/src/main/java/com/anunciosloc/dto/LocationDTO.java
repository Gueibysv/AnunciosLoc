package com.anunciosloc.dto;

import java.util.List;

public class LocationDTO {
    public long id;
    public String nome;
    public String tipoCoordenada;
    public Double latitude;
    public Double longitude;
    public Double raioMetros;
    public List<String> wifiSSIDs;

    @Override
    public String toString() {
        return nome; // 👈 Spinner mostra só o nome
    }
}
