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

public class PhotoListController {
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
    private Album currentAlbum;
    
    Stage stage;
    
    public PhotoListController(){
    	
    }
    /**
     * This method sets the photo album so that we have a reference
     * to the current photo album.
     * @param photoAlbum, currentAlbum
     */
    
    public void setState(PhotoAlbum photoAlbum, Album currentAlbum) {
        this.photoAlbum = photoAlbum;
        this.currentAlbum = currentAlbum;
        photoList.setText(currentAlbum.getName());
        // Add observable list data to the table
        photoTable.setItems(currentAlbum.getPhotos());
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
    
    /**
     * This method displays the photo specified by the file sent in.
     * @param imageFile
     * @return an image of the photo
     *
	private ImageView displayPhoto (final File imageFile) {
	    // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
	    // The last two arguments are: preserveRatio, and use smooth (slower)
	    // resizing
	
	    ImageView imageView = null;
	    try {
	        final Image image = new Image(new FileInputStream(imageFile), 150, 0, true,
	                true);
	        imageView = new ImageView(image);
	        imageView.setFitWidth(150);
	        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
	
	            @Override
	            public void handle(MouseEvent mouseEvent) {
	
	                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
	
	                    if(mouseEvent.getClickCount() == 2){
	                        try {
	                            BorderPane borderPane = new BorderPane();
	                            ImageView imageView = new ImageView();
	                            Image image = new Image(new FileInputStream(imageFile));
	                            imageView.setImage(image);
	                            imageView.setStyle("-fx-background-color: BLACK");
	                            imageView.setFitHeight(stage.getHeight() - 10);
	                            imageView.setPreserveRatio(true);
	                            imageView.setSmooth(true);
	                            imageView.setCache(true);
	                            borderPane.setCenter(imageView);
	                            borderPane.setStyle("-fx-background-color: BLACK");
	                            Stage newStage = new Stage();
	                            newStage.setWidth(stage.getWidth());
	                            newStage.setHeight(stage.getHeight());
	                            newStage.setTitle(imageFile.getName());
	                            Scene scene = new Scene(borderPane,Color.BLACK);
	                            newStage.setScene(scene);
	                            newStage.show();
	                        } catch (FileNotFoundException e) {
	                            e.printStackTrace();
	                        }
	
	                    }
	                    
	                    if(mouseEvent.getClickCount() == 1){
	                    		//boolean okClicked = photoAlbum.showPhotoEditDialog();
	                    		if(okClicked){
	                    			//update Photo album accordingly
	                    		}
	                    		
	                    	}
	                    }
	                }
	        });
	    } catch (FileNotFoundException ex) {
	        ex.printStackTrace();
	    }
	    return imageView;
	}
	*/
    
	/**
	 * This method deletes the photo sent in by the file.
	 * @param imageFile
	 */
	
	@FXML
    private void handleDeletePhoto(){ //possible parameter: final File imageFile
    	int selectedIndex = photoTable.getSelectionModel().getSelectedIndex();
    	if(selectedIndex >= 0){
    		if(photoAlbum.showDeleteDialog() ){
    			currentAlbum.getPhotos().remove(photoTable.getSelectionModel().getSelectedItem());
            	photoTable.setItems(currentAlbum.getPhotos());
    		}
    	}
    	else{
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(photoAlbum.getPrimaryStage());
    		alert.setTitle("No photo selected!");
    		alert.setContentText("Please select a photo in the table.");
    		alert.showAndWait();
    	} 	
    }
    
    @FXML
    private void handleAddPhoto() {
    	FileChooser fileChooser = new FileChooser();
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
         
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        //do we have a placeholder to current album?
        //if yes, convert file to Picture
        //return Picture
        Date tempDate= new Date(file.lastModified());
        Picture p = new Picture(currentAlbum.getName(), tempDate, file);
        p.setZname(file.getName());
        currentAlbum.getPhotos().add(p);
    }
    
    @FXML
    private void handleManualSlideshow(){
    	//open slide show page
    	int selectedIndex = photoTable.getSelectionModel().getSelectedIndex();
    	if (selectedIndex >= 0) {
    		photoAlbum.showSlideshow(currentAlbum, photoTable.getSelectionModel().getSelectedItem());
    	} else {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(photoAlbum.getPrimaryStage());
    		alert.setTitle("No Selection");
    		alert.setHeaderText("No Photo Selected");
    		alert.setContentText("Please select a photo in the table");
    		alert.showAndWait();
    	}
    	
    }
    
    @FXML
    private void handleEdit(){
    	int selectedIndex = photoTable.getSelectionModel().getSelectedIndex();
    	if(selectedIndex >= 0){
    		photoAlbum.showPhotoEditDialog(currentAlbum, photoTable.getSelectionModel().getSelectedItem());
    	}
    	else{
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(photoAlbum.getPrimaryStage());
    		alert.setTitle("No photo selected!");
    		alert.setContentText("Please select a photo in the table.");
    		alert.showAndWait();
    	} 	
    }
    
    @FXML
    private void handleLogOut(){
    	photoAlbum.showPrimaryDisplay(null);
    }
    
   
}
