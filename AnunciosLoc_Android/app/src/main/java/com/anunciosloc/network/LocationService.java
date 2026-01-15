package com.anunciosloc.network;

import com.anunciosloc.dto.LocationDTO;
import com.anunciosloc.dto.LocationRequestDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationService {

    public static void create(LocationRequestDTO dto) throws Exception {

        JSONObject body = new JSONObject();
        body.put("nome", dto.nome);
        body.put("tipoCoordenada", dto.tipoCoordenada);
        body.put("latitude", dto.latitude);
        body.put("longitude", dto.longitude);
        body.put("raioMetros", dto.raioMetros);
        body.put("username", dto.username);

        JSONArray wifiArray = new JSONArray();
        if (dto.wifiSSIDs != null) {
            for (String ssid : dto.wifiSSIDs) {
                wifiArray.put(ssid);
            }
        }
        body.put("wifiSSIDs", wifiArray);

        ApiClient.post("/locations", body.toString());
    }

    public static List<LocationDTO> getLocations() throws Exception {

        String response = ApiClient.get("/locations");
        JSONArray array = new JSONArray(response);

        List<LocationDTO> locations = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);

            LocationDTO loc = new LocationDTO();
            loc.id = json.getLong("id");
            loc.nome = json.getString("nome");
            loc.tipoCoordenada = json.getString("tipoCoordenada");

            if (!json.isNull("latitude"))
                loc.latitude = json.getDouble("latitude");

            if (!json.isNull("longitude"))
                loc.longitude = json.getDouble("longitude");

            if (!json.isNull("raioMetros"))
                loc.raioMetros = json.getDouble("raioMetros");

            locations.add(loc);
        }

        return locations;
    }
}
