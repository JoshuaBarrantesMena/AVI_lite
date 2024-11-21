/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import utils.Assignment;
import utils.Course;
import utils.Document;
import utils.StudentAssignment;
import utils.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import utils.StudentFileThread;

public class StudentFileController implements Initializable {
    private Assignment AssignmentInstance;
    private StudentAssignment studentAssignment;
    private User User;
    private String token;
    private Course CourseInstance;
    private Document Document;

    @FXML private TextField Value, Deadline, Lastdate, State, ValueObtained, FileURL;
    @FXML private TextArea Comment;
    @FXML private Button upload, undo, Submit, Back;
    @FXML private Label Course, Assignment, Description;

    @FXML
    private void getFile() {
        
        if(FileURL.getText().isEmpty()){
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(FileURL.getScene().getWindow());
        if (selectedFile != null) {
            FileURL.setText(selectedFile.getAbsolutePath());
            
            Document.getInstance().setUrl(selectedFile.getAbsolutePath());
            Document.getInstance().setIDStudent(User.getInstance().getID());
            Document.getInstance().setHashcode(Long.toString(selectedFile.getTotalSpace()));
            
            
            
        } else {
            FileURL.setText("No se seleccionó ningún archivo.");
        }
    }
    }

    @FXML
    private void ReturnScene() throws IOException {
        Pane pane = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/StudentCourses.fxml"));
        StudentViewController.publicPane.getChildren().setAll(pane);
    }

    private void loadAssignmentInfo() {
        StudentFileThread thread = new StudentFileThread(User.getInstance().getToken());
        thread.setAssignmentId(AssignmentInstance.getInstance().getAssignmetnId());
        thread.setOperation("load");
        thread.setCallback(success -> Platform.runLater(() -> {
            if (success) {
                studentAssignment = thread.getStudentAssignment();
                Document = thread.getDocument();

                Assignment.setText(AssignmentInstance.getInstance().getTitle());
                Course.setText(CourseInstance.getInstance().getName());
                Description.setText(AssignmentInstance.getInstance().getDescription());
                Value.setText(Integer.toString(AssignmentInstance.getInstance().getPercentageValue()));
                Comment.setText(studentAssignment.getComment());
                Lastdate.setText(studentAssignment.formatDate());
                Deadline.setText(AssignmentInstance.getInstance().formatDate());
                State.setText(studentAssignment.isRated() ? "Calificado" : "No calificado");
                FileURL.setText(Document.getUrl());
                if (studentAssignment.isRated()) {
                    ValueObtained.setText(Integer.toString(studentAssignment.getValue()));
                    
                }
            } else {
                Assignment.setText(AssignmentInstance.getInstance().getTitle());
                Value.setText(Integer.toString(AssignmentInstance.getInstance().getPercentageValue()));
                Description.setText(AssignmentInstance.getInstance().getDescription());
                Course.setText(CourseInstance.getInstance().getName());
                Deadline.setText(AssignmentInstance.getInstance().formatDate());
                Submit.setDisable(false);
                upload.setDisable(false);
                undo.setDisable(true);
            }
        }));
        thread.start();
    }

    @FXML
    private void submitTask(ActionEvent event) {
        if (FileURL.getText().isEmpty()|| Comment.getText().isEmpty()) {
        showAlert("Campos incompletos", "Debe seleccionar un archivo.");
        return; // No continuar si faltan datos importantes
    }

    // Deshabilitar el botón de enviar mientras se realiza la operación
    Submit.setDisable(true);
 studentAssignment.getInstance().setComment(Comment.getText());
    // Crear una instancia de StudentFileThread para enviar la tarea
    StudentFileThread thread = new StudentFileThread(User.getInstance().getToken());
    thread.setAssignmentId(AssignmentInstance.getInstance().getAssignmetnId());
    thread.setOperation("submit");

    // Establecer el callback para manejar el resultado de la operación
    thread.setCallback(success -> Platform.runLater(() -> {
        if (success) {
            try {
                ReturnScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Submit.setDisable(true);
            undo.setDisable(false);
            showAlert("Éxito", "La asignación se ha enviado correctamente.");
            
        } else {
            // Si hubo un error al enviar la tarea
            showAlert("Error", "No se pudo enviar la asignación. Intente nuevamente.");
            Submit.setDisable(false); // Habilitar nuevamente el botón para reintentar
        }
    }));

  
    thread.start();
    }

    @FXML
    private void undoTask(ActionEvent event) {
        StudentFileThread thread = new StudentFileThread(User.getInstance().getToken());
        thread.setAssignmentId(studentAssignment.getIDAssignment());
        thread.setOperation("undo");
        thread.setCallback(success -> Platform.runLater(() -> {
            if (success) {
                try {
                    ReturnScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Error", "No se pudo deshacer la entrega.");
            }
        }));
        thread.start();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        upload.setDisable(true);
        Submit.setDisable(true);
        undo.setDisable(false);
        token = User.getInstance().getToken();
        loadAssignmentInfo();
    }
}
