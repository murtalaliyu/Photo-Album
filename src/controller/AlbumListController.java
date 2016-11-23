package controller;
/**
 * This class controls the list of albums
 * @author Ashni
 * @author Brandon
 */

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
/*import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;*/

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Album;
import model.Picture;

public class AlbumListController {
	
    @FXML
    private TableView<Album> albumTable;
    @FXML
    private TableColumn<Album, String> albumList;
    
    @FXML
    private TextField searchBox;
    @FXML
    private Label albumTitle;
    @FXML
    private Label numberPhotos;
    @FXML
    private Label oldest;
    @FXML
    private Label dateRange;
    
    // Reference to the main application.
    private PhotoAlbum photoAlbum;

    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
        
        // Add observable list data to the table
        albumTable.setItems(photoAlbum.currentUser.getAlbums());
        albumTable.getSelectionModel().select(0);
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public AlbumListController() {
    }

    /**
     * TODO: Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	albumList.setCellValueFactory(
                cellData -> cellData.getValue().getNameProperty() );
       
        // Clear album details.
        showAlbumDetails(null);

        // Listen for selection changes and show the album details when changed.
        albumTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showAlbumDetails(newValue) );
    }
    
    /**
     * Fills all text fields to show details about the album.
     * If the specified album is null, all text fields are cleared.
     * 
     * @param album the album or null
     */
    private void showAlbumDetails(Album album) {
        if (album!=null) {
            // Fill the labels with info from the album.
        	numberPhotos.setText(Integer.toString( album.getPhotos().size() ) );
            albumTitle.setText(album.getName());
            
        	/*Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	String oldestDate = formatter.format(photoAlbum.currentUser.getOldestDateInAlbum(album));
        	if(oldestDate == null){
        		 oldest.setText("n/a");
                 dateRange.setText("n/a");
        	}
        	else{
        		String newestDate = formatter.format(photoAlbum.currentUser.getNewestDateInAlbum(album));
            	oldest.setText(oldestDate);
                dateRange.setText(oldestDate+" - "+newestDate);
        	}*/
        } else {
            // album is null, remove all the text.
            numberPhotos.setText("");
            oldest.setText("");
            albumTitle.setText("");
            dateRange.setText("");
        }
    }
    
    /**
     * This method handles logout
     * @param
     * @return
     */
    
    @FXML
    private void handleLogOut(){
    	photoAlbum.currentUser = null;
        photoAlbum.writeUserList();
    	photoAlbum.showLogin();
    }
    
    /** 
     * Occurs when the user presses the delete buttom
     * @param
     * @return
     * 
     */
    @FXML
    private void handleDelete() {
    	int selectedIndex = albumTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
        	String albumInfo = albumTable.getSelectionModel().getSelectedItem().getName();
            if(photoAlbum.showDeleteDialog()){
            	photoAlbum.currentUser.deleteAlbum(albumInfo);
            	albumTable.setItems(photoAlbum.getAlbumData());
	    		//initialize();
            }
	    } else {
	        // Nothing selected.
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(photoAlbum.getPrimaryStage()); 
	        alert.setTitle("No Selection!");
	        alert.setHeaderText("No album selected.");
	        alert.setContentText("Please select an album in the table.");
	        alert.showAndWait();
	    }
    }

	/** TODO
	 * Called when the user clicks the new button. Opens a dialog to edit
	 * details for a new song.
	 */
	@FXML
	private void handleCreate() {
		String inputName = photoAlbum.showAddUserDialog();
		if(inputName != null){
			if(photoAlbum.currentUser.checkNameAvailability(inputName)){
				photoAlbum.currentUser.createAlbum(inputName);
				albumTable.setItems(photoAlbum.currentUser.getAlbums());
			}
			else{
				Alert alert = new Alert(AlertType.WARNING);
		        alert.initOwner(photoAlbum.getPrimaryStage()); 
		        alert.setTitle("Invalid Album!");
		        alert.setHeaderText("You Can't Add That Album");
		        alert.setContentText("You already have an album by that name...");
		        alert.showAndWait();
			}
		}
	}
	
	/** TODO
	 * Called when the user clicks the edit button. Opens a dialog to edit
	 * details for the selected song.
	 */
	@FXML
	private void handleEdit() {
		int selectedIndex = albumTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
        	String inputName = photoAlbum.showAddUserDialog();
    		if(inputName != null){
    			if(photoAlbum.currentUser.checkNameAvailability(inputName)){
    	        	albumTable.getSelectionModel().getSelectedItem().setName(inputName);
    				albumTable.setItems(photoAlbum.getAlbumData());
    			}
    			else{
    				//User tried to enter a name that already exists
    				Alert alert = new Alert(AlertType.WARNING);
    		        alert.initOwner(photoAlbum.getPrimaryStage()); 
    		        alert.setTitle("Invalid Album!");
    		        alert.setHeaderText("You Can't Use That Name");
    		        alert.setContentText("You already have an album by that name...");
    		        alert.showAndWait();
    			}
            }
	    } else {
	        // Nothing selected.
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(photoAlbum.getPrimaryStage());
	        alert.setTitle("No Selection");
	        alert.setHeaderText("No Album Selected");
	        alert.setContentText("Please select an album in the table.");
	        alert.showAndWait();
	    }
	}
	
	@FXML
	private void handleOpen(){
		int selectedIndex = albumTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0)
        	photoAlbum.showPhotoList(albumTable.getSelectionModel().getSelectedItem());
        else {
	        // Nothing selected.
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(photoAlbum.getPrimaryStage());
	        alert.setTitle("No Selection");
	        alert.setHeaderText("No Album Selected");
	        alert.setContentText("Please select an album in the table.");
	        alert.showAndWait();
	    }
	}
	
	@FXML
    private void handleSearch(){
		String query = sanitizeInput(searchBox.getText() );
		String dateQuery = checkForDateSearch(searchBox.getText());
		
		//check if search was by tag or date
		if (dateQuery.length() > 0) {
			//search by date
			ObservableList<Picture> result = photoAlbum.currentUser.searchByDate(dateQuery);
			if (result.size() > 0) {
				photoAlbum.showSearchResults(result);
			} else {
				// Nothing was found
		        Alert alert = new Alert(AlertType.WARNING);
		        alert.initOwner(photoAlbum.getPrimaryStage());
		        alert.setTitle("Search Failure");
		        alert.setHeaderText("Nothing Found");
		        alert.setContentText("No matches were found.");
		        alert.showAndWait();
		        
			}
		} else {
			if(query.length() > 0){
				ObservableList<Picture> results = photoAlbum.currentUser.searchByTags(query);
				if(results.size() > 0)
					photoAlbum.showSearchResults(results);
				else{
					// Nothing was found
			        Alert alert = new Alert(AlertType.WARNING);
			        alert.initOwner(photoAlbum.getPrimaryStage());
			        alert.setTitle("Search Failure");
			        alert.setHeaderText("Nothing Found");
			        alert.setContentText("No matches were found.");
			        alert.showAndWait();
				}
			}
			else{
				// Nothing was entered (properly)
		        Alert alert = new Alert(AlertType.WARNING);
		        alert.initOwner(photoAlbum.getPrimaryStage());
		        alert.setTitle("Search Error");
		        alert.setHeaderText("Bad Query");
		        alert.setContentText("Please enter a valid search term!");
		        alert.showAndWait();
			}
		}
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
	 
	 private String checkForDateSearch(String dirtyInput) {
		 if (dirtyInput.length() != 10) {
			 return "";
		 }
		 String temp = dirtyInput.substring(0, 2); temp+= dirtyInput.substring(3, 5); temp += dirtyInput.substring(6, 10);
		 for (int i = 0; i < 8; i++) {
			 if (isInteger(temp.substring(i, i+1)) == false) {
				 return "";
			 }
		 }
		 if ((!dirtyInput.substring(2, 3).equals("/"))) {
			 if ((!dirtyInput.substring(5, 6).equals("/"))) {
				 return "";
			 }
		 }
		 
		 return dirtyInput;
	 }
	 
	 //determine if s is an integer
	 public static boolean isInteger(String s) {
		 try { 
			 Integer.parseInt(s); 
		 } catch (NumberFormatException e) { 
			 return false; 
		 } catch (NullPointerException e) {
			 return false;
		 }
		 	// only got here if we didn't return false
		 	return true;
	 }
}
