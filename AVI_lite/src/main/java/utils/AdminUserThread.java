package utils;

import controller.AdminUsersController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.User;

import java.io.IOException;

public class AdminUserThread extends Thread {

    private final String action;
    private final AdminUsersController controller;
    private final String token;
    private final String jsonInputString;
    private final int userId;

    private static final String BASE_URL = "http://localhost:5000/api/auth/";

    public AdminUserThread(String action, AdminUsersController controller, String token) {
        this(action, controller, token, null, -1);
    }

    public AdminUserThread(String action, AdminUsersController controller, String token, String jsonInputString) {
        this(action, controller, token, jsonInputString, -1);
    }

    public AdminUserThread(String action, AdminUsersController controller, String token, int userId) {
        this(action, controller, token, null, userId);
    }

    public AdminUserThread(String action, AdminUsersController controller, String token, String jsonInputString, int userId) {
        this.action = action;
        this.controller = controller;
        this.token = token;
        this.jsonInputString = jsonInputString;
        this.userId = userId;
    }

    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();
        Request request = null;

        try {
            switch (action) {
                case "loadUsers":
                    request = new Request.Builder()
                            .url(BASE_URL + "getAll")
                            .addHeader("Authorization", token)
                            .get()
                            .build();
                    handleLoadUsers(client, request);
                    break;

                case "addUser":
                    request = createRequest("POST");
                    handleUserModification(client, request);
                    break;

                case "updateUser":
                    request = createRequest("PUT");
                    handleUserModification(client, request);
                    break;

                case "deleteUser":
                    request = new Request.Builder()
                            .url(BASE_URL + userId)
                            .addHeader("Authorization", token)
                            .delete()
                            .build();
                    handleUserModification(client, request);
                    break;

                default:
                    throw new IllegalArgumentException("Acción no soportada: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Request createRequest(String method) throws IOException {
        RequestBody body = RequestBody.create(
                jsonInputString,
                MediaType.parse("application/json; charset=utf-8")
        );
        return new Request.Builder()
                .url(BASE_URL + (userId != -1 ? userId : ""))
                .addHeader("Authorization", token)
                .method(method, body)
                .build();
    }

    private void handleLoadUsers(OkHttpClient client, Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar usuarios: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<User> users = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonUser = jsonArray.getJSONObject(i);
                User newUser = new User(
                        jsonUser.getInt("identificacion"),
                        jsonUser.getString("pass"),
                        jsonUser.getString("token"),
                        jsonUser.getString("nombre"),
                        jsonUser.getString("apellidos"),
                        jsonUser.getString("correo"),
                        jsonUser.getString("rol"),
                        jsonUser.getInt("universidad_id")
                );
                users.add(newUser);
            }

            Platform.runLater(() -> controller.updateUsers(users));
        }
    }

    private void handleUserModification(OkHttpClient client, Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la operación: " + response.code());
            }

            Platform.runLater(() -> controller.refreshUsers());
        }
    }
}
