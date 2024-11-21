package controller;

import com.josh.avi_lite.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.AdminUserThread;
import utils.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminUsersController implements Initializable {
    private String token;

    @FXML
    private Button Register, Update, Delete, Back;

    @FXML
    private TextField ID, Name, LastName, Mail;

    @FXML
    private PasswordField pass;

    @FXML
    private TableView<User> UserList;

    @FXML
    private TableColumn<User, String> NameColumn, LastNColumn, RoleColumn;

    private ObservableList<User> Users;

    @FXML
    private void UserListSelected() {
        if (!UserList.getSelectionModel().isSelected(-1)) {
            User user = UserList.getSelectionModel().getSelectedItem();

            ID.setText(Integer.toString(user.getID()));
            Name.setText(user.getName());
            LastName.setText(user.getLastName());
            Mail.setText(user.getMail());
            pass.setText(user.getPassword());

            ID.setDisable(false);
            Name.setDisable(false);
            LastName.setDisable(false);
            Mail.setDisable(false);

            Update.setDisable(false);
            Delete.setDisable(false);
        }
    }

    @FXML
    private void DeleteUser() {
        if (!UserList.getSelectionModel().isSelected(-1)) {
            int id = Integer.parseInt(ID.getText());

            AdminUserThread deleteThread = new AdminUserThread("deleteUser", this, token, id);
            deleteThread.start();
        }
    }

    @FXML
    private void UpdateUser() {
        if (!UserList.getSelectionModel().isSelected(-1)) {
            if (ID.getText().isEmpty() || Name.getText().isEmpty() || LastName.getText().isEmpty() ||
                    pass.getText().isEmpty() || Mail.getText().isEmpty()) {
                return;
            }

            int id = Integer.parseInt(ID.getText());
            String name = Name.getText();
            String lastName = LastName.getText();
            String password = pass.getText();
            String email = Mail.getText();

            User user = UserList.getSelectionModel().getSelectedItem();
            String userType = user.getRol();

            String jsonInputString = String.format(
                    "{\"pass\": \"%s\", \"nombre\": \"%s\", \"apellidos\": \"%s\", \"correo\": \"%s\", \"rol\": \"%s\"}",
                    password, name, lastName, email, userType
            );

            AdminUserThread updateThread = new AdminUserThread("updateUser", this, token, jsonInputString, id);
            updateThread.start();
        }
    }

    @FXML
    private void RegisterUser() throws IOException {
        App.setRoot("AdminRegister", 360, 420);
    }

    @FXML
    private void ReturnScene() throws IOException {
        App.setRoot("AdminView", 600, 620);
    }

    @FXML
    private void UpdateCheck() {
        Update.setDisable(Name.getText().isEmpty() || LastName.getText().isEmpty() || Mail.getText().isEmpty());
    }

    private void loadUsers() {
        AdminUserThread loadThread = new AdminUserThread("loadUsers", this, token);
        loadThread.start();
    }

    public void updateUsers(ObservableList<User> users) {
        Users = users;
        UserList.setItems(Users);
    }

    public void refreshUsers() {
        loadUsers();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        token = User.getInstance().getToken();
        Users = FXCollections.observableArrayList();

        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        LastNColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        RoleColumn.setCellValueFactory(new PropertyValueFactory<>("rol"));

        loadUsers();
    }
}
