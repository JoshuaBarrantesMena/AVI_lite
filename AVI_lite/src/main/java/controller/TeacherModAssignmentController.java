/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import utils.*;

/**
 * FXML Controller class
 *
 * @author Josh
 */
public class TeacherModAssignmentController implements Initializable {
private final OkHttpClient client = new OkHttpClient();
    public static Assignment currentAssignment;
    private TelegramNotification telegramNotification = new TelegramNotification(
   "7696769229:AAENvJtKTOHK5ZbYbsbr2dIJtF5yLcShPGk",1368935005,1599144647
    );
    private User User;
    private String token;
     private Course Course;
    @FXML private Button Confirm, Cancel, Return;
    
    @FXML private TextField Name, Value;
    
    @FXML private DatePicker Date;
    
    @FXML private TextArea Description;
    
    @FXML private HBox ReturnHBox;
    
    @FXML
    private void ReturnScene() throws IOException{
        currentAssignment = null;
        Pane assignmentView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherAssignments.fxml"));
        TeacherViewController.publicPane.getChildren().setAll(assignmentView);
    }
    @FXML
    private void DeleteCancel() throws IOException{
        if(currentAssignment != null){
            deleteAssignment();
        }
        ReturnScene();
    }
    @FXML
    private void ConfirmUpdate(){
        if(currentAssignment != null){
            
             
            updateAssignment();
            
        }else{
            
            
            
                  LocalDate fechaLimite = Date.getValue();
   
             telegramNotification.notifyStudentAssignment(Name.getText(), fechaLimite);
            
          createAssignment();
    }
    }
    @FXML
    private void TypedCheck(){
        if(!Name.getText().equals("") && !Value.getText().equals("") && Date.getValue() != null){
            Confirm.setDisable(false);
        }else{
            Confirm.setDisable(true);
        }
    }
    
    private void checkAssignment(){
        if(currentAssignment != null){
            Name.setText(currentAssignment.getTitle());
            Value.setText("" + currentAssignment.getPercentageValue());
            Description.setText(currentAssignment.getDescription());
            
            try { 
                LocalDateTime fechaCompleta = currentAssignment.getLimitDate();
LocalDate fecha = fechaCompleta.toLocalDate(); 
Date.setValue(fecha);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha no válido: " + e.getMessage());
            }
            
            Confirm.setText("Actualizar");
            Cancel.setText("Eliminar");
            ReturnHBox.setDisable(false);
            ReturnHBox.setVisible(true);
            ReturnHBox.setPrefWidth(140);
        }else{
            Confirm.setText("Confirmar");
            Cancel.setText("Cancelar");
        }
    }
    
 private void createAssignment() {
    String titulo = Name.getText();
    int idProfesor = User.getInstance().getID();
    int idCurso = Course.getInstance().getId();
    int valorPorcentual = Integer.parseInt(Value.getText());
    LocalDate fechaLimite = Date.getValue();
    String descripcion = Description.getText();
    String fechaLimiteISO = fechaLimite.toString() + "T00:00:00";

    JSONObject assignmentJson = new JSONObject();
    try {
        assignmentJson.put("titulo", titulo);
        assignmentJson.put("id_profesor", idProfesor);
        assignmentJson.put("id_curso", idCurso);
        assignmentJson.put("valor_porcentual", valorPorcentual);
        assignmentJson.put("fecha_limite", fechaLimiteISO);
        assignmentJson.put("descripcion", descripcion);
    } catch (Exception e) {
        e.printStackTrace();
        return;
    }

    TeacherModAssignmentThread thread = new TeacherModAssignmentThread(
            "CREATE",
            client,
            token,
            assignmentJson,
            0,
            "http://localhost:5000/api/asignaciones",
            () -> System.out.println("Asignación creada exitosamente.")
    );
    thread.start();
}
private void updateAssignment() {
    String titulo = Name.getText();
    int idProfesor = User.getInstance().getID();
    int idCurso = Course.getInstance().getId();
    int valorPorcentual = Integer.parseInt(Value.getText());
    LocalDate fechaLimite = Date.getValue();
    String descripcion = Description.getText();
    String fechaLimiteISO = fechaLimite.toString() + "T00:00:00";

    JSONObject assignmentJson = new JSONObject();
    try {
        assignmentJson.put("titulo", titulo);
        assignmentJson.put("id_profesor", idProfesor);
        assignmentJson.put("id_curso", idCurso);
        assignmentJson.put("valor_porcentual", valorPorcentual);
        assignmentJson.put("fecha_limite", fechaLimiteISO);
        assignmentJson.put("descripcion", descripcion);
    } catch (Exception e) {
        e.printStackTrace();
        return;
    }

    TeacherModAssignmentThread thread = new TeacherModAssignmentThread(
            "UPDATE",
            client,
            token,
            assignmentJson,
            currentAssignment.getAssignmetnId(),
            "http://localhost:5000/api/asignaciones",
            () -> System.out.println("Asignación actualizada exitosamente.")
    );
    thread.start();
}
private void deleteAssignment() {
    TeacherModAssignmentThread thread = new TeacherModAssignmentThread(
            "DELETE",
            client,
            token,
            null,
            currentAssignment.getAssignmetnId(),
            "http://localhost:5000/api/asignaciones",
            () -> System.out.println("Asignación eliminada exitosamente.")
    );
    thread.start();
}

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        token = User.getInstance().getToken();
        
        ReturnHBox.setDisable(true);
        ReturnHBox.setVisible(false);
        ReturnHBox.setPrefWidth(0);
        checkAssignment();
    }
}
