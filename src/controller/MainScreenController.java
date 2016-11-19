package controller;

/*Authors: Brandon Diaz-Abreu and Murtala Aliyu*/

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Song;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainScreenController {
    
        @FXML
        private TableView<Song> songTable;
        @FXML
        private TableColumn<Song, String> songList;

        @FXML
        private Label songTitle;
        @FXML
        private Label artistName;
        @FXML
        private Label albumTitle;
        @FXML
        private Label year;

        // Reference to the main application.
        private SongLib songLib;

        /**
         * The constructor.
         * The constructor is called before the initialize() method.
         */
        public MainScreenController() {
        }

        /**
         * TODO: Initializes the controller class. This method is automatically called
         * after the fxml file has been loaded.
         */
        @FXML
        private void initialize() {
        	//TODO:  Initialize the song table with the two columns.
            songList.setCellValueFactory(
                    cellData -> cellData.getValue().titleProperty());

            // Listen for selection changes and show the song details when changed.
            songTable.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> showSongDetails(newValue));
            //TODO: Make the first item be selected as soon as the application is loaded!
            songTable.getSelectionModel().clearAndSelect(0);
        }
        
        /**
         * Fills all text fields to show details about the song.
         * If the specified song is null, all text fields are cleared.
         * 
         * @param song the song or null
         */
        private void showSongDetails(Song song) {
            if (song != null) {
                // Fill the labels with info from the song object.
                songTitle.setText(song.gettitle());
                artistName.setText(song.getartist());
                albumTitle.setText(song.getalbum());
                year.setText(song.getyear());
            } else {
                // Song  is null, remove all the text.
                songTitle.setText("");
                artistName.setText("");
                albumTitle.setText("");
                year.setText("");
            }
        }

        /**
         * Is called by the main application to give a reference back to itself.
         * 
         * @param songLib
         */
        public void setSongLib(SongLib songLib) {
            this.songLib = songLib;

            // Add observable list data to the table
            songTable.setItems(songLib.getSongData());
            songTable.getSelectionModel().select(0);
        }
        
        /**
         * Called when the user clicks on the delete button.
         */
        @FXML
        private void handleDeleteSong() {
        	int selectedIndex = songTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
            	boolean okClicked = songLib.showDeleteDialog();
        	    if (okClicked) {
        	    	songTable.getItems().remove(selectedIndex);
        	    	//select next or previous song depending on position  	        
        	    	songTable.getSelectionModel().clearAndSelect(selectedIndex);
        	    }
            } else {
                // Nothing selected.
                Alert alert = new Alert(AlertType.WARNING);
                alert.initOwner(songLib.getPrimaryStage());
                alert.setTitle("Failed Delete");
                alert.setHeaderText("No Song Selected");
                alert.setContentText("Please select a song in the table before trying to delete anything.");
                alert.showAndWait();
            }
        }

    	/**
    	 * Called when the user clicks the new button. Opens a dialog to edit
    	 * details for a new song.
    	 */
    	@FXML
    	private void handleNewSong() {
    	    songLib.showSongEditDialog(null);
    	}
    	
    	/**
    	 * Called when the user clicks the edit button. Opens a dialog to edit
    	 * details for the selected song.
    	 */
    	@FXML
    	private void handleEditSong() {
    	   Song selectedSong = songTable.getSelectionModel().getSelectedItem();
    	    if (selectedSong != null)
    	        songLib.showSongEditDialog(selectedSong);
    	    else {
    	        // Nothing selected.
    	        Alert alert = new Alert(AlertType.WARNING);
    	        alert.initOwner(songLib.getPrimaryStage());
    	        alert.setTitle("Failed Edit");
    	        alert.setHeaderText("No Song Selected");
    	        alert.setContentText("Please select a song in the table before trying to edit anything.");
    	        alert.showAndWait();
    	    }
    	}
    	
    	void updateSongList(Song inputSong){
    		if(!songLib.getSongData().contains(inputSong))
    			songLib.getSongData().add(inputSong);
    		showSongDetails(inputSong);
    		
	    	Object[] previouslyEnteredSongs = songLib.getSongData().toArray();
    	    for(int i = 0; i < previouslyEnteredSongs.length; i++) {
    	    	if(previouslyEnteredSongs[i].equals(inputSong)) {
    	    		songTable.getSelectionModel().select(i);
    	    		return;
    	    	}
    	    }
    	}
    	
    }

