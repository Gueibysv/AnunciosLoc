package com.anunciosloc.network;

import com.anunciosloc.dto.UserDTO;
import org.json.JSONObject;

public class AuthService {

    public static UserDTO login(String username, String password) throws Exception {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);

        String response = ApiClient.post("/auth/login", body.toString());
        JSONObject json = new JSONObject(response);

        UserDTO user = new UserDTO();
        user.userId = json.getLong("userId");
        user.username = json.getString("username");

        return user;
    }

    // 👉 NOVO MÉTODO REGISTER
    public static UserDTO register(String username, String password) throws Exception {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);

        String response = ApiClient.post("/auth/register", body.toString());
        JSONObject json = new JSONObject(response);

        UserDTO user = new UserDTO();
        user.userId = json.getLong("id"); // backend devolve id
        user.username = json.getString("username");

        return user;
    }
}
