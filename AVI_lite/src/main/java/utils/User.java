/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

public class User {
    
    
      private static User instance;
    
    protected int ID;
    protected String password;
    protected String token;
    protected String name;
    protected String lastName;
    protected String mail;
    protected String rol;
    protected String career;
    protected int universityID;

    public User(int ID, String password, String token, String name, String lastName, String mail, String rol,int universityID) {
        this.ID = ID;
        this.password = password;
        this.token = token;
        this.name = name;
        this.lastName = lastName;
        this.mail = mail;
        this.rol = rol;
        this.universityID = universityID;
    }
    

 

    

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getUniversityID() {
        return universityID;
    }

    public void setUniversityID(int universityID) {
        this.universityID = universityID;
    }

  

    public User(int ID, String token, String name, String lastName, String mail,String Rol) {
        this.ID = ID;
        this.token = token;
        this.name = name;
        this.lastName = lastName;
        this.mail = mail;
        this.rol  = rol;  
    }

 
    public User(int ID) {
        this.ID = ID;
    }

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    public String getToken(){
        return token;
    }
    
    public void setToken(String token){
        this.token = token;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public static User getInstance() {
        if (instance == null) {
            synchronized (User.class) {
                if (instance == null) {
                    instance = new User();
                }
            }
        }
        return instance;
    }
    
    
    @Override
    public String toString(){
return name;
     }
   
}
