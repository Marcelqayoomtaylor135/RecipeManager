package controller;

import au.edu.uts.ap.javafx.Controller;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.SQLException;

public class ErrorController extends Controller<SQLException>{
   
    private ObservableList<SQLException> exceptions;
    
    public final Exception getException() { return model; }
   
   @FXML private void handleOkay() {
       stage.close();
   }
}
