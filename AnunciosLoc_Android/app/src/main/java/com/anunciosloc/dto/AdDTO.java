package com.anunciosloc.dto;

import java.util.Map;

public class AdDTO {
    public long id;
    public String texto;
    public String editor;
    public long horaPublicacao;

    public long localId;
    public String localNome;   // 👈 IMPORTANTE

    public String politicaTipo;
    public Map<String, String> politicaRestricoes;
    public String modoEntrega;
}
