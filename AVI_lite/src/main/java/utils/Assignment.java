/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author Usuario
 */
public class Assignment {
    
     private static Assignment instance;
    private int assignmetnId;
    private String title;
    private int teacherId;
    private int courseId;
    private int percentageValue;
     private LocalDateTime limitDate;
   private String description;

    public Assignment() {
    }

    public Assignment(int assignmetnId, String title, int teacherId, int courseId, int percentageValue, LocalDateTime limitDate, String description) {
        this.assignmetnId = assignmetnId;
        this.title = title;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.percentageValue = percentageValue;
        this.limitDate = limitDate;
        this.description = description;
    }

    public int getAssignmetnId() {
        return assignmetnId;
    }

    public void setAssignmetnId(int assignmetnId) {
        this.assignmetnId = assignmetnId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getPercentageValue() {
        return percentageValue;
    }

    public void setPercentageValue(int percentageValue) {
        this.percentageValue = percentageValue;
    }

    public LocalDateTime getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(LocalDateTime limitDate) {
        this.limitDate = limitDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    public String formatDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return limitDate.format(formatter);
}
    
    public static Assignment getInstance() {
        if (instance == null) {
            synchronized (Assignment.class) {
                if (instance == null) {
                    instance = new Assignment();
                }
            }
        }
        return instance;
    }
     @Override
    public String toString(){
return title;
     }
    
    
}
