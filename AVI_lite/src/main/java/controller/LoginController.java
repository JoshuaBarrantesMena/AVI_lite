
package controller;
import com.josh.avi_lite.App;
import org.json.JSONObject; 

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.*;
import org.json.JSONArray;
import utils.University;



import utils.User;



public class LoginController implements Initializable {



private String savedPassword = ""; 
     private ObservableList<University> universitys = FXCollections.observableArrayList();
      
           private final OkHttpClient client = new OkHttpClient();
      
    @FXML
    private ComboBox<University> CBuniversitys;
        @FXML
    private PasswordField Pass;
    
    static String currentRole;
    User currentUser;
   int currentID; 
    
    @FXML
    private TextField UserIDText;  
   
    @FXML
    private TextField careerText;
    @FXML
    
        private ToggleButton eye;

       @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("Register", 360, 400);  
    }
  @FXML
private void switchToAdmin() throws IOException {

    if (UserIDText.getText().isEmpty() || Pass.getText().isEmpty()) {
        return;
    }

    String id = UserIDText.getText();
    String password = Pass.getText();

    JSONObject json = new JSONObject();
    json.put("identificacion", id);
    json.put("pass", password);

    String admin = "Admin";
    String student = "Estudiante";
    String teacher = "Profesor";

    boolean loginExitoso = iniciarSesion(id, password);

    if (loginExitoso) {
        

        if (currentRole.equals(admin)) {
           

  
             App.setRoot("AdminView", 600, 620);

       
           

        } else if (currentRole.equals(teacher)) {
           
        
           App.setRoot("TeacherView", 800, 500);

          
          

        } else if (currentRole.equals(student)) {
         
          
            App.setRoot("StudentView", 800, 500);

         
        
            

        } else {
            System.out.println("No coincide con ningun rol");
        }

    } else {
       
    }
}

private void loadUniversities() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/universidades")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar universidades: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
          
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonUni = jsonArray.getJSONObject(i);
                University university = new University(
                        jsonUni.getInt("universidad_id"),
                        jsonUni.getString("nombre")
                );
                universitys.add(university);
            }
             CBuniversitys.setItems(universitys);
            
        }
    }

 @FXML
      void MakeVisible(ActionEvent event) {
    if (eye.isSelected()) {
        // Guardamos el texto actual en la variable temporal y lo mostramos
        savedPassword = Pass.getText();
        Pass.setPromptText(savedPassword); // Mostramos el texto como prompt
        Pass.setText(""); // Limpiamos el campo para que el prompt sea visible
    } else {
        // Restauramos el texto oculto
        Pass.setText(savedPassword); // Recuperamos el texto guardado
        Pass.setPromptText(""); // Limpiamos el prompt
    }
}

    
 private  boolean iniciarSesion(String identificacion, String password) throws IOException {
                 

           University university =   CBuniversitys.getSelectionModel().getSelectedItem();

    int universityID   = university.getID();
        var jsonInputString = String.format(
            "{\"identificacion\": %s, \"pass\": \"%s\",\"universidad_id\": %s}", identificacion, password,universityID
        );

     
          RequestBody body = RequestBody.create(
        jsonInputString, MediaType.get("application/json; charset=utf-8")
    );

 
    Request request = new Request.Builder()
            .url("http://localhost:5000/api/auth/login")
            .post(body)
            .build();

  
      try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
            System.out.println("Error en la conexión: " + response.code());
            return false; 
        }

    
        String responseBody = response.body().string();

        
        if (responseBody != null) {
        
            JSONObject jsonResponse = new JSONObject(responseBody);

           
            String mensaje = jsonResponse.getString("message");
            int id = jsonResponse.getInt("identificacion");
            User.getInstance().setID(id);
            String token = jsonResponse.getString("token");
            User.getInstance().setToken(token);
            String name = jsonResponse.getString("nombre");
            User.getInstance().setName(name);
            String lastName = jsonResponse.getString("apellidos");
            User.getInstance().setLastName(lastName);
            String mail = jsonResponse.getString("correo");
            User.getInstance().setMail(mail);
            String rol = jsonResponse.getString("rol");
            User.getInstance().setRol(rol);
            User.getInstance().setUniversityID(universityID);
            
            
        
            
          
            currentRole = rol;

            
            System.out.println("Mensaje: " + mensaje);
            System.out.println("Token: " + token);
            System.out.println("Rol: " + rol);

            return true; 
        }
       
        
    }
             catch (IOException ex) {
            ex.printStackTrace();
         }
 return false;
    
}



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
          try {
              loadUniversities();
          } catch (IOException ex) {
              ex.printStackTrace();
          }
        
          
          
        
    }
}



    