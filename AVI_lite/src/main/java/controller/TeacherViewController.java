/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import utils.User;

/**
 * FXML Controller class
 *
 * @author Josh
 */
public class TeacherViewController implements Initializable {
        User User;
        @FXML private Pane viewPane;        
        @FXML public static Pane publicPane;
        
        @FXML private Label Teacher;
        
        @FXML
        private void viewFacAndDep() throws IOException{
            Pane facAndDepView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherFacAndDep.fxml"));
            publicPane.getChildren().setAll(facAndDepView);
        }
        @FXML
        private void enrollStudents() throws IOException{
            Pane enrollStudentsView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherEnrollStudents.fxml"));
            publicPane.getChildren().setAll(enrollStudentsView);
        }
        @FXML
        private void adminCourses() throws IOException{
            Pane adminCoursesView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherAdminCourses.fxml"));
            publicPane.getChildren().setAll(adminCoursesView);
        }
        @FXML
        private void assignmentsList() throws IOException{
            Pane assignmentsView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherAssignments.fxml"));
            publicPane.getChildren().setAll(assignmentsView);
        }
        
        
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Teacher.setText(User.getInstance().getLastName()+ '\n' + User.getInstance().getName());
        publicPane = viewPane; 
    }
}
