/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;


import javafx.application.Platform;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class TeacherModAssignmentThread extends Thread {
    private final String operationType; // CREATE, UPDATE, DELETE
    private final OkHttpClient client;
    private final String token;
    private final JSONObject assignmentData;
    private final int assignmentId;
    private final String apiUrl;
    private final Runnable onSuccess;

    public TeacherModAssignmentThread(
            String operationType,
            OkHttpClient client,
            String token,
            JSONObject assignmentData,
            int assignmentId,
            String apiUrl,
            Runnable onSuccess) {
        this.operationType = operationType;
        this.client = client;
        this.token = token;
        this.assignmentData = assignmentData;
        this.assignmentId = assignmentId;
        this.apiUrl = apiUrl;
        this.onSuccess = onSuccess;
    }

    @Override
    public void run() {
        try {
            switch (operationType) {
                case "CREATE":
                    createAssignment();
                    break;
                case "UPDATE":
                    updateAssignment();
                    break;
                case "DELETE":
                    deleteAssignment();
                    break;
                default:
                    throw new IllegalArgumentException("Operación no válida: " + operationType);
            }
        } catch (IOException e) {
            System.err.println("Error en la operación " + operationType + ": " + e.getMessage());
        }
    }

    private void createAssignment() throws IOException {
        RequestBody body = RequestBody.create(
                assignmentData.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", token)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Platform.runLater(onSuccess);
            } else {
                throw new IOException("Error al crear la asignación: " + response.code());
            }
        }
    }

    private void updateAssignment() throws IOException {
        RequestBody body = RequestBody.create(
                assignmentData.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(apiUrl + "/" + assignmentId)
                .addHeader("Authorization", token)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Platform.runLater(onSuccess);
            } else {
                throw new IOException("Error al actualizar la asignación: " + response.code());
            }
        }
    }

    private void deleteAssignment() throws IOException {
        Request request = new Request.Builder()
                .url(apiUrl + "/" + assignmentId)
                .addHeader("Authorization", token)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Platform.runLater(onSuccess);
            } else {
                throw new IOException("Error al eliminar la asignación: " + response.code());
            }
        }
    }
}
