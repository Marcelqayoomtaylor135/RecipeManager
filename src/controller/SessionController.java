/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import au.edu.uts.ap.javafx.Controller;
import au.edu.uts.ap.javafx.ViewLoader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Recipe;
import model.User;

/**
 * FXML Controller class
 *
 * @author test
 */
public class SessionController extends Controller<User> {


    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void handleEnter(ActionEvent event) throws IOException {
        System.out.println("ENTERED");
        ViewLoader.showStage(model, "/view/dashboard.fxml", "Virtual Pantry - Recipes", stage, sm, pantry);
    }
    
}
