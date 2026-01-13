package com.anunciosloc.anunciosloc.model;

import java.util.List;


import jakarta.persistence.EnumType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoCoordenada tipoCoordenada; // GPS | WIFI

    // GPS
    private Double latitude;
    private Double longitude;
    private Double raioMetros;

    // WIFI
    @ElementCollection
    @CollectionTable(name = "location_wifi_ssids")
    private List<String> wifiSSIDs;

    @ManyToOne(optional = false)
    private User owner;
}
