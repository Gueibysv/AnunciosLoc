package com.anunciosloc.data;

import com.anunciosloc.models.Anuncio;
import com.anunciosloc.models.Local;
import com.anunciosloc.models.Perfil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe para simular a interação com o servidor e os beacons.
 * Contém dados codificados (mocked data) para todas as funcionalidades.
 */
public class MockDataSource {

    private static String currentSessionId = null;
    private static Perfil currentUserProfile = null;
    private static final Map<String, String> users = new HashMap<>();
    private static final List<Local> locations = new ArrayList<>();
    private static final List<Anuncio> ads = new ArrayList<>();

    static {
        // Usuários simulados
        users.put("alice", "1234");
        users.put("bob", "5678");

        // Locais simulados (GPS)
        locations.add(new Local("L1", "Largo da Independência", 38.7343829, -9.1403882, 20.0));
        locations.add(new Local("L2", "Belas Shopping", 38.7500000, -9.1500000, 50.0));

        // Locais simulados (WiFi)
        locations.add(new Local("L3", "Ginásio do Camama I", Arrays.asList("WIFI_GYM_1", "WIFI_GYM_2")));

        // Anúncios simulados
        Map<String, String> policy1 = new HashMap<>();
        policy1.put("Profissao", "Estudante");
        ads.add(new Anuncio("A1", "Alugo quarto perto da universidade. Contacto: 912345678", "alice", "L1", System.currentTimeMillis(), "Whitelist", policy1, "Centralizado"));
        ads.add(new Anuncio("A2", "Venda de bilhetes para o jogo de futebol. Últimas unidades!", "bob", "L2", System.currentTimeMillis(), "Blacklist", Collections.emptyMap(), "Descentralizado"));
    }

    // --- Funções de Autenticação (F1, F2) ---

    public static boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // Usuário já existe
        }
        users.put(username, password);
        return true;
    }

    public static String login(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            // Simula a criação de um ID de sessão
            currentSessionId = "SESSION_" + username + "_" + System.currentTimeMillis();
            currentUserProfile = new Perfil(username);
            return currentSessionId;
        }
        return null; // Credenciais inválidas
    }

    public static void logout(String sessionId) {
        if (sessionId.equals(currentSessionId)) {
            currentSessionId = null;
            currentUserProfile = null;
        }
    }

    public static boolean isAuthenticated(String sessionId) {
        return currentSessionId != null && currentSessionId.equals(sessionId);
    }

    // --- Funções de Locais (F3) ---

    public static List<Local> getLocations() {
        return locations;
    }

    public static void addLocation(Local local) {
        locations.add(local);
    }

    public static boolean removeLocation(String localId) {
        return locations.removeIf(local -> local.getId().equals(localId));
    }

    // --- Funções de Anúncios (F4, F5) ---

    public static List<Anuncio> getAds() {
        return ads;
    }

    public static void addAd(Anuncio anuncio) {
        ads.add(anuncio);
    }

    public static boolean removeAd(String adId) {
        return ads.removeIf(ad -> ad.getId().equals(adId));
    }

    public static Anuncio getAdById(String adId) {
        for (Anuncio ad : ads) {
            if (ad.getId().equals(adId)) {
                return ad;
            }
        }
        return null;
    }

    // --- Funções de Perfil (F6) ---

    public static Perfil getCurrentUserProfile() {
        return currentUserProfile;
    }

    public static List<String> getAllProfileKeys() {
        // Simula a lista global de chaves
        return Arrays.asList("Profissao", "Clube", "Interesse", "Idade");
    }
}
