/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;



import controller.AdminCoursesController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Career;
import utils.Course;
import utils.User;

import java.io.IOException;

public class AdminCoursesThread extends Thread {
    private final String task;
    private final AdminCoursesController controller;
    private final String token;
    private String requestBody;
    public int courseId;

    // Constructor para tareas generales
    public AdminCoursesThread(String task, AdminCoursesController controller, String token) {
        this.task = task;
        this.controller = controller;
        this.token = token;
    }

    // Constructor para tareas que incluyen un cuerpo de solicitud
    public AdminCoursesThread(String task, AdminCoursesController controller, String token, String requestBody) {
        this(task, controller, token);
        this.requestBody = requestBody;
    }

    // Constructor para operaciones específicas en cursos
    public AdminCoursesThread(String task, AdminCoursesController controller, String token, int courseId) {
        this(task, controller, token);
        this.courseId = courseId;
    }

    @Override
    public void run() {
        switch (task) {
            case "loadTeachers":
                loadTeachers();
                break;
            case "loadCareers":
                loadCareers();
                break;
            case "loadCourses":
                loadCourses();
                break;
            case "addCourse":
                addOrUpdateCourse("add");
                break;
            case "updateCourse":
                addOrUpdateCourse("update");
                break;
            case "deleteCourse":
                deleteCourse();
                break;
            default:
                System.err.println("Tarea desconocida: " + task);
        }
    }

    private void loadTeachers() {
        ObservableList<User> teachers = FXCollections.observableArrayList();
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/auth/role/Profesor")
                .addHeader("Authorization", token)
                .get()
                .build();

        try (Response response = new OkHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar profesores: " + response.code());
            }
            JSONArray jsonArray = new JSONArray(response.body().string());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonUser = jsonArray.getJSONObject(i);
                User teacher = new User(
                        jsonUser.getInt("identificacion"),
                        jsonUser.getString("token"),
                        jsonUser.getString("nombre"),
                        jsonUser.getString("apellidos"),
                        jsonUser.getString("correo"),
                        jsonUser.getString("rol")
                );
                teachers.add(teacher);
            }
            Platform.runLater(() -> controller.updateTeachers(teachers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCareers() {
        ObservableList<Career> careers = FXCollections.observableArrayList();
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/carreras")
                .addHeader("Authorization", token)
                .get()
                .build();

        try (Response response = new OkHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar carreras: " + response.code());
            }
            JSONArray jsonArray = new JSONArray(response.body().string());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonCareer = jsonArray.getJSONObject(i);
                Career career = new Career(
                        jsonCareer.getInt("carrera_id"),
                        jsonCareer.getInt("departamento_id"),
                        jsonCareer.getString("nombre")
                );
                careers.add(career);
            }
            Platform.runLater(() -> controller.updateCareers(careers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCourses() {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/cursos")
                .addHeader("Authorization", token)
                .get()
                .build();

        try (Response response = new OkHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar cursos: " + response.code());
            }
            JSONArray jsonArray = new JSONArray(response.body().string());
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
            Platform.runLater(() -> controller.updateCourses(courses));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addOrUpdateCourse(String operation) {
        RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8"));
        String url = "http://localhost:5000/api/cursos/" + (operation.equals("update") ? courseId : "");
        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token);

        if (operation.equals("add")) {
            builder.post(body);
        } else {
            builder.put(body);
        }

        try (Response response = new OkHttpClient().newCall(builder.build()).execute()) {
            if (response.isSuccessful()) {
                Platform.runLater(controller::refreshCourses);
            } else {
                System.err.println("Error en " + operation + " curso: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteCourse() {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/cursos/" + courseId)
                .addHeader("Authorization", token)
                .delete()
                .build();

        try (Response response = new OkHttpClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                Platform.runLater(controller::refreshCourses);
            } else {
                System.err.println("Error al eliminar curso: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

