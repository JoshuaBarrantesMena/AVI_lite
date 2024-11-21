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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
public class TeacherFacAndDepController implements Initializable {
       private final OkHttpClient client = new OkHttpClient();
     private String token;
     private User User;
          private int universityId;
    @FXML private Label ListName;
    @FXML private ComboBox<String> FilterCB;
    @FXML private ListView<Object> List;
    private ObservableList<Object> visibleList;
    
    @FXML
    private void addNew() throws IOException{
        Pane addFacAndDepView = FXMLLoader.load(getClass().getResource("/com/josh/avi_lite/TeacherAddFacAndDep.fxml"));
        TeacherViewController.publicPane.getChildren().setAll(addFacAndDepView);
    }
    @FXML
    private void delete(){
        
    }
    @FXML
    private void detectFilterCB(){
        if(FilterCB.getSelectionModel().getSelectedIndex() == 0){
            ListName.setText("Facultades Actuales");
            loadFaculties();
        }else{
            ListName.setText("Departamentos actuales");
            loadDepartments();
        }
    }
    
  private void loadFaculties() {
    TeacherFacAndDepThread thread = new TeacherFacAndDepThread("LOAD_FACULTIES", client, token, universityId, List);
    thread.start();
}

private void loadDepartments() {
    TeacherFacAndDepThread thread = new TeacherFacAndDepThread("LOAD_DEPARTMENTS", client, token, universityId, List);
    thread.start();
}


    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        visibleList = FXCollections.observableArrayList();
        FilterCB.getItems().setAll("Facultades", "Departamentos");
        FilterCB.getSelectionModel().select(0);
        ListName.setText("Facultades Actuales");
        List.setItems(visibleList);
      
              token= User.getInstance().getToken();
      universityId = User.getInstance().getUniversityID();
        loadFaculties();
    }
}
