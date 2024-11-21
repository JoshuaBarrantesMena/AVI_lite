/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.TeacherModAssignmentController;
import controller.TeacherViewController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
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
public class TeacherAssignmentsController implements Initializable {
    private User User;
    private String token;
    private Course Course;
    private final OkHttpClient client = new OkHttpClient();
    @FXML private Button Add, Edit, Delivered;
    private TeacherDeliveredController controller;
    
    @FXML private ListView<Course> CoursesList;
    private ObservableList<Course> Courses;
    
    @FXML private ListView<Assignment> AssignmentsList;
    private ObservableList<Assignment> Assignments;
    
    @FXML
    private void AddAssignment() throws IOException{
        TeacherModAssignmentController.currentAssignment = null;
        Pane modAssignment = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherModAssignment.fxml"));
        TeacherViewController.publicPane.getChildren().setAll(modAssignment);
    }
    @FXML
    private void ModifyAssignment() throws IOException{
        if(!AssignmentsList.getSelectionModel().isSelected(-1)){
            TeacherModAssignmentController.currentAssignment = AssignmentsList.getSelectionModel().getSelectedItem();
            Pane modAssignment = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherModAssignment.fxml"));
            TeacherViewController.publicPane.getChildren().setAll(modAssignment);
        }
    }
    @FXML
    private void ViewDelivered() throws IOException{
        if(!AssignmentsList.getSelectionModel().isSelected(-1)){
           Assignment nuevo = AssignmentsList.getSelectionModel().getSelectedItem();
           controller.setCurrentAssignment(nuevo); 
           Pane modAssignment = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherDelivered.fxml"));
            TeacherViewController.publicPane.getChildren().setAll(modAssignment);
        }
    }
  
      

@FXML
private void CourseSelected() {
    if (!CoursesList.getSelectionModel().isSelected(-1)) {
        AssignmentsList.getItems().clear();
        Edit.setDisable(true);
        Delivered.setDisable(true);

        Course course = CoursesList.getSelectionModel().getSelectedItem();
        Course.getInstance().setId(course.getId());

        TeacherAssignmentsThread loadAssignmentsThread = new TeacherAssignmentsThread(
                "LOAD_ASSIGNMENTS", client, token, course.getId(), null, AssignmentsList
        );
        loadAssignmentsThread.start();
    } else {
        CoursesList.getItems().clear();
        AssignmentsList.getItems().clear();
        Edit.setDisable(true);
        Delivered.setDisable(true);
        Course.getInstance().setId(0);
    }
}

        
        
    
    @FXML
    private void AssignmentSelected(){
        if(!AssignmentsList.getSelectionModel().isSelected(-1)){
            Edit.setDisable(false);
            Delivered.setDisable(false);
        }
    }
    
   
   
    
    
    @Override
public void initialize(URL url, ResourceBundle rb) {
    token = User.getInstance().getToken();
    Courses = FXCollections.observableArrayList();
    Assignments = FXCollections.observableArrayList();

    TeacherAssignmentsThread loadCoursesThread = new TeacherAssignmentsThread(
            "LOAD_COURSES", client, token, null, CoursesList, null
    );
    loadCoursesThread.start();
}
}
