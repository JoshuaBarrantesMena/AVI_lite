/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import utils.Career;
import utils.Department;
import utils.Faculty;
import utils.University;



public class AdminChildThread extends Thread {

    private final String token;
    private final OkHttpClient client = new OkHttpClient();

    public AdminChildThread(String token) {
        this.token = token;
    }

    // Cargar universidades
    public ObservableList<University> loadUniversities() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/universidades")
                .addHeader("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar universidades: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<University> universities = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonUni = jsonArray.getJSONObject(i);
                University university = new University(
                        jsonUni.getInt("universidad_id"),
                        jsonUni.getString("nombre")
                );
                universities.add(university);
            }
            return universities;
        }
    }

    // Cargar facultades
    public ObservableList<Faculty> loadFaculties(int universityId) throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/facultades/byUniversity?universidad_id=" + universityId)
                .addHeader("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar facultades: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<Faculty> faculties = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonFac = jsonArray.getJSONObject(i);
                Faculty faculty = new Faculty(
                        jsonFac.getInt("facultad_id"),
                        jsonFac.getInt("universidad_id"),
                        jsonFac.getString("nombre")
                );
                faculties.add(faculty);
            }
            return faculties;
        }
    }

    // Cargar departamentos
    public ObservableList<Department> loadDepartments(int facultyId) throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/departamentos?facultad_id=" + facultyId)
                .addHeader("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar departamentos: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<Department> departments = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonDep = jsonArray.getJSONObject(i);
                Department department = new Department(
                        jsonDep.getInt("departamento_id"),
                        jsonDep.getInt("facultad_id"),
                        jsonDep.getString("nombre")
                );
                departments.add(department);
            }
            return departments;
        }
    }

    // Cargar carreras
    public ObservableList<Career> loadCareers(int departmentId) throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/carreras?departamento_id=" + departmentId)
                .addHeader("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar carreras: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            ObservableList<Career> careers = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonCar = jsonArray.getJSONObject(i);
                Career career = new Career(
                        jsonCar.getInt("carrera_id"),
                        jsonCar.getInt("departamento_id"),
                        jsonCar.getString("nombre")
                );
                careers.add(career);
            }
            return careers;
        }
    }

  
    // Crear universidad
    public void createUniversity(String name) throws IOException {
        String jsonInputString = "{\"nombre\": \"" + name + "\"}";
        RequestBody body = RequestBody.create(
                jsonInputString,
                MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/universidades")
                .addHeader("Authorization", token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al crear universidad: " + response.code());
            }
        }
    }

    // Eliminar universidad
    public void deleteUniversity(int universityId) throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/universidades/" + universityId)
                .addHeader("Authorization", token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al eliminar universidad: " + response.code());
            }
        }
    }

    // Crear facultad
    public void createFaculty(String name, int universityId) throws IOException {
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

    // Eliminar facultad
    public void deleteFaculty(int facultyId) throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/facultades/" + facultyId)
                .addHeader("Authorization", token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al eliminar facultad: " + response.code());
            }
        }
    }

    // Crear departamento
    public void createDepartment(String name, int facultyId) throws IOException {
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

    // Eliminar departamento
    public void deleteDepartment(int departmentId) throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/departamentos/" + departmentId)
                .addHeader("Authorization", token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al eliminar departamento: " + response.code());
            }
        }
    }

    // Crear carrera
    public void createCareer(String name, int departmentId) throws IOException {
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

    // Eliminar carrera
    public void deleteCareer(int careerId) throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/carreras/" + careerId)
                .addHeader("Authorization", token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al eliminar carrera: " + response.code());
            }
        }
    }

 
}




    
    

