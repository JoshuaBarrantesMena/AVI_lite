/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Josh
 */
public class AppUser {
    
    private static AppUser instance;
    private static User user;
    
    private AppUser(){
        
    }
    
    public static synchronized AppUser getInstance(){
        if(instance == null){
            instance = new AppUser();
        }
        return instance;
    }
    
    public synchronized User getUser(){
        return user;
    }
    
    public synchronized void setUser(User user){
        AppUser.user = user;
    }
}
