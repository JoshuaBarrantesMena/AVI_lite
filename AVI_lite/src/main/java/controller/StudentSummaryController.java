/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.*;

/**
 * FXML Controller class
 *
 * @author Josh
 */
public class StudentSummaryController implements Initializable {
private User User;
private String token;
private int readyCounter;
private int notes;
Course Course;

 private final OkHttpClient client = new OkHttpClient();
    @FXML private ListView<Course> CourseList;          // Crear funcion "toString" en la clase
    @FXML private ListView<Integer> TotalList, CommitedList;
    @FXML private ListView<Float> PercentageList;
    
    private ObservableList<Course> Courses;
    private ObservableList<Integer> Totals, Commited;
    private ObservableList<Float> Percentages;
    
    private List<Assignment> AllAssignments;
    private List<StudentAssignment> AllStudentAssignments;
    
    private void getStudentData(){
        // Obtener los cursos del estudiante y guardarlos en 'CoursesList'
        loadStudentData();
        
    }



 




private static boolean toBoolean(int value) {
    return value == 1;
}
    
    
private void loadStudentData() {
    StudentSummaryThread thread = new StudentSummaryThread(User.getInstance().getToken());
    thread.setOperation("load");
    thread.setCallback(success -> Platform.runLater(() -> {
        if (success) {
            CourseList.setItems(thread.getCourses());
            TotalList.setItems(thread.getTotals());
            CommitedList.setItems(thread.getCommitted());
            PercentageList.setItems(thread.getPercentages());
        } else {
            
        }
    }));
    thread.start();
}

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        token = User.getInstance().getToken();
        Courses = FXCollections.observableArrayList();
        Totals = FXCollections.observableArrayList();
        Commited = FXCollections.observableArrayList();
        Percentages = FXCollections.observableArrayList();
       AllAssignments = new ArrayList<>();
AllStudentAssignments = new ArrayList<>();
    
    
     
         CommitedList.setItems(Commited);
    
    
    
    getStudentData();
    
    
        TotalList.setItems(Totals);
       
        PercentageList.setItems(Percentages);
        
        
    }
}
