/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class TeacherAdminCoursesThread extends Thread {
    private final String operationType; // "LOAD_COURSES", "LOAD_CAREERS", "ADD_COURSE", "UPDATE_COURSE", "DELETE_COURSE"
    private final OkHttpClient client;
    private final String token;
    private final Object additionalData; // Datos adicionales necesarios según la operación
    private final ListView<Course> coursesListView; // Usado para actualizar la lista de cursos
    private final ComboBox<Career> careersComboBox; // Usado para actualizar la lista de carreras

    public TeacherAdminCoursesThread(String operationType, OkHttpClient client, String token, Object additionalData, ListView<Course> coursesListView, ComboBox<Career> careersComboBox) {
        this.operationType = operationType;
        this.client = client;
        this.token = token;
        this.additionalData = additionalData;
        this.coursesListView = coursesListView;
        this.careersComboBox = careersComboBox;
    }

    @Override
    public void run() {
        try {
            switch (operationType) {
                case "LOAD_COURSES":
                    loadCourses();
                    break;
                case "LOAD_CAREERS":
                    loadCareers();
                    break;
                case "ADD_COURSE":
                    addCourse();
                    break;
                case "UPDATE_COURSE":
                    updateCourse();
                    break;
                case "DELETE_COURSE":
                    deleteCourse();
                    break;
                default:
                    throw new IllegalArgumentException("Operación no válida: " + operationType);
            }
        } catch (IOException e) {
            System.err.println("Error ejecutando operación: " + e.getMessage());
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
            javafx.application.Platform.runLater(() -> coursesListView.setItems(courses));
        }
    }

    private void loadCareers() throws IOException {
        String url = "http://localhost:5000/api/carreras/universidad/" + User.getInstance().getUniversityID();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar carreras: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<Career> careers = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonCareer = jsonArray.getJSONObject(i);
                Career career = new Career(
                        jsonCareer.getInt("carrera_id"),
                        jsonCareer.getInt("departamento_id"),
                        jsonCareer.getString("nombre")
                );
                careers.add(career);
            }
            javafx.application.Platform.runLater(() -> careersComboBox.setItems(careers));
        }
    }

    private void addCourse() throws IOException {
        String jsonInputString = (String) additionalData;
        executePostOrPutRequest("http://localhost:5000/api/cursos/", jsonInputString, "POST");
    }

    private void updateCourse() throws IOException {
        String[] data = (String[]) additionalData;
        String jsonInputString = data[0];
        String courseId = data[1];
        executePostOrPutRequest("http://localhost:5000/api/cursos/" + courseId, jsonInputString, "PUT");
    }

    private void deleteCourse() throws IOException {
        int courseId = (int) additionalData;
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/cursos/" + courseId)
                .addHeader("Authorization", token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al eliminar curso: " + response.code());
            }
            javafx.application.Platform.runLater(() -> coursesListView.getItems().removeIf(course -> course.getId() == courseId));
        }
    }

    private void executePostOrPutRequest(String url, String jsonInputString, String method) throws IOException {
        RequestBody body = RequestBody.create(jsonInputString, MediaType.parse("application/json; charset=utf-8"));
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token);
        if ("POST".equals(method)) {
            requestBuilder.post(body);
        } else if ("PUT".equals(method)) {
            requestBuilder.put(body);
        }
        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error: " + response.code());
            }
            javafx.application.Platform.runLater(() -> {
                try {
                    loadCourses(); // Recargar cursos después de agregar o actualizar
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
