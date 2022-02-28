package au.edu.uts.ap.javafx;



import javafx.stage.*;
import model.Pantry;
import storage.StorageManager;

public abstract class Controller<M> {
    
    protected M model;
    protected Stage stage;
    protected StorageManager sm;
    protected Pantry pantry;
    
}

// hi hi hi