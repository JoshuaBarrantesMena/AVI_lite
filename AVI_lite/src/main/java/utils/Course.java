/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
/**
 *
 * @author Josh
 */
public class Course {
             int id;
             int IDCareer;
             int teacherID;
            String name;
            String information;
            int totalFields;
             private static Course instance;
           
    public static Course getInstance() {
        if (instance == null) {
            synchronized (Course.class) {
                if (instance == null) {
                    instance = new Course();
                }
            }
        }
        return instance;
    }

    public Course() {
    }
    
    
    
    public Course(int id, int IDCareer, int teacherID, String name, String information, int totalFields) {
        this.id = id;
        this.IDCareer = IDCareer;
        this.teacherID = teacherID;
        this.name = name;
        this.information = information;
        this.totalFields = totalFields;
     
    }

    public Course(int id) {
        this.id = id;
    
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getTotalFields() {
        return totalFields;
    }

    public void setTotalFields(int totalFields) {
        this.totalFields = totalFields;
    }

    

  

    public int getIDCareer() {
        return IDCareer;
    }

    public void setIDCareer(int IDCareer) {
        this.IDCareer = IDCareer;
    }
    
    
       
    
    
    @Override
    public String toString(){
return name;
     }
   
}
