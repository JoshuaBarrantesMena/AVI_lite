package utils;

import java.io.IOException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Course;
import utils.User;

/**
 * Clase para manejar hilos relacionados con la funcionalidad de StudentEnrollController.
 */
public class StudentEnrollThread extends Thread {
    private final String token;
    private final ObservableList<Course> coursesList;
    private final OkHttpClient client;
    private final int universityId;
    private final Course enrollCourse;
    private final boolean isEnrollmentTask;

    // Constructor para cargar cursos
    public StudentEnrollThread(String token, ObservableList<Course> coursesList) {
        this.token = token;
        this.coursesList = coursesList;
        this.client = new OkHttpClient();
        this.universityId = User.getInstance().getUniversityID();
        this.enrollCourse = null; // No se usa en este caso
        this.isEnrollmentTask = false; // Carga de cursos
    }

    // Constructor para inscribir a un estudiante
    public StudentEnrollThread(String token, Course enrollCourse) {
        this.token = token;
        this.enrollCourse = enrollCourse;
        this.client = new OkHttpClient();
        this.coursesList = null; // No se usa en este caso
        this.universityId = 0; // No se usa en este caso
        this.isEnrollmentTask = true; // Inscripción
    }

    @Override
    public void run() {
        try {
            if (isEnrollmentTask) {
                enrollStudent();
            } else {
                loadCourses();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCourses() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/cursos/universidad/" + universityId)
                .addHeader("Authorization", token)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al cargar cursos: " + response.code());
            }
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonUni = jsonArray.getJSONObject(i);
                Course course = new Course(
                        jsonUni.getInt("curso_id"),
                        jsonUni.getInt("carrera_id"),
                        jsonUni.getInt("profesor_id"),
                        jsonUni.getString("nombre"),
                        jsonUni.getString("informacion"),
                        jsonUni.getInt("campos_totales")
                );
                Platform.runLater(() -> coursesList.add(course)); // Actualizar en el hilo de JavaFX
            }
        }
    }

    private void enrollStudent() throws IOException {
        String jsonInputString = String.format(
                "{\"curso_id\": %d, \"usuario_id\": %d}",
                enrollCourse.getId(),
                User.getInstance().getID()
        );

        RequestBody body = RequestBody.create(
                jsonInputString,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("http://localhost:5000/api/inscripciones/inscribir/")
                .addHeader("Authorization", token)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Platform.runLater(() -> System.out.println("Inscripción exitosa"));
            } else {
                Platform.runLater(() -> System.out.println("Error en la conexión: " + response.code()));
            }
        }
    }
}
