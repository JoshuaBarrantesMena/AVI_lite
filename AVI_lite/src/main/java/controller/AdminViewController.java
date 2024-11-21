package controller;
import com.josh.avi_lite.App;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utils.AdminChildThread;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import utils.Career;
import utils.Course;
import utils.Department;
import utils.Faculty;
import utils.University;
import utils.User;

public class AdminViewController implements Initializable {

    @FXML private Button ACour, CCar, CCour, CDep, CFac, CUni, Cancel, Confirm, DCar, DCour, DDep, DFac, DUni;
    @FXML private ComboBox<Department> CBdepartment;
    @FXML private ComboBox<Faculty> CBfaculty;
    @FXML private ComboBox<University> CBuniversitys;
     ObservableList<Career> careers;
    
    
    @FXML
    private ComboBox<Career> CBcareer;
    @FXML private TableView<Course> CourseTable;
    @FXML private TextArea CourseInfo;
    @FXML private TextField Name;
    @FXML private Label l1, l2, l3, l4, l5, l6;

    private AdminChildThread adminChildThread;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adminChildThread = new AdminChildThread(User.getInstance().getToken());
      
careers= FXCollections.observableArrayList();
        loadUniversities();
        
        
    }

    private void loadUniversities() {
        new Thread(() -> {
            try {
                ObservableList<University> universities = adminChildThread.loadUniversities();
                Platform.runLater(() -> CBuniversitys.setItems(universities));
            } catch (IOException e) {
                Platform.runLater(() -> showError("Error al cargar universidades"));
            }
        }).start();
    }

    private void loadFaculties(int universityId) {
        new Thread(() -> {
            try {
                ObservableList<Faculty> faculties = adminChildThread.loadFaculties(universityId);
                Platform.runLater(() -> CBfaculty.setItems(faculties));
            } catch (IOException e) {
                Platform.runLater(() -> showError("Error al cargar facultades"));
            }
        }).start();
    }

    private void loadDepartments(int facultyId) {
        new Thread(() -> {
            try {
                ObservableList<Department> departments = adminChildThread.loadDepartments(facultyId);
                Platform.runLater(() -> CBdepartment.setItems(departments));
            } catch (IOException e) {
                Platform.runLater(() -> showError("Error al cargar departamentos"));
            }
        }).start();
    }

    private void loadCareers(int departmentId) {
         new Thread(() -> {
            try {
                careers = adminChildThread.loadCareers(departmentId);
                Platform.runLater(() -> CBcareer.setItems(careers));
            } catch (IOException e) {
                Platform.runLater(() -> showError("Error al cargar departamentos"));
            }
        }).start();
    }

  
    @FXML
    void UniversitySelected(ActionEvent event) {
        University selectedUniversity = CBuniversitys.getSelectionModel().getSelectedItem();
        if (selectedUniversity != null) {
            CBfaculty.setDisable(false);
            CUni.setDisable(false);
            DUni.setDisable(false);
             CFac.setDisable(false);
            loadFaculties(selectedUniversity.getID());
        }
    }

    @FXML
    void FacultySelected(ActionEvent event) {
        Faculty selectedFaculty = CBfaculty.getSelectionModel().getSelectedItem();
        if (selectedFaculty != null) {
            CBdepartment.setDisable(false);
             CDep.setDisable(false);
            DFac.setDisable(false);
            
            loadDepartments(selectedFaculty.getID());
            
        }
    }

    @FXML
    void DepartmentSelected(ActionEvent event) {
        Department selectedDepartment = CBdepartment.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            CBcareer.setDisable(false);
            DDep.setDisable(false);
            CCar.setDisable(false);
           
            loadCareers(selectedDepartment.getId());
        }
    }

    @FXML
    void CareerSelected(MouseEvent event) {
        Career selectedCareer = CBcareer.getSelectionModel().getSelectedItem();
        if (selectedCareer != null) {
        
           DCar.setDisable(false);
           
        }
    }

    @FXML
    private void createUniversity(ActionEvent event) {
        String newUniversity = Name.getText();
        if (!newUniversity.isEmpty()) {
            new Thread(() -> {
                try {
                    adminChildThread.createUniversity(newUniversity);
                    Platform.runLater(this::loadUniversities);
                } catch (IOException e) {
                    Platform.runLater(() -> showError("Error al crear universidad"));
                }
            }).start();
        }
    }

    @FXML
    private void deleteUniversity(ActionEvent event) {
        University selectedUniversity = CBuniversitys.getSelectionModel().getSelectedItem();
        if (selectedUniversity != null) {
            new Thread(() -> {
                try {
                    adminChildThread.deleteUniversity(selectedUniversity.getID());
                    Platform.runLater(this::loadUniversities);
                } catch (IOException e) {
                    Platform.runLater(() -> showError("Error al eliminar universidad"));
                }
            }).start();
        }
    }

    @FXML
    private void createFaculty(ActionEvent event) {
        University selectedUniversity = CBuniversitys.getSelectionModel().getSelectedItem();
        String newFaculty = Name.getText();
        if (selectedUniversity != null && !newFaculty.isEmpty()) {
            new Thread(() -> {
                try {
                    adminChildThread.createFaculty( newFaculty,selectedUniversity.getID());
                    Platform.runLater(() -> loadFaculties(selectedUniversity.getID()));
                } catch (IOException e) {
                    Platform.runLater(() -> showError("Error al crear facultad"));
                }
            }).start();
        }
    }

    @FXML
    private void deleteFaculty(ActionEvent event) {
        Faculty selectedFaculty = CBfaculty.getSelectionModel().getSelectedItem();
        if (selectedFaculty != null) {
            new Thread(() -> {
                try {
                    adminChildThread.deleteFaculty(selectedFaculty.getID());
                    Platform.runLater(() -> loadFaculties(selectedFaculty.getID()));
                } catch (IOException e) {
                    Platform.runLater(() -> showError("Error al eliminar facultad"));
                }
            }).start();
        }
    }

    @FXML
    private void createDepartment(ActionEvent event) {
        Faculty selectedFaculty = CBfaculty.getSelectionModel().getSelectedItem();
        String newDepartment = Name.getText();
        if (selectedFaculty != null && !newDepartment.isEmpty()) {
            new Thread(() -> {
                try {
                    adminChildThread.createDepartment(newDepartment,selectedFaculty.getID());
                    Platform.runLater(() -> loadDepartments(selectedFaculty.getID()));
                } catch (IOException e) {
                    Platform.runLater(() -> showError("Error al crear departamento"));
                }
            }).start();
        }
    }
 @FXML
    private void adminCourses() throws IOException{
        App.setRoot("AdminCourses", 700, 600);
    }
    @FXML
    private void adminUsers() throws IOException{
        App.setRoot("AdminUsers", 700, 600);
    }
    @FXML
    private void deleteDepartment(ActionEvent event) {
        Department selectedDepartment = CBdepartment.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            new Thread(() -> {
                try {
                    adminChildThread.deleteDepartment(selectedDepartment.getId());
                    Platform.runLater(() -> loadDepartments(selectedDepartment.getId()));
                } catch (IOException e) {
                    Platform.runLater(() -> showError("Error al eliminar departamento"));
                }
            }).start();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


