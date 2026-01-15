package com.anunciosloc.network;
import android.util.Log;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    private static final String TAG = "API_CLIENT";

    public static String post(String endpoint, String jsonBody) throws Exception {

        Log.d(TAG, "POST " + ApiConfig.BASE_URL + endpoint);
        Log.d(TAG, "BODY = " + jsonBody);

        URL url = new URL(ApiConfig.BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        os.write(jsonBody.getBytes("UTF-8"));
        os.close();

        int code = conn.getResponseCode();
        Log.d(TAG, "HTTP CODE = " + code);

        InputStreamReader isr =
                (code >= 200 && code < 300)
                        ? new InputStreamReader(conn.getInputStream())
                        : new InputStreamReader(conn.getErrorStream());

        BufferedReader reader = new BufferedReader(isr);

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        Log.d(TAG, "RESPONSE = " + response.toString());
        return response.toString();
    }


    public static String get(String endpoint) throws Exception {

        URL url = new URL(ApiConfig.BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        return response.toString();
    }

}

