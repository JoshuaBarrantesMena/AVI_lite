/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class TeacherAddFacAndDepThread extends Thread {
    private final String operationType; // Determina si es "CREATE" o "LOAD"
    private final OkHttpClient client;
    private final String token;
    private final int universityId;
    private final ComboBox<Object> comboBox; // Usado solo para cargar datos
    private final String name; // Nombre del elemento a crear
    private final int relatedId; // ID relacionado (facultad/departamento) para creación

    public TeacherAddFacAndDepThread(String operationType, OkHttpClient client, String token, int universityId, ComboBox<Object> comboBox, String name, int relatedId) {
        this.operationType = operationType;
        this.client = client;
        this.token = token;
        this.universityId = universityId;
        this.comboBox = comboBox;
        this.name = name;
        this.relatedId = relatedId;
    }

    @Override
    public void run() {
        try {
            switch (operationType) {
                case "CREATE_FACULTY":
                    createFaculty();
                    break;
                case "CREATE_DEPARTMENT":
                    createDepartment();
                    break;
                case "CREATE_CAREER":
                    createCareer();
                    break;
                case "LOAD_FACULTIES":
                    loadFaculties();
                    break;
                case "LOAD_DEPARTMENTS":
                    loadDepartments();
                    break;
                default:
                    throw new IllegalArgumentException("Operación no reconocida: " + operationType);
            }
        } catch (IOException e) {
            System.err.println("Error ejecutando operación: " + e.getMessage());
        }
    }

    private void createFaculty() throws IOException {
        String jsonInputString = "{\"nombre\": \"" + name + "\", \"universidad_id\": " + universityId + "}";
        executePostRequest("http://localhost:5000/api/facultades", jsonInputString);
    }

    private void createDepartment() throws IOException {
        String jsonInputString = "{\"nombre\": \"" + name + "\", \"facultad_id\": " + relatedId + "}";
        executePostRequest("http://localhost:5000/api/departamentos", jsonInputString);
    }

    private void createCareer() throws IOException {
        String jsonInputString = "{\"nombre\": \"" + name + "\", \"departamento_id\": " + relatedId + "}";
        executePostRequest("http://localhost:5000/api/carreras", jsonInputString);
    }

    private void executePostRequest(String url, String jsonInputString) throws IOException {
        RequestBody body = RequestBody.create(jsonInputString, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error: " + response.code() + " - " + response.message());
            }
        }
    }

    private void loadFaculties() throws IOException {
        String url = "http://localhost:5000/api/facultades/byUniversity?universidad_id=" + universityId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar facultades: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<Object> faculties = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonFac = jsonArray.getJSONObject(i);
                Faculty faculty = new Faculty(
                        jsonFac.getInt("facultad_id"),
                        jsonFac.getInt("universidad_id"),
                        jsonFac.getString("nombre")
                );
                faculties.add(faculty);
            }
            updateComboBox(faculties);
        }
    }

    private void loadDepartments() throws IOException {
        String url = "http://localhost:5000/api/departamentos/university/" + universityId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar departamentos: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<Object> departments = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonDep = jsonArray.getJSONObject(i);
                Department department = new Department(
                        jsonDep.getInt("departamento_id"),
                        jsonDep.getInt("facultad_id"),
                        jsonDep.getString("nombre")
                );
                departments.add(department);
            }
            updateComboBox(departments);
        }
    }

    private void updateComboBox(ObservableList<Object> items) {
        javafx.application.Platform.runLater(() -> comboBox.setItems(items));
    }
}
