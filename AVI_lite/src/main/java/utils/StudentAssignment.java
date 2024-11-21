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
public class StudentAssignment {
private int IDAssignment;
private int IDStudent;
 private int IDDoc;
private boolean rated;
     private static StudentAssignment instance;
private int value;
 private LocalDateTime date;
 private String comment;

    public StudentAssignment(int IDAssignment, int IDStudent, int IDDoc, boolean rated, int value, LocalDateTime date,String comment) {
        this.IDAssignment = IDAssignment;
        this.IDStudent = IDStudent;
        this.IDDoc = IDDoc;
        this.rated = rated;
        this.value = value;
        this.date = date;
        this.comment=comment;
    }

    public StudentAssignment() {
    }

    public int getIDAssignment() {
        return IDAssignment;
    }

    public void setIDAssignment(int IDAssignment) {
        this.IDAssignment = IDAssignment;
    }

    public int getIDStudent() {
        return IDStudent;
    }

    public void setIDStudent(int IDStudent) {
        this.IDStudent = IDStudent;
    }

    public int getIDDoc() {
        return IDDoc;
    }

    public void setIDDoc(int IDDoc) {
        this.IDDoc = IDDoc;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
public String formatDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return date.format(formatter);
}
public static StudentAssignment getInstance() {
        if (instance == null) {
            synchronized (StudentAssignment.class) {
                if (instance == null) {
                    instance = new StudentAssignment();
                }
            }
        }
        return instance;
    }
                                    
}
