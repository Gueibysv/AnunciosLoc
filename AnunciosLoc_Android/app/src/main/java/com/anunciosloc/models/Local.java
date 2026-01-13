package com.anunciosloc.models;

import java.util.List;

public class Local {
    private String id;
    private String nome;
    private String tipoCoordenada; // "GPS" ou "WIFI"

    // Dados GPS
    private Double latitude;
    private Double longitude;
    private Double raioMetros;

    // Dados WiFi
    private List<String> wifiSSIDs;

    public Local(String id, String nome, Double latitude, Double longitude, Double raioMetros) {
        this.id = id;
        this.nome = nome;
        this.tipoCoordenada = "GPS";
        this.latitude = latitude;
        this.longitude = longitude;
        this.raioMetros = raioMetros;
    }

    public Local(String id, String nome, List<String> wifiSSIDs) {
        this.id = id;
        this.nome = nome;
        this.tipoCoordenada = "WIFI";
        this.wifiSSIDs = wifiSSIDs;
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getTipoCoordenada() { return tipoCoordenada; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Double getRaioMetros() { return raioMetros; }
    public List<String> getWifiSSIDs() { return wifiSSIDs; }

    @Override
    public String toString() {
        if ("GPS".equals(tipoCoordenada)) {
            return nome + " (GPS: " + latitude + ", " + longitude + ", " + raioMetros + "m)";
        } else {
            return nome + " (WiFi: " + wifiSSIDs.size() + " IDs)";
        }
    }
}
