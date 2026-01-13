package com.anunciosloc.anunciosloc.model;

import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import lombok.Data;

@Entity
@Data
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String texto;

    @Column(nullable = false)
    private String editor;

    @Column(nullable = false)
    private Long horaPublicacao; // epoch millis (igual ao frontend)

    @ManyToOne(optional = false)
    private Location localDestino;

    // Política
    @Column(nullable = false)
    private String politicaTipo;

    @ElementCollection
    @CollectionTable(name = "ad_politica")
    @MapKeyColumn(name = "chave")
    @Column(name = "valor")
    private Map<String, String> politicaRestricoes;

    // Entrega
    @Column(nullable = false)
    private String modoEntrega;
}
