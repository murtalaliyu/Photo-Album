package controller;
/*Authors: Brandon Diaz-Abreu and Murtala Aliyu*/

import javafx.fxml.FXML;
import javafx.stage.Stage;


public class DeleteWarningController {
    
    private Stage dialogStage;
    private boolean okClicked = false;
    
    @FXML
    private void initialize(){
	
    }
    
    public void setDialogStage(Stage dialogStage){
	this.dialogStage = dialogStage;
    }
    
    public boolean isOkClicked(){
	return okClicked;
    }
    
    @FXML
    private void handleOk(){
    	okClicked = true;
    	dialogStage.close();
    }
    
    @FXML
    private void handleCancel(){
    	dialogStage.close();
    }
    
	public void start(Stage primaryStage) {
	}
}
