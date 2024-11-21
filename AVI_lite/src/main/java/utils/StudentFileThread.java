package utils;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Document;
import utils.StudentAssignment;
 import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import okhttp3.RequestBody;

public class StudentFileThread extends Thread {
    private final OkHttpClient client = new OkHttpClient();
    private final String token;
    private int assignmentId;
    private int documentId;

    private StudentAssignment studentAssignment;
      private StudentAssignment studentAssignmentInstance;
    private Document document;
    private boolean success;
    private Consumer<Boolean> callback;

    private String operation; 

    public StudentFileThread(String token) {
        this.token = token;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public void setCallback(Consumer<Boolean> callback) {
        this.callback = callback;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public StudentAssignment getStudentAssignment() {
        return studentAssignment;
    }

    public Document getDocument() {
        return document;
    }

    public boolean isSuccess() {
        return success;
    }
   

    @Override
    public void run() {
        try {
            switch (operation) {
                case "load":
                    if (loadReadyAssignment()) {
                        loadDocument();
                        success = true;
                    } else {
                        success = false;
                    }
                    break;
                case "submit":
                    success = submitTask();
                    break;
                case "undo":
                    success = undoTask();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        } finally {
            if (callback != null) {
                callback.accept(success);
            }
        }
    }

    private boolean loadReadyAssignment() throws IOException, JSONException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/lista-asignaciones/estudiante/" + User.getInstance().getID())
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

                StudentAssignment studentAssignmentSearch = new StudentAssignment(
                        jsonAssign.getInt("id_asignacion"),
                        jsonAssign.getInt("id_estudiante"),
                        jsonAssign.getInt("id_documento"),
                        jsonAssign.getInt("calificado") == 1,
                        jsonAssign.getInt("valor_obtenido"),
                        LocalDateTime.parse(jsonAssign.getString("fecha_entregado").replace("Z", "")),
                        jsonAssign.getString("comentario")
                );

                if (studentAssignmentSearch.getIDAssignment() == assignmentId) {
                    studentAssignment = studentAssignmentSearch;
                    documentId = studentAssignmentSearch.getIDDoc();
                    return true;
                }
            }
        }
        return false;
    }

    private void loadDocument() throws IOException, JSONException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/documentos/" + documentId)
                .addHeader("Authorization", token)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar el documento: " + response.code());
            }

            String responseData = response.body().string();
            JSONObject jsonDocument = new JSONObject(responseData);

            document = new Document(
                    jsonDocument.getInt("id_documento"),
                    jsonDocument.getInt("id_estudiante"),
                    jsonDocument.getString("url"),
                    jsonDocument.getString("hash_")
            );
         
            
            
        }
    }

 
       
       private boolean submitTask() throws IOException {
    // 1. Crear el documento usando los datos proporcionados
    
    
    
 // Crear el documento
if (document.getInstance() == null) {
    // Si no hay documento, no podemos continuar
    return false;
}

// Crear el objeto JSON para el documento
JSONObject documentData = new JSONObject();
documentData.put("id_estudiante", document.getInstance().getIDStudent());
documentData.put("url", document.getInstance().getUrl());
documentData.put("hash_", document.getInstance().getHashcode());

RequestBody documentRequestBody = RequestBody.create(documentData.toString(), okhttp3.MediaType.parse("application/json"));

Request documentRequest = new Request.Builder()
        .url("http://localhost:5000/api/documentos")
        .addHeader("Authorization", token)
        .post(documentRequestBody)
        .build();

try (Response documentResponse = client.newCall(documentRequest).execute()) {
    if (!documentResponse.isSuccessful()) {
        throw new IOException("Error al enviar el documento: " + documentResponse.code());
    }

    String documentResponseData = documentResponse.body().string();
    JSONObject documentJson = new JSONObject(documentResponseData);

  int message = documentJson.getInt("message");
    System.out.println(message);
   

  
    LocalDateTime now = LocalDateTime.now();
  

    JSONObject assignmentData = new JSONObject();
    assignmentData.put("id_asignacion", assignmentId); // Ajusta según lo que tengas definido
    assignmentData.put("id_estudiante", User.getInstance().getID());
    assignmentData.put("id_documento", message);
    assignmentData.put("calificado", 0);
    assignmentData.put("valor_obtenido", 0);
    assignmentData.put("fecha_entregado", now);
    assignmentData.put("comentario",studentAssignmentInstance.getInstance().getComment() );

    RequestBody assignmentRequestBody = RequestBody.create(assignmentData.toString(), okhttp3.MediaType.parse("application/json"));

    Request assignmentRequest = new Request.Builder()
            .url("http://localhost:5000/api/lista-asignaciones")
            .addHeader("Authorization", token)
            .post(assignmentRequestBody)
            .build();

    try (Response assignmentResponse = client.newCall(assignmentRequest).execute()) {
        if (!assignmentResponse.isSuccessful()) {
             return true;
        }
 return true;
        // Si todo es exitoso
       
    }
}

}
       
   
   


    private boolean undoTask() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/lista-asignaciones/" + assignmentId)
                .addHeader("Authorization", token)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }
}
