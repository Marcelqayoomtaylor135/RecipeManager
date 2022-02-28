package model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class CustomCell extends ListCell<PantryItem> {
    
    private Button actionBtn;
    private Label ingredientName;
    private GridPane pane;
    
    public CustomCell(){
        
        super();
        
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                System.out.println("item was selected");
            }       
        });
        
        actionBtn = new Button("X");
        actionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                System.out.println("Action: " + getItem());
            }
        });
        
        ingredientName = new Label();
        pane = new GridPane();
        pane.add(ingredientName, 0, 0);
        pane.add(actionBtn, 0, 1);
        setText(null);
        
    }
    
    @Override
    public void updateItem(PantryItem item, boolean empty) {
        
        super.updateItem(item, empty);
        setEditable(false);
        if (item != null){
                ingredientName.setText(item.getName());
                setGraphic(pane);
        } else {
            setGraphic(null);
        }
        
    }
    
}
