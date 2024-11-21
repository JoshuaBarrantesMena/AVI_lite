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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
public class TeacherEnrollStudentsController implements Initializable {
      private User User;
   private String token;
    @FXML private TextField StudentFilter, CourseFilter, StudentName, CourseName;
      private final OkHttpClient client = new OkHttpClient();
    @FXML private ListView<User> StudentList;
    @FXML private ListView<Course> CourseList;
        @FXML
    private Button Confirm;
    @FXML private ObservableList<User> Students, StudentsFiltered;
    @FXML private ObservableList<Course> Courses, CoursesFiltered;
    
    private void ListSelect(){
        //detectar si en ambas listas hay algo seleccionado, si las hay, verifica en DB si ese estudiante esta matriculado en el curso
        //si lo esta, no hace nada (dar a entender que ya esta matriculado), sino, el boton confirmar se activa
        
        if(StudentList.getSelectionModel().getSelectedIndex() != -1&&CourseList.getSelectionModel().getSelectedIndex() != -1 ){
         Confirm.setDisable(false);
    }
        
        
        
        
    }
   @FXML
void enrollStudent(ActionEvent event) {
    Course selectedCourse = CourseList.getSelectionModel().getSelectedItem();
    User selectedStudent = StudentList.getSelectionModel().getSelectedItem();
    if (selectedCourse != null && selectedStudent != null) {
        EnrollmentData enrollmentData = new EnrollmentData(selectedCourse.getId(), selectedStudent.getID());
        TeacherEnrollStudentsThread thread = new TeacherEnrollStudentsThread(
                "ENROLL_STUDENT", client, token, enrollmentData, null, null, null, null
        );
        thread.start();
    }
}


  
   
    @FXML
    private void StudentsListSelect(){
        if(StudentList.getSelectionModel().getSelectedIndex() != -1){
            StudentName.setText(StudentList.getSelectionModel().getSelectedItem().getName());
        }
        ListSelect();
    }
    @FXML
    private void CoursesListSelect(){
        if(CourseList.getSelectionModel().getSelectedIndex() != -1){
            CourseName.setText(CourseList.getSelectionModel().getSelectedItem().getName());
        }
        ListSelect();
    }
    
    @FXML
    private void StudentFilterList(){
        //por cada letra ingresada, se realiza una busqueda en la lista de estudiantes que contengan lo ingresado por texto, los resultados se guardan en 'StudentsFiltered'
        Students.clear();
        loadStudents();
        
        
        
    }
    @FXML
    private void CourseFilterList(){
        //por cada letra ingresada, se realiza una busqueda en la lista de cursos que contengan lo ingresado por texto, los resultados se guardan en 'CoursesFiltered'
      Courses.clear();
        loadCourses();
        
    }
private void loadCourses() {
    String filter = CourseFilter.getText();
    TeacherEnrollStudentsThread thread = new TeacherEnrollStudentsThread(
            "LOAD_COURSES", client, token, filter, Courses, null, CourseList, null
    );
    thread.start();
}

private void loadStudents() {
    String filter = StudentFilter.getText();
    TeacherEnrollStudentsThread thread = new TeacherEnrollStudentsThread(
            "LOAD_STUDENTS", client, token, filter, null, Students, null, StudentList
    );
    thread.start();
}

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        Confirm.setDisable(true);
         token = User.getInstance().getToken();
        
        Students = FXCollections.observableArrayList();         //listas completas
        Courses = FXCollections.observableArrayList();
        StudentsFiltered = FXCollections.observableArrayList();         //listas filtradas e incompletas
        CoursesFiltered = FXCollections.observableArrayList();
    }
}