package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.User;

/**
 * This class controls the admin's methods
 * @author Murtala Aliyu
 * @author Brandon Diaz-Abreu
 *
 */

public class AdminController {
	
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> userList;
    @FXML
    private Label userID;
    @FXML
    private Label access;
    
    // Reference to the main application.
    private PhotoAlbum photoAlbum;
    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
        
        // Add observable list data to the table
        userTable.setItems(photoAlbum.getUserData());
        userTable.getSelectionModel().select(0);
    }
    
    @FXML
    private void initialize(){
    	userList.setCellValueFactory(
                cellData -> cellData.getValue().loginProperty());
        // Clear User details.
        showUserInfo(null);

        // Listen for selection changes and show the user details when changed.
        userTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showUserInfo(newValue));
    }
    
    /**
     * Fills all text fields to show details about the user.
     * If the specified user is null, all text fields are cleared.
     * 
     * @param selectedUser the user or null
     */
    private void showUserInfo(User selectedUser) {
        if (selectedUser != null) {
        	// Fill the labels with info from the user object.
        	userID.setText(selectedUser.getLogin());
        	access.setText("non-admin");
        } else {
            // User  is null, remove all the text.
            userID.setText("");
            access.setText("");
        }
    }
    
    @FXML
    private void handleDeleteUser() {
    	int selectedIndex = userTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
        	String userInfo = userTable.getSelectionModel().getSelectedItem().toString();
            if(photoAlbum.showDeleteDialog()){
            	String login = "";
			   for(int i = 0; i<photoAlbum.userList.size(); i++){
			    	login = photoAlbum.userList.get(i).toString();
			    	if(login.equals(userInfo)){
			    		photoAlbum.userList.remove(i);
			    		photoAlbum.userLogins.remove(userInfo);
			    		userTable.setItems(photoAlbum.getUserData());
			    	}
			   }
            }
	    } else {
	        // Nothing selected.
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(photoAlbum.getPrimaryStage());  //I got a null pointer on this line... TODO: could be useful
	        alert.setTitle("No Selection!");
	        alert.setHeaderText("No user selected.");
	        alert.setContentText("Please select a user in the table.");
	        alert.showAndWait();
	    }
    }
    
    @FXML
    private void handleNewUser() {
		String inputName = photoAlbum.showAddUserDialog();
		if(inputName != null){
			photoAlbum.userLogins.add(inputName);
			User tempUser = new User(inputName);
			//System.out.println("We are trying to add the name "+inputName);
		    //photoAlbum.getUserData().add(tempUser); We should only need this line. Not the two below!
		    photoAlbum.userList.add(tempUser);
			userTable.setItems(photoAlbum.getUserData());
		}
    }
    
    
    @FXML
    private void handleOk(){
        photoAlbum.writeUserList();
    	photoAlbum.showLogin();
    }
}
