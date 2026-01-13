package com.anunciosloc.models;

import java.util.Map;

public class Anuncio {
    private String id;
    private String texto;
    private String editor;
    private String localDestinoId;
    private long horaPublicacao;
    private String politicaTipo; // "Whitelist" ou "Blacklist"
    private Map<String, String> politicaRestricoes; // Chave=Valor
    private String modoEntrega; // "Centralizado" ou "Descentralizado"

    public Anuncio(String id, String texto, String editor, String localDestinoId, long horaPublicacao, String politicaTipo, Map<String, String> politicaRestricoes, String modoEntrega) {
        this.id = id;
        this.texto = texto;
        this.editor = editor;
        this.localDestinoId = localDestinoId;
        this.horaPublicacao = horaPublicacao;
        this.politicaTipo = politicaTipo;
        this.politicaRestricoes = politicaRestricoes;
        this.modoEntrega = modoEntrega;
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getTexto() { return texto; }
    public String getEditor() { return editor; }
    public String getLocalDestinoId() { return localDestinoId; }
    public long getHoraPublicacao() { return horaPublicacao; }
    public String getPoliticaTipo() { return politicaTipo; }
    public Map<String, String> getPoliticaRestricoes() { return politicaRestricoes; }
    public String getModoEntrega() { return modoEntrega; }

    @Override
    public String toString() {
        return texto.substring(0, Math.min(texto.length(), 30)) + "... (Local: " + localDestinoId + ")";
    }
}
