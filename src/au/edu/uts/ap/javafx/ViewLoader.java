package au.edu.uts.ap.javafx;

import javafx.fxml.*;
import javafx.stage.*;
import javafx.scene.*;
import java.io.*;
import model.Pantry;
import storage.StorageManager;

public class ViewLoader {
    
    public static <T> void showStage(T model, String fxml, String title, Stage stage, StorageManager sm, Pantry pantry) throws IOException {
        FXMLLoader loader = new FXMLLoader(Controller.class.getResource(fxml), null, null,
                type -> {
                    try {
                    	@SuppressWarnings("unchecked")
                        Controller<T> controller = (Controller<T>)type.newInstance();
                        controller.model = model;
                        controller.stage = stage;
                        controller.sm = sm;
                        controller.pantry = pantry;
                        return controller;
                    } catch (Exception e) { throw new RuntimeException(e); }
                });
        Parent root = loader.load();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }
    
}