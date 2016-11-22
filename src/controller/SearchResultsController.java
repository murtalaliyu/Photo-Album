package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Picture;
import model.Song;
import model.Album;
import model.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

/**This controller handles what happens when a user opens an album.
 * @author Brandon
 * @author Murtala
 *
 */

public class SearchResultsController {
	@FXML
    private TableView<Picture> photoTable;
    @FXML
    private TableColumn<Picture, String> photoList;
    @FXML
    private Label photoCaption;
    @FXML
    private ImageView thumbnail;
    private Image defaultImage = null;
    
    private PhotoAlbum photoAlbum;
    ObservableList<Picture> results;
    
    Stage stage;
    
    public SearchResultsController(){
    	
    }
    /**
     * This method sets the photo album so that we have a reference
     * to the current photo album.
     * @param photoAlbum
     */
    
    public void setState(PhotoAlbum photoAlbum, ObservableList<Picture> inputPhotos) {
        this.photoAlbum = photoAlbum;
        this.results=inputPhotos;
        // Add observable list data to the table
        photoTable.setItems(this.results);
        photoTable.getSelectionModel().select(0);
    }
    
    @FXML
    private void initialize(){
    	if(thumbnail != null)
        	defaultImage = thumbnail.getImage();
    	if(photoList != null){
        	photoList.setCellValueFactory(
        		    cellData -> cellData.getValue().getZname());
        }

        // Listen for selection changes and show the song details when changed.
        photoTable.getSelectionModel().selectedItemProperty().addListener(
        		(observable, oldValue, newValue) -> showPhotoDetails(newValue));
        //TODO: Make the first item be selected as soon as the application is loaded!
        photoTable.getSelectionModel().clearAndSelect(0);
    }
    
    
    /**
     * Fills all text fields to show details about the song.
     * If the specified song is null, all text fields are cleared.
     * 
     * @param song the song or null
     */
    private void showPhotoDetails(Picture photo) {
        if (photo != null) {
            // Fill the labels with info from the song object.
            photoCaption.setText(photo.getCaption());
            thumbnail.setImage(new Image(photo.getPhotoFile().toURI().toString() ) );
        } else {
            // Song  is null, remove all the text.
        	photoCaption.setText("");
        	thumbnail.setImage(defaultImage);
        }
    }
    
    @FXML
    private void createAlbumFromResults(){
    	String inputName = photoAlbum.showAddUserDialog();
		if(inputName != null){
			if(photoAlbum.currentUser.checkNameAvailability(inputName)){
		    	photoAlbum.currentUser.createAlbum(inputName, results);
		    	photoAlbum.showPrimaryDisplay(null);
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
	
    @FXML
    private void handleManualSlideshow(){
    	//unknown
    }
    
    @FXML
    private void handleLogOut(){
    	photoAlbum.showPrimaryDisplay(null);
    }
    
   
}
