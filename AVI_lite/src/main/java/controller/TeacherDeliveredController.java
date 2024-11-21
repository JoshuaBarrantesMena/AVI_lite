/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.*;

/**
 * FXML Controller class
 *
 * @author Josh
 */
public class TeacherDeliveredController implements Initializable {
private final OkHttpClient client = new OkHttpClient();
    public static Assignment currentAssignment;
    //private IAmessage IA;
    // private Document currentFile;
    private User User;
    private String token;

    public static Assignment getCurrentAssignment() {
        return currentAssignment;
    }

    public static void setCurrentAssignment(Assignment currentAssignment) {
        TeacherDeliveredController.currentAssignment = currentAssignment;
    }
    private Document currentDocument;
    @FXML private Label Name, Description;
    
    @FXML private TextField URL, Date, Value;
    private boolean isDelivered;
    @FXML private TextArea Comment;
    
    @FXML private ComboBox<String> CBFilter;
    
    @FXML private Button OpenFile, RateByIA, Upload, Return;
    
    @FXML private ListView<User> StudentList;
    private ObservableList<User> AllStudents;
    private ObservableList<User> FilteredStudents;
     private ObservableList<StudentAssignment> Delivered;            
    
    @FXML
    private void ReturnScene() throws IOException{
        currentAssignment = null;
        Pane assignmentsView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherAssignments.fxml"));
        TeacherViewController.publicPane.getChildren().setAll(assignmentsView);
    }
    @FXML
    private void FilterSelected(){
   
        if(CBFilter.getSelectionModel().getSelectedIndex() == 0){
            sortDelivered();
        }else{
            sortUndelivered();
        }
    }
    @FXML
    private void StudentSelected(){
       
        if(!StudentList.getSelectionModel().isEmpty()){
            User student = StudentList.getSelectionModel().getSelectedItem();
            StudentAssignment fillAssignment = searchStudentAssignment(student.getID());
            if(!(fillAssignment==null)){
             
               Date.setText(fillAssignment.formatDate());
             Value.setText(Integer.toString(fillAssignment.getValue()));
             Comment.setText(fillAssignment.getComment());
             
             getDocument(fillAssignment.getIDDoc());
             
             URL.setText(currentDocument.getUrl());
           
            
            
            }
        }
    }
   
    @FXML
    private void IARate() throws IOException{
        
        
        User student = StudentList.getSelectionModel().getSelectedItem();
        
         StudentAssignment fillAssignment = searchStudentAssignment(student.getID());
       int IAId = transformToRange(fillAssignment.hashCode());
       IAMessage ia = getIa(IAId);
        
        
         
       

        if(!(fillAssignment.isRated())){
        LocalDateTime now = LocalDateTime.now();
  
  User currentStudent = StudentList.getSelectionModel().getSelectedItem();
  
  
    JSONObject assignmentData = new JSONObject();
    assignmentData.put("id_asignacion", currentAssignment.getAssignmetnId()); 
    assignmentData.put("id_estudiante", currentStudent.getID());
    assignmentData.put("calificado", 1);
    assignmentData.put("valor_obtenido", ia.getValue() );
    assignmentData.put("fecha_entregado", now);
    assignmentData.put("comentario",ia.getMessage() );


              RequestBody body = RequestBody.create(
                assignmentData.toString(),
                MediaType.get("application/json; charset=utf-8")
        );
         

        Request request = new Request.Builder()
                .url("http://localhost:5000/api/lista-asignaciones/" + searchDocument(currentStudent.getID()))
                .addHeader("Authorization", token)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
              
            } else {
                throw new IOException("Error al actualizar la asignación: " + response.code());
            }
        }   catch (IOException ex) {
                ex.printStackTrace();
            }
            
            
            
        }
        
        ReturnScene();
        
        
        }
      
       
    
    
    public int transformToRange(long bytes) {
    int result = (int) (bytes % 10);
    
    if (result == 0) {
        result = 10;
    }
    
    return result;
}

    
    @FXML
    private void UploadRate() throws IOException{
        
        if(!Value.getText().equals("")){
          
            
            LocalDateTime now = LocalDateTime.now();
  
  User currentStudent = StudentList.getSelectionModel().getSelectedItem();
  
  
    JSONObject assignmentData = new JSONObject();
    assignmentData.put("id_asignacion", currentAssignment.getAssignmetnId()); 
    assignmentData.put("id_estudiante", currentStudent.getID());
    assignmentData.put("calificado", 1);
    assignmentData.put("valor_obtenido", String.valueOf(Value.getText()));
    assignmentData.put("fecha_entregado", now);
    assignmentData.put("comentario",Comment.getText() );


              RequestBody body = RequestBody.create(
                assignmentData.toString(),
                MediaType.get("application/json; charset=utf-8")
        );
         

        Request request = new Request.Builder()
                .url("http://localhost:5000/api/lista-asignaciones/" + searchDocument(currentStudent.getID()))
                .addHeader("Authorization", token)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
              
            } else {
                throw new IOException("Error al actualizar la asignación: " + response.code());
            }
        }   catch (IOException ex) {
                ex.printStackTrace();
            }
            
            
            
        }
        
        ReturnScene();
        
        
    }
    
    private void getStudents(){
        
        
              Request request = new Request.Builder()
                .url("http://localhost:5000/api/inscripciones/usuarios/" + currentAssignment.getCourseId())
                .addHeader("Authorization", token)
                .get()
                .build();
          try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar usuarios: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonUser = jsonArray.getJSONObject(i);
                User newUser = new User(
                        jsonUser.getInt("identificacion"),
                        jsonUser.getString("pass"),
                        jsonUser.getString("token"),
                        jsonUser.getString("nombre"),
                        jsonUser.getString("apellidos"),
                        jsonUser.getString("correo"),
                        jsonUser.getString("rol"),
                        jsonUser.getInt("universidad_id")
                );
                AllStudents.add(newUser);
            }
        
        
        
        
        
        
        
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }
    private void getDelivered(){
       
         Request request = new Request.Builder()
                .url("http://localhost:5000/api/lista-asignaciones/" + currentAssignment.getAssignmetnId())
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

                Delivered.add(studentAssignmentSearch);
                    
               
                }
            
            
            } catch (IOException ex) {
        ex.printStackTrace();
    }
        }
   
 private IAMessage getIa(int iaId) {
    String url = "http://localhost:5000/api/ia/" + iaId;  
    Request request = new Request.Builder()
            .url(url)
            .addHeader("Authorization", token)  
            .get()
            .build();

    try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
            throw new IOException("Error al obtener el documento IA: " + response.code());
        }

      
        String responseData = response.body().string();
        JSONObject jsonIaDocument = new JSONObject(responseData);

       
        IAMessage iaDocument = new IAMessage(
                jsonIaDocument.getInt("id_ia"),
                jsonIaDocument.getInt("valor"),
                jsonIaDocument.getString("respuesta")
        );

        
       return iaDocument;

    } catch (IOException | JSONException ex) {
        ex.printStackTrace();
    }
    return null;
}
    
     private int searchDocument(int studentId){
     

            for(StudentAssignment currentDelivered : Delivered){
                if(currentDelivered.getIDStudent() == studentId){
                   
                   return currentDelivered.getIDAssignment();
                }
            }
            return 0;
        
    }
      private StudentAssignment searchStudentAssignment(int studentId){
     

            for(StudentAssignment currentDelivered : Delivered){
                if(currentDelivered.getIDStudent() == studentId){
                   
                   return currentDelivered;
                }
            }
    return null;
           
        
    }
     
    private void sortUndelivered(){
        FilteredStudents.clear();
        boolean isNotDelivered = true;
        for(User currentStudent : AllStudents){                                         // Descomentar cuando la clase faltante sea creada y definida
            for(StudentAssignment currentDelivered : Delivered){
                if(currentDelivered.getIDStudent() == currentStudent.getID()){
                    isNotDelivered = false;
                    break;
                }
            }
            if(isNotDelivered){
                FilteredStudents.add(currentStudent);
            }
        }
    }
    
      private void getDocument(int documentId) {
    String url = "http://localhost:5000/api/documentos/" + documentId; // URL base de la API
    Request request = new Request.Builder()
            .url(url)
            .addHeader("Authorization", token) 
            .get()
            .build();

    try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
            throw new IOException("Error al obtener el documento: " + response.code());
        }

      
        String responseData = response.body().string();
        JSONObject jsonDocument = new JSONObject(responseData);

      
        Document document = new Document(
                jsonDocument.getInt("id_documento"),
                jsonDocument.getInt("id_estudiante"),
                jsonDocument.getString("url"),
                jsonDocument.getString("hash_")
        );

        currentDocument = document;

    } catch (IOException | JSONException ex) {
        ex.printStackTrace();
    }
}

        
    
    private void sortDelivered(){
        FilteredStudents.clear();
        for(User currentStudent : AllStudents){                                        
            for(StudentAssignment currentDelivered : Delivered){
                if(currentDelivered.getIDStudent() == currentStudent.getID()){
                    FilteredStudents.add(currentStudent);
                    break;
                }
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        token = User.getInstance().getToken();
        
        AllStudents = FXCollections.observableArrayList();
        FilteredStudents = FXCollections.observableArrayList();
        Delivered =FXCollections.observableArrayList();
        
        getStudents();
        getDelivered();
        sortDelivered();
        
        StudentList.setItems(FilteredStudents);
        // AllDelivered = FXCollecions.observableArrayList();
        
        CBFilter.getItems().setAll("Entregados", "Sin Entregar");
        CBFilter.getSelectionModel().select(0);
        Name.setText(currentAssignment.getTitle());
        Description.setText(currentAssignment.getDescription());
        
        
    }
}
