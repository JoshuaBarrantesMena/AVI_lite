/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Josh
 */
public class University {
    protected int ID;
    protected String name;

    public University() {
    }
    
    public University(int ID, String name){
        this.ID = ID;
        this.name = name;
    }

    public University(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
     @Override
    public String toString(){

return name;
     }
    
    
}
