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
import java.time.LocalDateTime;

public class TeacherAssignmentsThread extends Thread {
    private final String operationType; // "LOAD_COURSES", "LOAD_ASSIGNMENTS"
    private final OkHttpClient client;
    private final String token;
    private final Object additionalData; // Datos adicionales necesarios según la operación
    private final ListView<Course> coursesListView; // ListView para cursos
    private final ListView<Assignment> assignmentsListView; // ListView para asignaciones

    public TeacherAssignmentsThread(String operationType, OkHttpClient client, String token, Object additionalData, ListView<Course> coursesListView, ListView<Assignment> assignmentsListView) {
        this.operationType = operationType;
        this.client = client;
        this.token = token;
        this.additionalData = additionalData;
        this.coursesListView = coursesListView;
        this.assignmentsListView = assignmentsListView;
    }

    @Override
    public void run() {
        try {
            switch (operationType) {
                case "LOAD_COURSES":
                    loadCourses();
                    break;
                case "LOAD_ASSIGNMENTS":
                    loadAssignments();
                    break;
                default:
                    throw new IllegalArgumentException("Operación no válida: " + operationType);
            }
        } catch (IOException e) {
            System.err.println("Error en la operación " + operationType + ": " + e.getMessage());
        }
    }

    private void loadCourses() throws IOException {
        String url = "http://localhost:5000/api/cursos/profesor/" + User.getInstance().getID();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar cursos: " + response.code());
            }

            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<Course> courses = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonCourse = jsonArray.getJSONObject(i);
                Course course = new Course(
                        jsonCourse.getInt("curso_id"),
                        jsonCourse.getInt("carrera_id"),
                        jsonCourse.getInt("profesor_id"),
                        jsonCourse.getString("nombre"),
                        jsonCourse.getString("informacion"),
                        jsonCourse.getInt("campos_totales")
                );
                courses.add(course);
            }

            Platform.runLater(() -> coursesListView.setItems(courses));
        }
    }

    private void loadAssignments() throws IOException {
        int courseId = (int) additionalData;
        String url = "http://localhost:5000/api/asignaciones/curso/" + courseId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar asignaciones: " + response.code());
            }

            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<Assignment> assignments = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonAssign = jsonArray.getJSONObject(i);
                Assignment assignment = new Assignment(
                        jsonAssign.getInt("id_asignacion"),
                        jsonAssign.getString("titulo"),
                        jsonAssign.getInt("id_profesor"),
                        jsonAssign.getInt("id_curso"),
                        jsonAssign.getInt("valor_porcentual"),
                        LocalDateTime.parse(jsonAssign.getString("fecha_limite").replace("Z", "")),
                        jsonAssign.getString("descripcion")
                );
                assignments.add(assignment);
            }

            Platform.runLater(() -> assignmentsListView.setItems(assignments));
        }
    }
}
