/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.IOException;
import java.time.LocalDateTime;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Clase que maneja operaciones asíncronas relacionadas con entregas de asignaciones.
 */
public class TeacherDeliveredThread extends Thread {
    private final OkHttpClient client = new OkHttpClient();
    private final String token;
    private final int courseId;
    private final int assignmentId;

    private final ObservableList<User> allStudents = FXCollections.observableArrayList();
    private final ObservableList<User> filteredStudents = FXCollections.observableArrayList();
    private final ObservableList<StudentAssignment> deliveredAssignments = FXCollections.observableArrayList();
    private Document currentDocument;

    public TeacherDeliveredThread(String token, int courseId, int assignmentId) {
        this.token = token;
        this.courseId = courseId;
        this.assignmentId = assignmentId;
    }

    public ObservableList<User> getAllStudents() {
        return allStudents;
    }

    public ObservableList<User> getFilteredStudents() {
        return filteredStudents;
    }

    public ObservableList<StudentAssignment> getDeliveredAssignments() {
        return deliveredAssignments;
    }

    public Document getCurrentDocument() {
        return currentDocument;
    }

    // Obtiene la lista de estudiantes inscritos al curso.
    public void getStudents(Runnable onComplete) {
        new Thread(() -> {
            String url = "http://localhost:5000/api/inscripciones/usuarios/" + courseId;
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", token)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Error al obtener estudiantes: " + response.code());
                }

                JSONArray jsonArray = new JSONArray(response.body().string());
                Platform.runLater(() -> {
                    allStudents.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonUser = jsonArray.getJSONObject(i);
                        allStudents.add(new User(
                                jsonUser.getInt("identificacion"),
                                jsonUser.getString("pass"),
                                jsonUser.getString("token"),
                                jsonUser.getString("nombre"),
                                jsonUser.getString("apellidos"),
                                jsonUser.getString("correo"),
                                jsonUser.getString("rol"),
                                jsonUser.getInt("universidad_id")
                        ));
                    }
                    onComplete.run();
                });
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
        }).start();
    }


    public void getDelivered(Runnable onComplete) {
        new Thread(() -> {
            String url = "http://localhost:5000/api/lista-asignaciones/" + assignmentId;
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", token)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Error al obtener entregas: " + response.code());
                }

                JSONArray jsonArray = new JSONArray(response.body().string());
                Platform.runLater(() -> {
                    deliveredAssignments.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonAssign = jsonArray.getJSONObject(i);
                        deliveredAssignments.add(new StudentAssignment(
                                jsonAssign.getInt("id_asignacion"),
                                jsonAssign.getInt("id_estudiante"),
                                jsonAssign.getInt("id_documento"),
                                jsonAssign.getInt("calificado") == 1,
                                jsonAssign.getInt("valor_obtenido"),
                                LocalDateTime.parse(jsonAssign.getString("fecha_entregado").replace("Z", "")),
                                jsonAssign.getString("comentario")
                        ));
                    }
                    onComplete.run();
                });
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public void getDocument(int documentId, Runnable onComplete) {
        new Thread(() -> {
            String url = "http://localhost:5000/api/documentos/" + documentId;
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", token)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Error al obtener el documento: " + response.code());
                }

                JSONObject jsonDoc = new JSONObject(response.body().string());
                currentDocument = new Document(
                        jsonDoc.getInt("id_documento"),
                        jsonDoc.getInt("id_estudiante"),
                        jsonDoc.getString("url"),
                        jsonDoc.getString("hash_")
                );

                Platform.runLater(onComplete);
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

   
    public void uploadRate(StudentAssignment assignment, int value, String comment, Runnable onComplete) {
        new Thread(() -> {
            String url = "http://localhost:5000/api/lista-asignaciones/" + assignment.getIDAssignment();
            JSONObject data = new JSONObject();
            data.put("id_asignacion", assignment.getIDAssignment());
            data.put("id_estudiante", assignment.getIDStudent());
            data.put("calificado", 1);
            data.put("valor_obtenido", value);
            data.put("fecha_entregado", LocalDateTime.now());
            data.put("comentario", comment);

            RequestBody body = RequestBody.create(data.toString(), MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", token)
                    .put(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Error al subir calificación: " + response.code());
                }
                Platform.runLater(onComplete);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

  
    public void iaRate(StudentAssignment assignment, int iaId, Runnable onComplete) {
        new Thread(() -> {
            String url = "http://localhost:5000/api/ia/" + iaId;
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", token)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Error al obtener calificación IA: " + response.code());
                }

                JSONObject jsonResponse = new JSONObject(response.body().string());
                int value = jsonResponse.getInt("valor");
                String message = jsonResponse.getString("respuesta");

                uploadRate(assignment, value, message, onComplete);
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}
