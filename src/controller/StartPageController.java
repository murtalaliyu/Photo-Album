package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class handles the start page
 * @author Murtala Aliyu
 * 
 */

public class StartPageController {
	 // Reference to the main application.
    private PhotoAlbum photoAlbum;
   /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param photoAlbum
     */
    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
    }
	
	@FXML
	private TextField usernameField;
	
	/**
	 * This method handles if a user wants to exit the application
	 * @throws IOException
	 */
	
	@FXML
	private void handleExit() throws IOException{
		/*photoAlbum.writeUserList();
		for(int i = 0; i<photoAlbum.userList.size(); i++){
			photoAlbum.userList.get(i).setAlbumStrings();
			photoAlbum.writeUser(photoAlbum.userList.get(i));
		}
		System.exit(0);*/
	}
	
	/**
	 * This method handles Login for the user
	 * @author Brandon
	 */
	
	@FXML
	private void handleLogin(){
		int i = 0;
		if(usernameField.getText().equals("admin")){
			photoAlbum.showAdminPage();
		}
		else{
			/*This is a non-admin user
			 * if the saved list of logins contains the input login info,
			 * then we will loop through the list of users and find that user 
			 */
			if(photoAlbum.userLogins.contains(usernameField.getText())){
				System.out.println("We found a match on user "+usernameField.getText());
				while(i < photoAlbum.userList.size()){
					if(photoAlbum.userList.get(i).getLogin().equals(usernameField.getText())){
						photoAlbum.currentUser = photoAlbum.userList.get(i);
						break;
					}
					++i;
				}
			    photoAlbum.showPrimaryDisplay(null);
		    }
			else{
		    	Alert alert = new Alert(AlertType.ERROR);
			    alert.initOwner(photoAlbum.getPrimaryStage());
			    alert.setTitle("Invalid User");
			    alert.setHeaderText("The information you entered was incorrect");
			    alert.setContentText("That's not a valid user!");
			    alert.showAndWait();
		    }
		}
	}
	
}