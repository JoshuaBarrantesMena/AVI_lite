/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Josh
 */
public class Faculty extends University{
    private int id;
    private int IDUniversity;
private String name;

    public Faculty(int IDUniversity, int id, String name, int ID) {
      this.IDUniversity = IDUniversity;
        this.id = id;
        this.name = name;
    }

    public Faculty(int id,int ID, String name) {
        super(ID);
        this.id = id;
        this.name = name;
    }

    
    public int getID(){
        
return id;
}

    public int getIDUniversity() {
        return IDUniversity;
    }

    public void setIDUniversity(int IDUniversity) {
        this.IDUniversity = IDUniversity;
    }

    public Faculty() {
    }
    
    
    @Override
    public String toString(){
return name;
     }
    
    
    
}
