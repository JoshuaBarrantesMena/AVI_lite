/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Josh
 */
public class Career  {
      private int id;
    private int IDDepartment;
    String name;

    public Career(int id, int IDDepartment, String name) {
        this.id = id;
        this.IDDepartment = IDDepartment;
        this.name = name;
    }
  

   
  
  
    
        public Career() {
      
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    public int getIDDepartment() {
        return IDDepartment;
    }

    public void setIDDepartment(int IDDepartment) {
        this.IDDepartment = IDDepartment;
    }
    
      
    @Override
    public String toString(){
return name;
     }
    
}
