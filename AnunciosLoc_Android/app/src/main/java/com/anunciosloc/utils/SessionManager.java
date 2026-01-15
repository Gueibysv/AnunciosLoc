package com.anunciosloc.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF = "anunciosloc_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";

    public static void createSession(Context ctx, Long id, String username) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit()
                .putLong(KEY_USER_ID, id)
                .putString(KEY_USERNAME, username)
                .apply();
    }

    public static boolean isLoggedIn(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.contains(KEY_USER_ID);
    }

    public static void clear(Context ctx) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().clear().apply();
    }
    public static String getUsername(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(KEY_USERNAME, null);
    }
}
