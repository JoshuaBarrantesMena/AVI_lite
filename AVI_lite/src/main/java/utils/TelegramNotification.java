/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import okhttp3.*;
import java.io.IOException;
import java.time.LocalDate;

public class TelegramNotification {

  

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";
    private final String botToken;
    private int studentChatId;
private int professorChatId;

    public TelegramNotification(String botToken, int studentChatId, int professorChatId) {
        this.botToken = botToken;
        this.studentChatId = studentChatId;
        this.professorChatId = professorChatId;
    }


  
    public void notifyProfessor(String message) {
        String url = TELEGRAM_API_URL + botToken + "/sendMessage";
        String payload = "{\"chat_id\": \"" + professorChatId + "\", \"text\": \"" + message + "\"}";

        sendMessage(url, payload);
    }


    public void notifyStudent( String message) {
        String url = TELEGRAM_API_URL + botToken + "/sendMessage";
        String payload = "{\"chat_id\": \"" + studentChatId + "\", \"text\": \"" + message + "\"}";

        sendMessage(url, payload);
    }


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

  
 


    public void notifyStudentEnrollment( String courseName) {
        String message = "Has sido matriculado en el curso: " + courseName;
        notifyStudent( message);
    }

    
    public void notifyStudentAssignment( String assignmentName, LocalDate dueDate) {
        String message = "Se te ha asignado el trabajo: " + assignmentName + ". Fecha de entrega: " + dueDate;
        notifyStudent( message);
    }

    // Método para notificar al estudiante sobre la calificación del trabajo
    public void notifyStudentGrade( String assignmentName, int grade) {
        String message = "Tu trabajo " + assignmentName + " ha sido calificado. Obtuviste una calificación de: " + grade;
        notifyStudent( message);
    }

   
        /*  // Ejemplo de uso
      

    
        // Notificar al estudiante sobre matriculación
        telegramNotification.notifyStudentEnrollment("STUDENT_CHAT_ID", "Matemáticas Avanzadas");

        // Notificar al estudiante sobre trabajo asignado
        telegramNotification.notifyStudentAssignment("STUDENT_CHAT_ID", "Trabajo de Algoritmos", "2024-12-15");

        // Notificar al estudiante sobre calificación
        telegramNotification.notifyStudentGrade("STUDENT_CHAT_ID", "Trabajo de Algoritmos", 85)  ;*/
    
}
