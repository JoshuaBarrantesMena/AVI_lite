/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import com.josh.avi_lite.App;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import utils.University;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import utils.AdminRegisterThread;

public class AdminRegisterController implements Initializable {
    @FXML private TextField universityID;
    @FXML private Label l1;
    @FXML private TextField ID, Name, LastName, Mail;
    @FXML private ToggleButton eye;
    @FXML private ComboBox<String> CBUserType;
    @FXML private ComboBox<University> CBuniversitys;
    @FXML private PasswordField Pass;

    private String savedPassword = "";

    @FXML
    private void ReturnScene() throws IOException {
        App.setRoot("AdminUsers", 700, 600);
    }

    @FXML
    private void RegisterUser() {
        if (ID.getText().isEmpty() || Name.getText().isEmpty() || 
            LastName.getText().isEmpty() || Pass.getText().isEmpty() || 
            Mail.getText().isEmpty()) {
            return;
        }

        String id = universityID.getText()+ ID.getText();
        String name = Name.getText();
        String lastName = LastName.getText();
        String pass = Pass.getText();
        String email = Mail.getText();
        String userType = CBUserType.getValue();
        University university = CBuniversitys.getSelectionModel().getSelectedItem();

        if (university == null) {
            System.err.println("Seleccione una universidad válida.");
            return;
        }

        int universityID = university.getID();

        String jsonInputString = String.format(
            "{\"identificacion\": %s, \"pass\": \"%s\", \"nombre\": \"%s\", \"apellidos\": \"%s\", \"correo\": \"%s\", \"rol\": \"%s\",\"universidad_id\": %s}",
            id, pass, name, lastName, email, userType, universityID
        );

        // Crear un hilo para registrar al usuario
        AdminRegisterThread thread = new AdminRegisterThread("registerUser", this, jsonInputString);
        thread.start();
    }

    public void onRegistrationSuccess() throws IOException {
        ReturnScene();
    }

    public void onRegistrationFailure(int errorCode) {
        System.err.println("Error al registrar usuario: código " + errorCode);
    }

    @FXML
    void MakeVisible() {
        if (eye.isSelected()) {
            savedPassword = Pass.getText();
            Pass.setPromptText(savedPassword);
            Pass.setText("");
        } else {
            Pass.setText(savedPassword);
            Pass.setPromptText("");
        }
    }

    @FXML
    void universitySelected() {
        University university = CBuniversitys.getSelectionModel().getSelectedItem();
        if (university != null) {
            universityID.setText(String.valueOf(university.getID()));
        } else {
            universityID.clear();
        }
    }

    public void updateUniversityComboBox(ObservableList<University> universityList) {
        CBuniversitys.setItems(universityList);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        l1.setFocusTraversable(true);
        CBUserType.getItems().setAll("Estudiante", "Profesor", "Administrador");
        CBUserType.getSelectionModel().select(0);

        AdminRegisterThread thread = new AdminRegisterThread("loadUniversities", this);
        thread.start();
    }
}
