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
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class TeacherEnrollStudentsThread extends Thread {
    private final String operationType; // "LOAD_COURSES", "LOAD_STUDENTS", "ENROLL_STUDENT"
    private final OkHttpClient client;
    private final String token;
    private final Object additionalData; // Datos adicionales necesarios según la operación
    private final ObservableList<Course> courses; // Cursos a cargar (solo para operaciones relacionadas con cursos)
    private final ObservableList<User> students; // Estudiantes a cargar (solo para operaciones relacionadas con estudiantes)
    private final ListView<Course> coursesListView;
    private final ListView<User> studentsListView;

    public TeacherEnrollStudentsThread(
            String operationType,
            OkHttpClient client,
            String token,
            Object additionalData,
            ObservableList<Course> courses,
            ObservableList<User> students,
            ListView<Course> coursesListView,
            ListView<User> studentsListView
    ) {
        this.operationType = operationType;
        this.client = client;
        this.token = token;
        this.additionalData = additionalData;
        this.courses = courses;
        this.students = students;
        this.coursesListView = coursesListView;
        this.studentsListView = studentsListView;
    }

    @Override
    public void run() {
        try {
            switch (operationType) {
                case "LOAD_COURSES":
                    loadCourses();
                    break;
                case "LOAD_STUDENTS":
                    loadStudents();
                    break;
                case "ENROLL_STUDENT":
                    enrollStudent();
                    break;
                default:
                    throw new IllegalArgumentException("Operación no válida: " + operationType);
            }
        } catch (IOException e) {
            System.err.println("Error en la operación " + operationType + ": " + e.getMessage());
        }
    }

    private void loadCourses() throws IOException {
        String filter = (String) additionalData; // Filtro de búsqueda
        String url = "http://localhost:5000/api/cursos/busqueda/universidad/" + 
                     User.getInstance().getUniversityID() + "/" + filter + "/" + User.getInstance().getID();
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
            ObservableList<Course> tempCourses = FXCollections.observableArrayList();

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
                tempCourses.add(course);
            }

            Platform.runLater(() -> {
                courses.setAll(tempCourses);
                coursesListView.setItems(courses);
            });
        }
    }

    private void loadStudents() throws IOException {
        String filter = (String) additionalData; // Filtro de búsqueda
        String url = "http://localhost:5000/api/auth/estudiantes/" + 
                     User.getInstance().getUniversityID() + "/" + filter;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar estudiantes: " + response.code());
            }

            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<User> tempStudents = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonStudent = jsonArray.getJSONObject(i);
                User student = new User(
                        jsonStudent.getInt("identificacion"),
                        jsonStudent.getString("pass"),
                        jsonStudent.getString("token"),
                        jsonStudent.getString("nombre"),
                        jsonStudent.getString("apellidos"),
                        jsonStudent.getString("correo"),
                        jsonStudent.getString("rol"),
                        jsonStudent.getInt("universidad_id")
                );
                tempStudents.add(student);
            }

            Platform.runLater(() -> {
                students.setAll(tempStudents);
                studentsListView.setItems(students);
            });
        }
    }

    private void enrollStudent() throws IOException {
        EnrollmentData enrollmentData = (EnrollmentData) additionalData;
        String jsonInputString = String.format(
                "{\"curso_id\": %d, \"usuario_id\": %d}",
                enrollmentData.getCourseId(),
                enrollmentData.getStudentId()
        );

        RequestBody body = RequestBody.create(
                jsonInputString,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("http://localhost:5000/api/inscripciones/inscribir/")
                .addHeader("Authorization", token)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Platform.runLater(() -> System.out.println("Estudiante inscrito con éxito"));
            } else {
                System.err.println("Error en la inscripción: " + response.code());
            }
        }
    }
}
