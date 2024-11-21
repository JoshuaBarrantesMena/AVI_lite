 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.TeacherViewController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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



public class TeacherAddFacAndDepController implements Initializable {
     private final OkHttpClient client = new OkHttpClient();
     private String token;
     private User User;
     private int universityId;
    @FXML private VBox ExtraVBox, AdminVBox, NameVBox;
    
    @FXML private Label Origin;
    
    @FXML private ComboBox<String> AdminCB;
    @FXML private ComboBox<University> UniCB; // Guarda las universidades
    @FXML private ComboBox<Object> ExtraCB; // Guarda facultades y departamentos (funcion 'toString' requerida en ambas clases)
    
    @FXML private TextField Name;
    
    @FXML private Button Add, Return;
    
    @FXML
    private void universitySelect(){
        AdminVBox.setDisable(false);
        NameVBox.setDisable(false);
        AdminCB.getSelectionModel().select(0);
    }
    @FXML
    private void AdminCBSelect() throws IOException{
        switch (AdminCB.getSelectionModel().getSelectedIndex()) {
            default:
                ExtraVBox.setDisable(true);
                ExtraVBox.setPrefWidth(1);
                ExtraVBox.setVisible(false);
                break;
            case 1:
                ExtraVBox.setDisable(false);
                ExtraVBox.setPrefWidth(240);
                ExtraVBox.setVisible(true);
                Origin.setText("Facultad de origen");
                
                loadFaculties();
                // Cargar en 'ExtraCB' todas las facultades
                
                
                
                break;
            case 2:
                ExtraVBox.setDisable(false);
                ExtraVBox.setPrefWidth(240);
                ExtraVBox.setVisible(true);
                Origin.setText("Departamento de origen");
               loadDepartments();
                break;
        }
    }
    
    @FXML
    private void nameTextDetect(){
        if(AdminCB.getSelectionModel().isSelected(0)){
            if(!Name.getText().equals("")){ // (funcionalidad) si se esta añadiendo una facultad, solo detecta si hay algo escrito
                Add.setDisable(false);
            }else{
                Add.setDisable(true);
            }
        }else{
            if(!Name.getText().equals("") && !ExtraCB.getSelectionModel().isSelected(-1)){ // (funcionalidad) si el nombre no esta vacio y hay algo seleccionado en 'ExtraCB'
                Add.setDisable(false);
            }else{
                Add.setDisable(true);
            }
        }
    }
    @FXML
private void addNew() {
    String name = Name.getText();
    int relatedId = 0;

    if (AdminCB.getSelectionModel().getSelectedIndex() > 0 && ExtraCB.getValue() != null) {
        if (AdminCB.getSelectionModel().getSelectedIndex() == 1) {
            relatedId = ((Faculty) ExtraCB.getValue()).getID();
        } else if (AdminCB.getSelectionModel().getSelectedIndex() == 2) {
            relatedId = ((Department) ExtraCB.getValue()).getId();
        }
    }

    String operationType;
    switch (AdminCB.getSelectionModel().getSelectedIndex()) {
        case 0:
            operationType = "CREATE_FACULTY";
            break;
        case 1:
            operationType = "CREATE_DEPARTMENT";
            break;
        case 2:
            operationType = "CREATE_CAREER";
            break;
        default:
            throw new IllegalArgumentException("Operación no válida");
    }

    TeacherAddFacAndDepThread thread = new TeacherAddFacAndDepThread(operationType, client, token, universityId, null, name, relatedId);
    thread.start();
}

    @FXML
    private void returnScene() throws IOException{
        Pane facAndDepView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherFacAndDep.fxml"));
        TeacherViewController.publicPane.getChildren().setAll(facAndDepView);
    }
    
public void createFaculty() throws IOException {
    
      String name = Name.getText();
    
        String jsonInputString = "{\"nombre\": \"" + name + "\", \"universidad_id\": " + universityId + "}";
        RequestBody body = RequestBody.create(
                jsonInputString,
                MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/facultades")
                .addHeader("Authorization", token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al crear facultad: " + response.code());
            }
        }
    }

public void createDepartment() throws IOException {
    
     String name = Name.getText();
     Faculty faculty = (Faculty) ExtraCB.getValue();
    int facultyId = faculty.getID();
        String jsonInputString = "{\"nombre\": \"" + name + "\", \"facultad_id\": " + facultyId + "}";
        RequestBody body = RequestBody.create(
                jsonInputString,
                MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/departamentos")
                .addHeader("Authorization", token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al crear departamento: " + response.code());
            }
        }
    }

 public void createCareer() throws IOException {
     String name = Name.getText();
     Department department = (Department) ExtraCB.getValue();
    int departmentId = department.getId();
     
        String jsonInputString = "{\"nombre\": \"" + name + "\", \"departamento_id\": " + departmentId + "}";
        RequestBody body = RequestBody.create(
                jsonInputString,
                MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/carreras")
                .addHeader("Authorization", token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al crear carrera: " + response.code());
            }
        }
    }

private void loadFaculties() {
    TeacherAddFacAndDepThread thread = new TeacherAddFacAndDepThread("LOAD_FACULTIES", client, token, universityId, ExtraCB, null, 0);
    thread.start();
}

private void loadDepartments() {
    TeacherAddFacAndDepThread thread = new TeacherAddFacAndDepThread("LOAD_DEPARTMENTS", client, token, universityId, ExtraCB, null, 0);
    thread.start();
}


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AdminCB.getItems().setAll("Facultad", "Departamento", "Carrera");
        ExtraVBox.setDisable(true);
        ExtraVBox.setPrefWidth(1);
        ExtraVBox.setVisible(false);
        AdminCB.getSelectionModel().select(0);
      token= User.getInstance().getToken();
      universityId = User.getInstance().getUniversityID();
        
        //AdminVBox.setDisable(true);               //activar ambos cuando ya se obtengan universidades
        //NameVBox.setDisable(true);
    }
}
