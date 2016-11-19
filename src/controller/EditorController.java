package controller;
/*Authors: Brandon Diaz-Abreu and Murtala Aliyu*/


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Song;


public class EditorController {
    
	@FXML
    private TextField titleField;
	@FXML
    private TextField artistField;
	@FXML
    private TextField albumField;
	@FXML
    private TextField yearField;
	
	private String originalTitle = "";
	private String originalArtist = "";
    
    private Stage dialogStage;
    private Song song;
    
 // Reference to the main application
    private SongLib songLib;
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param songLib
     */
    public void setSongLib(SongLib songLib) {
        this.songLib = songLib;
    }
    
    @FXML
    private void initialize(){
	
    }
    
    public void setDialogStage(Stage dialogStage){
	this.dialogStage = dialogStage;
    }
    
    public void setSong(Song song){
		this.song = song;
		if(song != null){
			titleField.setText(song.gettitle());
			artistField.setText(song.getartist());
			albumField.setText(song.getalbum());
			yearField.setText(song.getyear());
			
			originalTitle = song.gettitle();
			originalArtist = song.getartist();
		}
    }
    
    @FXML
    private void handleOk(){
		if(isInputValid()){
			if(this.song==null)
				this.song = new Song();
			song.settitle(titleField.getText());
			song.setartist(artistField.getText());
			if(albumField.getText() == null || albumField.getText().length() == 0){
				song.setalbum("");
			}
			else{
				song.setalbum(albumField.getText());
			}
			if(yearField.getText() == null || yearField.getText().length() == 0){
				song.setyear("");
			}
			else{
			    song.setyear(yearField.getText());
			}
			songLib.showPrimaryDisplay(this.song);
		}
    }
    
    @FXML
    private void handleCancel(){
    	if(this.song==null)
    		songLib.showPrimaryDisplay(null);
    	else
    		//return the unmodified song
    		songLib.showPrimaryDisplay(this.song);
    }
    
    private boolean isInputValid(){
		String errorMessage = "";
		if(titleField.getText() == null || titleField.getText().length() == 0){
		    errorMessage += "Please enter a song title!\n";
		}
		if (artistField.getText() == null || artistField.getText().length() == 0){
		            errorMessage += "This song has to have an artist!\n"; 
		}
		if(errorMessage.length() > 0){
			Alert alert = new Alert(AlertType.ERROR);
		    alert.initOwner(dialogStage);
		    alert.setTitle("Input Error");
		    alert.setHeaderText("Something was wrong with what you input:");
		    alert.setContentText(errorMessage);
		    alert.showAndWait();
			return false;
		}
		if(songLib != null){
			Object[] previouslyEnteredSongs = songLib.getSongData().toArray();
			for( Object tempCurrSong : previouslyEnteredSongs){
				Song current = (Song) tempCurrSong;
				if(current.gettitle().equalsIgnoreCase(originalTitle) && current.getartist().equalsIgnoreCase(originalArtist)){
					continue;
				}
				else if(current.gettitle().equalsIgnoreCase(titleField.getText()) && current.getartist().equalsIgnoreCase(artistField.getText())){
					errorMessage += "This song is already in your list!";
					Alert alert = new Alert(AlertType.ERROR);
				    alert.initOwner(dialogStage);
				    alert.setTitle("Input Error");
				    alert.setHeaderText("Something was wrong with what you input:");
				    alert.setContentText(errorMessage);
				    alert.showAndWait();
				    return false;
				}
			}
		}
		return true;
	}

	public void start(Stage primaryStage) {
	}
}
