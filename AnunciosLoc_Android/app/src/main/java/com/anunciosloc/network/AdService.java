package com.anunciosloc.network;

import com.anunciosloc.dto.AdDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AdService {

    public static void publishAd(
            String texto,
            String editor,
            long localDestinoId,
            long horaPublicacao,
            String politicaTipo,
            Map<String, String> politicaRestricoes,
            String modoEntrega
    ) throws Exception {

        JSONObject body = new JSONObject();
        body.put("texto", texto);
        body.put("editor", editor);
        body.put("localDestinoId", localDestinoId);
        body.put("horaPublicacao", horaPublicacao);
        body.put("politicaTipo", politicaTipo);
        body.put("modoEntrega", modoEntrega);

        if (politicaRestricoes != null) {
            body.put("politicaRestricoes", new JSONObject(politicaRestricoes));
        } else {
            body.put("politicaRestricoes", JSONObject.NULL);
        }

        ApiClient.post("/ads", body.toString());
    }


    public static List<AdDTO> getAllAds() throws Exception {

        String response = ApiClient.get("/ads");
        JSONArray array = new JSONArray(response);

        List<AdDTO> ads = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);

            AdDTO ad = new AdDTO();
            ad.id = json.getLong("id");
            ad.texto = json.getString("texto");
            ad.editor = json.getString("editor");
            ad.horaPublicacao = json.getLong("horaPublicacao");
            ad.politicaTipo = json.getString("politicaTipo");
            ad.modoEntrega = json.getString("modoEntrega");

            // 👇 LOCAL DESTINO
            JSONObject localJson = json.getJSONObject("localDestino");
            ad.localId = localJson.getLong("id");
            ad.localNome = localJson.getString("nome");

            ads.add(ad);
        }

        return ads;
    }


    public static AdDTO getAdById(long id) throws Exception {

        String response = ApiClient.get("/ads/" + id);
        JSONObject json = new JSONObject(response);

        AdDTO ad = new AdDTO();
        ad.id = json.getLong("id");
        ad.texto = json.getString("texto");
        ad.editor = json.getString("editor");
        ad.horaPublicacao = json.getLong("horaPublicacao");
        ad.politicaTipo = json.getString("politicaTipo");
        ad.modoEntrega = json.getString("modoEntrega");
        ad.localId =
                json.getJSONObject("localDestino").getLong("id");

        return ad;
    }
    private static Map<String, String> jsonToMap(JSONObject json) {
        Map<String, String> map = new HashMap<>();

        if (json == null) return map;

        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            map.put(key, json.optString(key));
        }

        return map;
    }

}
