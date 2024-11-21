/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Josh
 */
public class Document {
private int IDDoc; 
 private int IDStudent; 
private String url; 
private String hashcode;  
 private static Document instance;

    public Document() {
    }

    public Document(int IDDoc, int IDStudent, String url, String hashcode) {
        this.IDDoc = IDDoc;
        this.IDStudent = IDStudent;
        this.url = url;
        this.hashcode = hashcode;
    }

    public int getIDDoc() {
        return IDDoc;
    }

    public void setIDDoc(int IDDoc) {
        this.IDDoc = IDDoc;
    }

    public int getIDStudent() {
        return IDStudent;
    }

    public void setIDStudent(int IDStudent) {
        this.IDStudent = IDStudent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }
      public static Document getInstance() {
        if (instance == null) {
            synchronized (Document.class) {
                if (instance == null) {
                    instance = new Document();
                }
            }
        }
        return instance;
    }
    
}
