/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;


public class TeacherFacAndDepThread extends Thread {
    private final String operationType; // "LOAD_FACULTIES" o "LOAD_DEPARTMENTS"
    private final OkHttpClient client;
    private final String token;
    private final int universityId;
    private final ListView<Object> listView;

    public TeacherFacAndDepThread(String operationType, OkHttpClient client, String token, int universityId, ListView<Object> listView) {
        this.operationType = operationType;
        this.client = client;
        this.token = token;
        this.universityId = universityId;
        this.listView = listView;
    }

    @Override
    public void run() {
        try {
            switch (operationType) {
                case "LOAD_FACULTIES":
                    loadFaculties();
                    break;
                case "LOAD_DEPARTMENTS":
                    loadDepartments();
                    break;
                default:
                    throw new IllegalArgumentException("Operación no válida: " + operationType);
            }
        } catch (IOException e) {
            System.err.println("Error en la operación " + operationType + ": " + e.getMessage());
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

            Platform.runLater(() -> listView.setItems(faculties));
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

            Platform.runLater(() -> listView.setItems(departments));
        }
    }
}
