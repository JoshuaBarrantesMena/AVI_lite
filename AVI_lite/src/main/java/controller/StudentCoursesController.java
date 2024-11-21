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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.*;
import static utils.User.getInstance;

/**
 * FXML Controller class
 *
 * @author Josh
 */
public class StudentCoursesController implements Initializable {
    private User User;
    private String token;
    private Course Course;

    @FXML
    Button Details;

    @FXML
    public ListView<Course> CoursesList;
    @FXML
    private ListView<Assignment> AssignmentsList;

    private ObservableList<Course> Courses;
    private ObservableList<Assignment> Assignments;

    @FXML
    private void viewAssignmentDetails() throws IOException {
        Assignment Assignment = AssignmentsList.getSelectionModel().getSelectedItem();
        Course = CoursesList.getSelectionModel().getSelectedItem();
        Assignment.getInstance().setAssignmetnId(Assignment.getAssignmetnId());
        Assignment.getInstance().setCourseId(Assignment.getCourseId());
        Assignment.getInstance().setDescription(Assignment.getDescription());
        Assignment.getInstance().setLimitDate(Assignment.getLimitDate());
        Assignment.getInstance().setTeacherId(Assignment.getTeacherId());
        Assignment.getInstance().setTitle(Assignment.getTitle());
        Assignment.getInstance().setPercentageValue(Assignment.getPercentageValue());
        Course.getInstance().setName(Course.getName());

        Pane assignmentFilepane = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/StudentFile.fxml"));
        StudentViewController.publicPane.getChildren().setAll(assignmentFilepane);
    }

    @FXML
    private void CourseSelected() {
        if ((CoursesList.getSelectionModel().getSelectedIndex() != -1)) {
            Assignments.clear();
            loadAssignments();
        } else {
            Assignments.clear();
        }
    }

    @FXML
    private void AssignmentsSelected() {
        if ((AssignmentsList.getSelectionModel().getSelectedIndex() != -1)) {
            Details.setDisable(false);
        }
    }

    private void loadCourses() {
        // Llama a la clase de hilos para cargar los cursos
        new StudentCoursesThread(this, "loadCourses").start();
    }

    private void loadAssignments() {
        // Llama a la clase de hilos para cargar las asignaciones
        new StudentCoursesThread(this, "loadAssignments").start();
    }

    public void updateCourses(ObservableList<Course> courses) {
        Courses.setAll(courses);
        CoursesList.setItems(Courses);
    }

    public void updateAssignments(ObservableList<Assignment> assignments) {
        Assignments.setAll(assignments);
        AssignmentsList.setItems(Assignments);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Details.setDisable(true);
        token = User.getInstance().getToken();
        Courses = FXCollections.observableArrayList();
        Assignments = FXCollections.observableArrayList();
        loadCourses();
    }
}



