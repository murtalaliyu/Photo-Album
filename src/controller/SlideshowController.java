package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Album;
import model.Picture;

import java.text.SimpleDateFormat;

/**This controller handles what happens when a user starts a slideshow.
 * @author Murtala
 * @author Brandon
 *
 */

//maybe date shouldn't be added here

public class SlideshowController {

	@FXML
	private Label caption;
	@FXML
	private Label tags;
	@FXML
	private Label date;
	@FXML
	private ImageView thumbnail;
	private Image defaultImage = null;
	@FXML
	private ButtonBar buttonBar;
	
	//we'll use this a lot
	private PhotoAlbum photoAlbum;
	private Album currentAlbum;
	private Picture photo;
	private int numberOfPhotos, position;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
    //constructor
    public SlideshowController() {
    	
    }
    
    /**
     * This method sets the photoAlbum, current album and current photo so that we have a reference
     * to each.
     * @param currentAlbum, photo, photoAlbum
     */
    public void setState(Album currentAlbum, Picture photo, PhotoAlbum photoAlbum) {
    	this.photoAlbum = photoAlbum;
    	this.currentAlbum = currentAlbum;
    	this.photo = photo;
    	showPhotoDetails(this.photo);
    	numberOfPhotos = currentAlbum.getPhotos().size();
		position = currentAlbum.getPhotos().indexOf(photo);
    	
	    
    }
    
    @FXML
    private void initialize() {
    	if (thumbnail != null) {
    		defaultImage = thumbnail.getImage();
    	}
    }
    
    /**
     * Fills all text fields to show details about the photo.
     * If the specified album is null, all text fields and image are cleared.
     * 
     * @param current album, current photo, or null
     */
    private void showPhotoDetails(Picture photo) {
    	if (photo != null) {
    		//add labels
    		thumbnail.setImage(new Image(photo.getPhotoFile().toURI().toString()));
    		caption.setText(photo.getCaption());
    		tags.setText(photo.getTags());
    		date.setText(sdf.format(photo.getDate()));
    	
    	} else {
    		caption.setText("");
    		thumbnail.setImage(defaultImage);
    	}
    }
	
    /**
     * Displays the previous photo when previous button is clicked
     * 
     * @param none
     */
    @FXML
	private void handlePrevious() {
		if (position > 0) {
			position -= 1;
			System.out.println(position);
			thumbnail.setImage(new Image(currentAlbum.getPhotos().get(position).getPhotoFile().toURI().toString()));
			caption.setText(currentAlbum.getPhotos().get(position).getCaption());
    		tags.setText(currentAlbum.getPhotos().get(position).getTags());
    		date.setText(sdf.format(currentAlbum.getPhotos().get(position).getDate()));
    		
		} else if (position > 0 && position < (numberOfPhotos-1)) {
			buttonBar.getButtons().get(1).setDisable(false);
			buttonBar.getButtons().get(0).setDisable(false);
		} 
	}
	
    /**
     * Displays the previous photo when previous button is clicked
     * 
     * @param none
     */
	@FXML
	private void handleNext() {
		if (position < (numberOfPhotos-1)) {
			position += 1;
			System.out.println(position);
			thumbnail.setImage(new Image(currentAlbum.getPhotos().get(position).getPhotoFile().toURI().toString()));
			caption.setText(currentAlbum.getPhotos().get(position).getCaption());
    		tags.setText(currentAlbum.getPhotos().get(position).getTags());
    		date.setText(sdf.format(currentAlbum.getPhotos().get(position).getDate()));
    		
		} else if (position > 0 && position < (numberOfPhotos-1)) {
			buttonBar.getButtons().get(1).setDisable(false);
			buttonBar.getButtons().get(0).setDisable(false);
		} 
	}
    
	/**
     * Returns to the current album page
     * 
     * @param none
     */
	@FXML
	private void handleBackToPhotoList() {
		photoAlbum.showPhotoList(currentAlbum);
	}
}
