package com.anunciosloc.utils;

import com.anunciosloc.data.MockDataSource;

/**
 * Classe utilitária para gerir o estado da sessão do utilizador.
 * Em um projeto real, usaria SharedPreferences ou um armazenamento seguro.
 */
public class SessionManager {
    private static String currentSessionId = null;

    public static void createSession(String sessionId) {
        currentSessionId = sessionId;
    }

    public static void clearSession() {
        if (currentSessionId != null) {
            MockDataSource.logout(currentSessionId);
        }
        currentSessionId = null;
    }

    public static String getCurrentSessionId() {
        return currentSessionId;
    }

    public static boolean isLoggedIn() {
        return currentSessionId != null && MockDataSource.isAuthenticated(currentSessionId);
    }
}
