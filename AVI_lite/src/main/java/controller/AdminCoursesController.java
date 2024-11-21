/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import utils.AdminCoursesThread;
import com.josh.avi_lite.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import utils.Career;
import utils.Course;
import utils.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminCoursesController implements Initializable {

    private String token;

    @FXML
    private Button Add, Update, Delete, Back;

    @FXML
    private TextField Name, Fields;

    @FXML
    private TextArea Information;

    @FXML
    private ComboBox<User> CBteacher;
    private ObservableList<User> Teachers;

    @FXML
    private ComboBox<Career> CBcareer;
    private ObservableList<Career> Careers;

    @FXML
    private ListView<Course> CoursesList;
    private ObservableList<Course> Courses;

    @FXML
    private void ReturnScene() throws IOException {
        App.setRoot("AdminView", 600, 620);
    }

    @FXML
    private void AddCourse() {
        Career career = CBcareer.getValue();
        User teacher = CBteacher.getValue();
        String name = Name.getText();
        String information = Information.getText();
        int totalFields = Integer.parseInt(Fields.getText());

        String jsonInputString = String.format(
                "{\"carrera_id\": %s, \"profesor_id\": %s, \"nombre\": \"%s\", \"informacion\": \"%s\", \"campos_totales\": %s}",
                career.getID(), teacher.getID(), name, information, totalFields
        );

        AdminCoursesThread thread = new AdminCoursesThread("addCourse", this, token, jsonInputString);
        thread.start();
    }

    @FXML
    private void UpdateCourse() {
        if (!CoursesList.getSelectionModel().isEmpty()) {
            Course course = CoursesList.getSelectionModel().getSelectedItem();
            Career career = CBcareer.getValue();
            User teacher = CBteacher.getValue();
            String name = Name.getText();
            String information = Information.getText();
            int totalFields = Integer.parseInt(Fields.getText());

            String jsonInputString = String.format(
                    "{\"carrera_id\": %s, \"profesor_id\": %s, \"nombre\": \"%s\", \"informacion\": \"%s\", \"campos_totales\": %s}",
                    career.getID(), teacher.getID(), name, information, totalFields
            );

            AdminCoursesThread thread = new AdminCoursesThread("updateCourse", this, token, jsonInputString);
            thread.courseId = course.getId();
            thread.start();
        }
    }

    @FXML
    private void DeleteCourse() {
        if (!CoursesList.getSelectionModel().isEmpty()) {
            Course course = CoursesList.getSelectionModel().getSelectedItem();
            AdminCoursesThread thread = new AdminCoursesThread("deleteCourse", this, token, course.getId());
            thread.start();
        }
    }

    @FXML
    private void CoursesSelected() {
        if (!CoursesList.getSelectionModel().isEmpty()) {
            Add.setDisable(false);
            Update.setDisable(false);
            Delete.setDisable(false);

            Course fillCourse = CoursesList.getSelectionModel().getSelectedItem();
            Name.setText(fillCourse.getName());
            Fields.setText(Integer.toString(fillCourse.getTotalFields()));
            Information.setText(fillCourse.getInformation());

            for (User teacher : CBteacher.getItems()) {
                if (teacher.getID() == fillCourse.getTeacherID()) {
                    CBteacher.setValue(teacher);
                    break;
                }
            }

            for (Career career : CBcareer.getItems()) {
                if (career.getID() == fillCourse.getIDCareer()) {
                    CBcareer.setValue(career);
                    break;
                }
            }
        } else {
            clearFields();
        }
    }

    @FXML
    private void AddConfirmation() {
        boolean isName = !Name.getText().isEmpty();
        boolean isTeacher = !CBteacher.getSelectionModel().isEmpty();
        boolean isCareer = !CBcareer.getSelectionModel().isEmpty();
        boolean isFields = !Fields.getText().isEmpty();
        boolean isCourseSelected = !CoursesList.getSelectionModel().isEmpty();

        Add.setDisable(!(isName && isTeacher && isCareer && isFields && isCourseSelected));
    }

    private void clearFields() {
        Name.clear();
        Fields.clear();
        Information.clear();
        CBteacher.getSelectionModel().clearSelection();
        CBteacher.setPromptText("Sin Profesor");
        CBcareer.getSelectionModel().clearSelection();
        CBcareer.setPromptText("Sin Carrera");
        Delete.setDisable(true);
        Update.setDisable(true);
        Add.setDisable(true);
    }

    public void updateTeachers(ObservableList<User> teachers) {
        Teachers = teachers;
        CBteacher.getItems().setAll(teachers);
    }

    public void updateCareers(ObservableList<Career> careers) {
        Careers = careers;
        CBcareer.getItems().setAll(careers);
    }

    public void updateCourses(ObservableList<Course> courses) {
        Courses = courses;
        CoursesList.getItems().setAll(courses);
    }

    public void refreshCourses() {
        AdminCoursesThread thread = new AdminCoursesThread("loadCourses", this, token);
        thread.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        token = User.getInstance().getToken();

        Teachers = FXCollections.observableArrayList();
        Careers = FXCollections.observableArrayList();
        Courses = FXCollections.observableArrayList();

        AdminCoursesThread loadTeachersThread = new AdminCoursesThread("loadTeachers", this, token);
        AdminCoursesThread loadCareersThread = new AdminCoursesThread("loadCareers", this, token);
        AdminCoursesThread loadCoursesThread = new AdminCoursesThread("loadCourses", this, token);

        loadTeachersThread.start();
        loadCareersThread.start();
        loadCoursesThread.start();
    }
}
