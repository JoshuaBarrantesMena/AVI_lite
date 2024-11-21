/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Josh
 */
public class Department {
    
    
    private int IDFaculty;
    private int id;
    private String name;

    public Department(int id,int IDFaculty,String name) {
        
        this.id = id;
        this.IDFaculty = IDFaculty;
        this.name = name;
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
   

    public int getIDFaculty() {
        return IDFaculty;
    }

    public void setIDFaculty(int IDFaculty) {
        this.IDFaculty = IDFaculty;
    }
    
    @Override
    public String toString(){
return name;
     }
    
    
}
