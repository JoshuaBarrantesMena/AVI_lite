package utils;

import controller.StudentCoursesController;
import java.io.IOException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Assignment;
import utils.Course;
import utils.User;

public class StudentCoursesThread extends Thread {
    private final StudentCoursesController controller;
    private final String action;
    private final OkHttpClient client = new OkHttpClient();

    public StudentCoursesThread(StudentCoursesController controller, String action) {
        this.controller = controller;
        this.action = action;
    }

    @Override
    public void run() {
        switch (action) {
            case "loadCourses":
                loadCourses();
                break;
            case "loadAssignments":
                loadAssignments();
                break;
        }
    }

    private void loadCourses() {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        String token = User.getInstance().getToken();
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/inscripciones/cursosDetallados/" + User.getInstance().getID())
                .addHeader("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar cursos: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonUni = jsonArray.getJSONObject(i);
                Course course = new Course(
                        jsonUni.getInt("curso_id"),
                        jsonUni.getInt("carrera_id"),
                        jsonUni.getInt("profesor_id"),
                        jsonUni.getString("nombre"),
                        jsonUni.getString("informacion"),
                        jsonUni.getInt("campos_totales")
                );
                courses.add(course);
            }
            controller.updateCourses(courses);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadAssignments() {
        ObservableList<Assignment> assignments = FXCollections.observableArrayList();
        Course course = controller.CoursesList.getSelectionModel().getSelectedItem();
        if (course == null) {
            return;
        }
        int cursoId = course.getId();
        String token = User.getInstance().getToken();
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/asignaciones/curso/" + cursoId)
                .addHeader("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar asignaciones: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
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
            controller.updateAssignments(assignments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
