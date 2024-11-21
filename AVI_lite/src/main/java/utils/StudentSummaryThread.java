/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Assignment;
import utils.Course;
import utils.StudentAssignment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StudentSummaryThread extends Thread {
    private final OkHttpClient client = new OkHttpClient();
    private final String token;
    private User User;
    private String operation;
private int readyAssignments;
    private ObservableList<Course> courses;
    private ObservableList<Integer> totals, committed;
    private ObservableList<Float> percentages;

    private List<Assignment> allAssignments;
    private List<StudentAssignment> allStudentAssignments;

    private Consumer<Boolean> callback;

    public StudentSummaryThread(String token) {
        this.token = token;
        this.courses = FXCollections.observableArrayList();
        this.totals = FXCollections.observableArrayList();
        this.committed = FXCollections.observableArrayList();
        this.percentages = FXCollections.observableArrayList();
        this.allAssignments = new ArrayList<>();
        this.allStudentAssignments = new ArrayList<>();
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setCallback(Consumer<Boolean> callback) {
        this.callback = callback;
    }

    public ObservableList<Course> getCourses() {
        return courses;
    }

    public ObservableList<Integer> getTotals() {
        return totals;
    }

    public ObservableList<Integer> getCommitted() {
        return committed;
    }

    public ObservableList<Float> getPercentages() {
        return percentages;
    }

    @Override
    public void run() {
        boolean success = false;

        try {
            switch (operation) {
                case "load":
                    loadCourses();
                    calculateNotes();
                    success = true;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (callback != null) {
                callback.accept(success);
            }
        }
    }

    private void loadCourses() throws IOException, JSONException {
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
                loadAssignments(course);
            }
        }
    }

    private void loadAssignments(Course course) throws IOException, JSONException {
        int courseId = course.getId();
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/asignaciones/curso/" + courseId)
                .addHeader("Authorization", token)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar asignaciones: " + response.code());
            }

            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);

            int totalAssignments = 0;
          

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

                loadReadyAssignment(assignment);
                allAssignments.add(assignment);
                totalAssignments++;
            }
           committed.add(readyAssignments);
            totals.add(totalAssignments);
            readyAssignments = 0;
            
        }
    }

    private void loadReadyAssignment(Assignment assignment) throws IOException, JSONException {
        
       
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/lista-asignaciones/estudiante/" + User.getInstance().getID())
                .addHeader("Authorization", token)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar asignaciones entregadas: " + response.code());
            }

            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonAssign = jsonArray.getJSONObject(i);

                StudentAssignment studentAssignment = new StudentAssignment(
                        jsonAssign.getInt("id_asignacion"),
                        jsonAssign.getInt("id_estudiante"),
                        jsonAssign.getInt("id_documento"),
                        jsonAssign.getInt("calificado") == 1,
                        jsonAssign.getInt("valor_obtenido"),
                        LocalDateTime.parse(jsonAssign.getString("fecha_entregado").replace("Z", "")),
                        jsonAssign.getString("comentario")
                );

                if (studentAssignment.getIDAssignment() == assignment.getAssignmetnId()) {
                    allStudentAssignments.add(studentAssignment);
                    readyAssignments++;
                }
                
            }
            
            
        }
    }

    private void calculateNotes() {
        percentages.clear();

        for (Course course : courses) {
            int totalCourseValue = 0;
            int obtainedValue = 0;

            for (Assignment assignment : allAssignments) {
                if (assignment.getCourseId() == course.getId()) {
                    totalCourseValue += assignment.getPercentageValue();

                    for (StudentAssignment studentAssignment : allStudentAssignments) {
                        if (studentAssignment.getIDAssignment() == assignment.getAssignmetnId() && studentAssignment.isRated()) {
                            obtainedValue += studentAssignment.getValue();
                        }
                    }
                }
            }

            float percentage = totalCourseValue > 0
                    ? (float) obtainedValue / totalCourseValue * 100
                    : 0;
            percentages.add(percentage);
        }
    }
}
