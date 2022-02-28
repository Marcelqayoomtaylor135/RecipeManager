
package model;

import java.io.Serializable;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User implements Serializable {

    private StringProperty email;
    private StringProperty username;
    private StringProperty password;
    private Pantry pantry;
    
    public User(String username, String email, String password){
        
        this.username = new SimpleStringProperty();
        this.username.set(username);
        this.email = new SimpleStringProperty();
        this.email.set(email);
        this.password = new SimpleStringProperty();
        this.password.set(password);
        pantry = new Pantry(username + "'s Pantry");
        
     }
    
    public User(String username, String email, String password, Pantry pantry){
        
        this.username = new SimpleStringProperty();
        this.username.set(username);
        this.email = new SimpleStringProperty();
        this.email.set(email);
        this.password = new SimpleStringProperty();
        this.password.set(password);
        this.pantry = pantry;
        
     }     
    
    public User(String username, Pantry pantry){
        
        this.username = new SimpleStringProperty();
        this.username.set(username);
        this.pantry = pantry;
        
    }
    
    public ReadOnlyStringProperty usernameProperty() {
        
        return username;
        
    }
    

    public String getUsername() {
        
        return username.getValue();
        
    }

    public void setUsername(String name) {
        
        this.username.set(name);
        
    }

    public ReadOnlyStringProperty emailProperty() {
        
        return email;
        
    }

    public String getEmail() {
        
        return email.getValue();
        
    }

    public void setEmail(String email) {
        
        this.email.set(email);
        
    }

    public ReadOnlyStringProperty passwordProperty() {
        
        return password;
        
    }

    public String getPassword() {
        
        return password.getValue();
        
    }

    public void setPassword(String password) {
        
        this.password.set(password);
        
    }
    
    public Pantry getPantry(){
        
        return pantry;
        
    }
    
    public boolean login(String username, String password) {
        
        return this.getUsername().equals(username) && this.getPassword().equals(password);
        
    }
    

    
}
