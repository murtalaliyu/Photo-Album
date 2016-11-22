package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Album;
import model.Picture;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	private String dateAdded = "";
	
    //constructor
    public SlideshowController() {
    	
    }
    
    public void setState(Album currentAlbum, Picture photo, PhotoAlbum photoAlbum) {
    	this.photoAlbum = photoAlbum;
    	this.currentAlbum = currentAlbum;
    	this.photo = photo;
    	showPhotoDetails(this.photo);
    	numberOfPhotos = currentAlbum.getPhotos().size();
		position = currentAlbum.getPhotos().indexOf(photo);
    	
		Date myDate = Calendar.getInstance().getTime();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.hhmmss");
	    String todaysDate = sdf.format(myDate); 
	    dateAdded += todaysDate.substring(4, 6);
	    dateAdded += "/";
	    dateAdded += todaysDate.substring(6, 8);
	    dateAdded += "/";
	    dateAdded += todaysDate.substring(0, 4);
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
    		date.setText(dateAdded);
    	
    	} else {
    		caption.setText("");
    		thumbnail.setImage(defaultImage);
    	}
    }
	
    @FXML
	private void handlePrevious() {
		if (position > 0) {
			position -= 1;
			System.out.println(position);
			thumbnail.setImage(new Image(currentAlbum.getPhotos().get(position).getPhotoFile().toURI().toString()));
			caption.setText(currentAlbum.getPhotos().get(position).getCaption());
    		tags.setText(currentAlbum.getPhotos().get(position).getTags());
    		date.setText(dateAdded);
    		
		} else if (position > 0 && position < (numberOfPhotos-1)) {
			buttonBar.getButtons().get(1).setDisable(false);
			buttonBar.getButtons().get(0).setDisable(false);
		} 
	}
	
	@FXML
	private void handleNext() {
		if (position < (numberOfPhotos-1)) {
			position += 1;
			System.out.println(position);
			thumbnail.setImage(new Image(currentAlbum.getPhotos().get(position).getPhotoFile().toURI().toString()));
			caption.setText(currentAlbum.getPhotos().get(position).getCaption());
    		tags.setText(currentAlbum.getPhotos().get(position).getTags());
    		date.setText(dateAdded);
    		
		} else if (position > 0 && position < (numberOfPhotos-1)) {
			buttonBar.getButtons().get(1).setDisable(false);
			buttonBar.getButtons().get(0).setDisable(false);
		} 
	}
    
	@FXML
	private void handleBackToPhotoList() {
		photoAlbum.showPhotoList(currentAlbum);
	}
}
