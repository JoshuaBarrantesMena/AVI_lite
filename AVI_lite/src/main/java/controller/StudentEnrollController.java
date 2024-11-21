/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import utils.Course;
import utils.StudentEnrollThread;
import utils.User;

/**
 * FXML Controller class.
 */
public class StudentEnrollController implements Initializable {
    private String token;
    private ObservableList<Course> Courses;

    @FXML private TextField State;
    @FXML private TextArea Information;
    @FXML private Button e;
    @FXML private TableView<Course> CoursesList;
    @FXML private TableColumn<Course, String> NameColumn;
    @FXML private TableColumn<Course, Integer> FieldsColumn;

    @FXML
    private void courseSelect() {
        if (CoursesList.getSelectionModel().getSelectedIndex() != -1) {
            Course course = CoursesList.getSelectionModel().getSelectedItem();
            Information.setText(course.getInformation());
            e.setDisable(false);
        }
    }

    @FXML
    private void enrollStudent() {
        Course enrollCourse = CoursesList.getSelectionModel().getSelectedItem();
        if (enrollCourse != null) {
            StudentEnrollThread enrollThread = new StudentEnrollThread(token, enrollCourse);
            enrollThread.start();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        e.setDisable(true);
        token = User.getInstance().getToken();
        Courses = FXCollections.observableArrayList();

        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        FieldsColumn.setCellValueFactory(new PropertyValueFactory<>("totalFields"));
        CoursesList.setItems(Courses);

        // Ejecutar la carga de cursos en un hilo
        StudentEnrollThread loadCoursesThread = new StudentEnrollThread(token, Courses);
        loadCoursesThread.start();
    }
}

