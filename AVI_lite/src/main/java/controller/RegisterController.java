package controller;

import com.josh.avi_lite.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.University;



public class RegisterController implements Initializable {
    
    private String savedPassword = ""; 
    private final OkHttpClient client = new OkHttpClient();
    private ObservableList<University> universitys = FXCollections.observableArrayList();
      
    @FXML private ComboBox<University> CBuniversitys;
    
    @FXML private TextField IDText, NameText, LastNameText,  MailText, ID;
    
    @FXML private PasswordField Pass;
    
    @FXML private ComboBox<String> CBUserType;
    
    @FXML private Label r;
    
    @FXML private ToggleButton eye;
    
    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("Login", 360, 360);
    }
    @FXML
    void universitySelected(ActionEvent event) {
        if(!CBuniversitys.getSelectionModel().isSelected(0)){
            University university= CBuniversitys.getSelectionModel().getSelectedItem();
            ID.setText(Integer.toString(university.getID()));
        }else{
            ID.clear();
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
   
    @FXML
    private void register(ActionEvent e) throws IOException {
        University university=   CBuniversitys.getSelectionModel().getSelectedItem();
        if (IDText.getText().isEmpty() || NameText.getText().isEmpty() || 
            LastNameText.getText().isEmpty() || Pass.getText().isEmpty() || 
            MailText.getText().isEmpty()) {
            r.setText("Por favor, completa todos los campos.");
            return;
        }
        String id= "";
        if(university.getID()!=0){
            id = university.getID() +  IDText.getText();
        }
        else{
            id= IDText.getText();
        }
        String name = NameText.getText();
        String lastName = LastNameText.getText();
        String pass = Pass.getText();
        String email = MailText.getText();
        String userType = CBUserType.getValue(); 

        int universityID   = university.getID();
        String jsonInputString = String.format(
            "{\"identificacion\": %s, \"pass\": \"%s\", \"nombre\": \"%s\", \"apellidos\": \"%s\", \"correo\": \"%s\", \"rol\": \"%s\",\"universidad_id\": %s}",
            id, pass, name, lastName, email, userType, universityID);
        System.out.println("JSON Enviado: " + jsonInputString);
        boolean registroExitoso = registrarUsuario(jsonInputString);
        if (registroExitoso) {
            switchToLogin();
        } else {
            r.setText("Error en el registro. Intenta de nuevo.");
        }
    }
    private boolean registrarUsuario(String jsonInputString) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(
            jsonInputString,
            MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
            .url("http://localhost:5000/api/auth/register")
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) { 
                return true;
            } else {
                System.out.println("Error en la conexión: " + response.code());
                return false;
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        r.setFocusTraversable(true);
        CBUserType.getItems().setAll("Estudiante", "Profesor");
        CBUserType.getSelectionModel().select(0);
         try {
             loadUniversities();
         } catch (IOException ex) {
             ex.printStackTrace();
         }
    }
}