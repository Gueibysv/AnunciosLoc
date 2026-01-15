package com.anunciosloc.dto;

import java.util.List;

public class LocationRequestDTO {
    public String nome;
    public String tipoCoordenada; // "GPS" ou "WIFI"
    public Double latitude;
    public Double longitude;
    public Double raioMetros;
    public List<String> wifiSSIDs;
    public String username;
}
