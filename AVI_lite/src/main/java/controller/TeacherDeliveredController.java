/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
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
    
    @FXML private Label Name, Description;
    
    @FXML private TextField URL, Date, Value;
    private boolean isDelivered;
    @FXML private TextArea Comment;
    
    @FXML private ComboBox<String> CBFilter;
    
    @FXML private Button OpenFile, RateByIA, Upload, Return;
    
    @FXML private ListView<User> StudentList;
    private ObservableList<User> AllStudents;
    private ObservableList<User> FilteredStudents;
     private ObservableList<StudentAssignment> Delivered;            // Clase faltante, lista de asignaciones entregadas de la asignacion principal
    
    @FXML
    private void ReturnScene() throws IOException{
        currentAssignment = null;
        Pane assignmentsView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherAssignments.fxml"));
        TeacherViewController.publicPane.getChildren().setAll(assignmentsView);
    }
    @FXML
    private void FilterSelected(){
        // IA = null;
        if(CBFilter.getSelectionModel().getSelectedIndex() == 0){
            sortDelivered();
        }else{
            sortUndelivered();
        }
    }
    @FXML
    private void StudentSelected(){
        // IA = null;
        if(!StudentList.getSelectionModel().isEmpty()){
            // Obtener objeto archivo de la asignacion entregada del estudiante seleccionado, guardarlo en currentDocument y cargar el url en 'URL'
            // Si el documento tiene una IA asosiada, cargarla en 'IA, desactivar el boton 'Upload', 'Value' y 'Comment' y cambiar texto de 'RateByIA' por "Calificación Manual"
            // Cargar parametros de la asignacion del estudiante seleccionado en 'Date', 'Value' y 'Comment' 
        }
    }
    @FXML
    private void OpenFile(){
        // DESKTOP LIBRARY
    }
    @FXML
    private void IARate(){
        // Si 'IA' es nula, asignarle un mensaje de IA al archivo (falta plantear la logica), boton 'Upload', 'Value' y 'Comment' se desactivan.
        // Si no es nula, eliminar la relacion entre el documento y la IA en la base de datos, variable 'IA' se vuelve nula, boton 'Upload', 'Value' y 'Comment' se activan
    }
    @FXML
    private void UploadRate() throws IOException{
        if(!Value.getText().equals("")){
            // actualizar la asignacion subida por el estudiante con la calificacion obtenida y el comentario
            
            LocalDateTime now = LocalDateTime.now();
  
  User currentStudent = StudentList.getSelectionModel().getSelectedItem();
  
  
    JSONObject assignmentData = new JSONObject();
    assignmentData.put("id_asignacion", currentAssignment.getAssignmetnId()); // Ajusta según lo que tengas definido
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
        // Obtener todos los estudiantes del curso (currentAssignment.getIDCourse()) y guardarlos en 'StudentList'
        
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
        // Obtener todas las asignaciones entregadas de 'currentAssignment' y guardarlos en 'Delivered'
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
      
        
    
    private void sortDelivered(){
        FilteredStudents.clear();
        for(User currentStudent : AllStudents){                                         // Descomentar el ciclo cuando la clase faltante sea creada y definida
            for(StudentAssignment currentDelivered : Delivered){
                if(currentDelivered.getIDStudent() == currentStudent.getID()){
                    FilteredStudents.add(currentStudent);
                    break;
                }
            }
        }
    }
    
    
     private int searchDocument(int studentId){
     
                                               // Descomentar el ciclo cuando la clase faltante sea creada y definida
            for(StudentAssignment currentDelivered : Delivered){
                if(currentDelivered.getIDStudent() == studentId){
                   
                   return currentDelivered.getIDAssignment();
                }
            }
            return 0;
        
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
