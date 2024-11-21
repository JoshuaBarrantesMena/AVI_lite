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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
public class TeacherAdminCoursesController implements Initializable {
    User User;
    String token;
       private final OkHttpClient client = new OkHttpClient();
    
    @FXML private TextField Name, Fields;
    
    @FXML private TextArea Information;
    
    @FXML private ListView<Course> CoursesList;         //crear funcion toString en la clase 'Course'
    @FXML private ObservableList<Course> Courses;
    
    @FXML private ComboBox<Career> CBCareer;        //crear funcion toString en la clase 'Career'
    @FXML private ObservableList<Career> Careers;
    
    @FXML private Button Add, Update, Delete;
    
    @FXML
    private void CoursesListSelect(){
        Update.setDisable(false);
        Delete.setDisable(false);
        if(CoursesList.getSelectionModel().getSelectedIndex() != -1){
            
        }
        Course fillCourse= CoursesList.getSelectionModel().getSelectedItem();
             Name.setText(fillCourse.getName());
             Fields.setText(Integer.toString(fillCourse.getTotalFields()));
             Information.setText(fillCourse.getInformation());
             int teacherId = fillCourse.getTeacherID();

       
      
        int careerID = fillCourse.getIDCareer();
        
              for (Career career : CBCareer.getItems()) {
            if (career.getID() == careerID) {
                CBCareer.setValue(career); 
                break;
            }
        }
        
        
    
        
        
        
        
        
    }
@FXML
private void AddCourse() {
    Career career = CBCareer.getValue();
    User teacher = User.getInstance();
    int careerID = career.getID();
    int teacherID = teacher.getID();
    String name = Name.getText();
    String information = Information.getText();
    int totalFields = Integer.parseInt(Fields.getText());

    String jsonInputString = String.format(
            "{\"carrera_id\": %s, \"profesor_id\": %s, \"nombre\": \"%s\", \"informacion\": \"%s\", \"campos_totales\": %s }",
            careerID, teacherID, name, information, totalFields
    );

    TeacherAdminCoursesThread thread = new TeacherAdminCoursesThread(
            "ADD_COURSE", client, token, jsonInputString, CoursesList, null
    );
    thread.start();
}

@FXML
private void UpdateCourse() {
    Course course = CoursesList.getSelectionModel().getSelectedItem();
    int courseId = course.getId();
    Career career = CBCareer.getValue();
    User teacher = User.getInstance();
    int careerID = career.getID();
    int teacherID = teacher.getID();
    String name = Name.getText();
    String information = Information.getText();
    int totalFields = Integer.parseInt(Fields.getText());

    String jsonInputString = String.format(
            "{\"carrera_id\": %s, \"profesor_id\": %s, \"nombre\": \"%s\", \"informacion\": \"%s\", \"campos_totales\": %s }",
            careerID, teacherID, name, information, totalFields
    );

    String[] data = {jsonInputString, String.valueOf(courseId)};
    TeacherAdminCoursesThread thread = new TeacherAdminCoursesThread(
            "UPDATE_COURSE", client, token, data, CoursesList, null
    );
    thread.start();
}

@FXML
private void deleteCourse() {
    Course course = CoursesList.getSelectionModel().getSelectedItem();
    TeacherAdminCoursesThread thread = new TeacherAdminCoursesThread(
            "DELETE_COURSE", client, token, course.getId(), CoursesList, null
    );
    thread.start();
}


        
        //cargar todos los cursos en el ObservableList 'Courses'
 
        
        
        
        
        
        
        
        
        
        
    

    
  @Override
public void initialize(URL url, ResourceBundle rb) {
    token = User.getInstance().getToken();
    Courses = FXCollections.observableArrayList();
    Careers = FXCollections.observableArrayList();

    TeacherAdminCoursesThread loadCoursesThread = new TeacherAdminCoursesThread(
            "LOAD_COURSES", client, token, null, CoursesList, null
    );
    loadCoursesThread.start();

    TeacherAdminCoursesThread loadCareersThread = new TeacherAdminCoursesThread(
            "LOAD_CAREERS", client, token, null, null, CBCareer
    );
    loadCareersThread.start();
}
}
