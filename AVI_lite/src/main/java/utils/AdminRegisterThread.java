/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import controller.AdminRegisterController;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.University;
import okhttp3.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.io.IOException;
import utils.University;

public class AdminRegisterThread extends Thread {
    private final String task; 
    private AdminRegisterController controller;
    private String requestBody;
    private ObservableList<University> universityList;

    // Constructor para tareas generales
    public AdminRegisterThread(String task, AdminRegisterController controller) {
        this.task = task;
        this.controller = controller;
    }

    // Constructor para tareas con datos
    public AdminRegisterThread(String task, AdminRegisterController controller, String requestBody) {
        this(task, controller);
        this.requestBody = requestBody;
    }

    @Override
    public void run() {
        switch (task) {
            case "loadUniversities":
                loadUniversities();
                break;
            case "registerUser":
                registerUser();
                break;
            default:
                System.err.println("Tarea no reconocida: " + task);
        }
    }

    private void loadUniversities() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/universidades")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar universidades: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);

            universityList = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonUni = jsonArray.getJSONObject(i);
                University university = new University(
                        jsonUni.getInt("universidad_id"),
                        jsonUni.getString("nombre")
                );
                universityList.add(university);
            }

            Platform.runLater(() -> controller.updateUniversityComboBox(universityList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerUser() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(
            requestBody,
            MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("http://localhost:5000/api/auth/register")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    try {
                        controller.onRegistrationSuccess();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            } else {
                Platform.runLater(() -> controller.onRegistrationFailure(response.code()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> controller.onRegistrationFailure(-1));
        }
    }
}

