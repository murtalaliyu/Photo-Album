package controller;
/*Authors: Brandon Diaz-Abreu and Murtala Aliyu*/

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.Picture;

public class EditorController {
    
	@FXML
    private TextField captionField;
	@FXML
    private TextField tagsField;
    private Picture givenPhoto;
    private Album currentAlbum;
    
 // Reference to the main application
    private PhotoAlbum songLib;
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param songLib
     */
    public void setState(PhotoAlbum songLib, Album inputAlbum, Picture inputPhoto) {
        this.songLib = songLib;
        this.givenPhoto = inputPhoto;
        this.currentAlbum = inputAlbum;
        captionField.setText(givenPhoto.getCaption());
        tagsField.setText(givenPhoto.getTags());
    }
    
    @FXML
    private void initialize(){
	
    }
    
    @FXML
    private void handleOk(){
		givenPhoto.setCaption(this.captionField.getText());
		givenPhoto.setTags(sanitizeInput(tagsField.getText() ) );
		songLib.showPhotoList(currentAlbum);
    }
    
    @FXML
    private void handleCancel(){
    	songLib.showPhotoList(currentAlbum);
    }
    
    private String sanitizeInput(String dirtyInput){
		while(dirtyInput.charAt(0) == ' ' || dirtyInput.charAt(0) == ','){
			dirtyInput = dirtyInput.substring(1);
			if(dirtyInput.length() == 0)
				return "";
		}
		//the character at index zero must be valid because of the loop we just did!
		for(int i = dirtyInput.length()-1; i>0;--i){
			char endingChar = dirtyInput.charAt(i);
			if(endingChar == ' ' || endingChar == ',')
				dirtyInput = dirtyInput.substring(0, i);
			else
				break;
		}
		//this is the fully sanitized input
		return dirtyInput+",";
	}

	public void start(Stage primaryStage) {
	}
}
