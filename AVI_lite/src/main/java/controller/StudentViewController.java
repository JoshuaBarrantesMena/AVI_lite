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
public class StudentViewController implements Initializable {
 User User;
    @FXML private Pane viewPane;            // Variable necesaria para obtener el puntero del Panel de la ventana de FXML
    @FXML public static Pane publicPane;
    
    @FXML private Label Student;
    
    @FXML
    private void Enroll() throws IOException{
        Pane enrollView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/StudentEnroll.fxml"));
        publicPane.getChildren().setAll(enrollView);
    }
    
    @FXML
    private void myCourses() throws IOException{
        Pane coursesView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/StudentCourses.fxml"));
        publicPane.getChildren().setAll(coursesView);
    }
    
    @FXML
    private void Summary() throws IOException{
        Pane summaryView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/StudentSummary.fxml"));
        publicPane.getChildren().setAll(summaryView);
    }

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Student.setText(User.getInstance().getLastName()+ '\n' + User.getInstance().getName());
        publicPane = viewPane; // El panel se vuelve publico para poder utilizarse en otras clases
    }
}
