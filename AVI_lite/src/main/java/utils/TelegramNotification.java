/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import okhttp3.*;
import java.io.IOException;

public class TelegramNotification {

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";
    private final String botToken = "7696769229:AAENvJtKTOHK5ZbYbsbr2dIJtF5yLcShPGk";
    
  

    // Método para enviar mensaje al profesor
    public void notifyProfessor(String professorChatId, String message) {
        String url = TELEGRAM_API_URL + botToken + "/sendMessage";
        String payload = "{\"chat_id\": \"" + professorChatId + "\", \"text\": \"" + message + "\"}";

        sendMessage(url, payload);
    }

    // Método para notificar al estudiante sobre el estado de su tarea
    public void notifyStudent(String studentChatId, String message) {
        String url = TELEGRAM_API_URL + botToken + "/sendMessage";
        String payload = "{\"chat_id\": \"" + studentChatId + "\", \"text\": \"" + message + "\"}";

        sendMessage(url, payload);
    }

    // Método privado para enviar mensajes a Telegram
    private void sendMessage(String url, String payload) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(payload, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error al enviar el mensaje: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("Mensaje enviado exitosamente.");
                } else {
                    System.out.println("Error al enviar el mensaje: " + response.message());
                }
            }
        });
    }

    // Método para enviar notificación de trabajo listo para revisión con estadísticas
    public void notifyProfessorWithStats(String professorChatId, int totalSubmitted, int lateSubmitted, int lowestGrade, int highestGrade, double averageGrade) {
        String message = String.format("El trabajo está listo para revisión. Estadísticas:\n" +
                "Total entregados: %d\n" +
                "Total entregados tarde: %d\n" +
                "Nota más baja: %d\n" +
                "Nota más alta: %d\n" +
                "Nota promedio: %.2f", 
                totalSubmitted, lateSubmitted, lowestGrade, highestGrade, averageGrade);

        notifyProfessor(professorChatId, message);
    }


    public void notifyStudentEnrollment(String studentChatId, String courseName) {
        String message = "Has sido matriculado en el curso: " + courseName;
        notifyStudent(studentChatId, message);
    }

    // Método para notificar al estudiante sobre el trabajo asignado
    public void notifyStudentAssignment(String studentChatId, String assignmentName, String dueDate) {
        String message = "Se te ha asignado el trabajo: " + assignmentName + ". Fecha de entrega: " + dueDate;
        notifyStudent(studentChatId, message);
    }

    // Método para notificar al estudiante sobre la calificación del trabajo
    public void notifyStudentGrade(String studentChatId, String assignmentName, int grade) {
        String message = "Tu trabajo " + assignmentName + " ha sido calificado. Obtuviste una calificación de: " + grade;
        notifyStudent(studentChatId, message);
    }

   
        /*  // Ejemplo de uso
      

        // Notificar al profesor con estadísticas
        telegramNotification.notifyProfessorWithStats("PROFESSOR_CHAT_ID", 50, 5, 60, 100, 85.5);

        // Notificar al estudiante sobre matriculación
        telegramNotification.notifyStudentEnrollment("STUDENT_CHAT_ID", "Matemáticas Avanzadas");

        // Notificar al estudiante sobre trabajo asignado
        telegramNotification.notifyStudentAssignment("STUDENT_CHAT_ID", "Trabajo de Algoritmos", "2024-12-15");

        // Notificar al estudiante sobre calificación
        telegramNotification.notifyStudentGrade("STUDENT_CHAT_ID", "Trabajo de Algoritmos", 85)  ;*/
    
}
