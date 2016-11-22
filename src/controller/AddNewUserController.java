package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AddNewUserController {
    
   private Stage dialogStage;
   private String userInput;
   
   @FXML
   private TextField inputField;
    
    public void setDialogStage(Stage dialogStage){
    	this.dialogStage = dialogStage;
    }
    
    public String returnValue(){
    	return userInput;
    }
    
    @FXML
    private void handleOk(){
    	if(inputField.getText().length() < 1){
    		//They didn't type anything
    		return;
    	}
    	if(inputField.getText().indexOf(' ') > -1){
    		//The given name has whitespace!
    		Alert alert = new Alert(AlertType.ERROR);
		    alert.initOwner(dialogStage);  //was dialogStage
		    alert.setTitle("Invalid Input");
		    alert.setHeaderText("The information you entered was incorrect");
		    alert.setContentText("This program does not allow whitespace in object names!");
		    alert.showAndWait();
    	}
    	else{
    		//we're good to go
    		userInput = inputField.getText();
    		dialogStage.close();
    	}
    }
}