package controller;
/*Authors: Brandon Diaz-Abreu and Murtala Aliyu*/

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Song;
import model.SongListWrapper;
import model.TitleComparator;
import javafx.collections.*;
import javafx.event.EventHandler;

public class SongLib extends Application {

    private Stage openingStage;
    private BorderPane windowMenu;
    private ObservableList<Song> songData = FXCollections.observableArrayList();
    
    public ObservableList<Song> getSongData(){
    	if(!songData.isEmpty()){
    		FXCollections.sort(songData, new TitleComparator() );
    	}
    	return songData;
    }
    
    //Default Constructor
    public SongLib() {
    }

    @Override
    public void start(Stage openingStage) {
        this.openingStage = openingStage;
        this.openingStage.setTitle("myTunes");
        
     // Set the application icon.
        this.openingStage.getIcons().add(new Image("file:resources/Cassette.png") );
        
        initRootLayout();

        showPrimaryDisplay(null);
        
        openingStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	public void handle(WindowEvent we) {
        		File outputFile = getSongFilePath();
        		if(outputFile != null)
        			saveSongDataToFile(outputFile);
        		else{
        			outputFile = new File("defaultLibrary.xml");
        			saveSongDataToFile(outputFile);
        		}
            }
        });
    }

    /**
     * Initializes the application's menu
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SongLib.class.getResource("/view/WindowMenu.fxml"));
            windowMenu = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(windowMenu);
            openingStage.setScene(scene);
           
            MenuController controller = loader.getController();
            controller.setSongLib(this);
            
            openingStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Try to load the most recent song list
        File file = getSongFilePath();
        if(file != null)
        {
            loadSongDataFromFile(file);
        }
    }

    /**
     * Sets the song list and detailed view within the main screen
     */
    public void showPrimaryDisplay(Song returnedSong) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SongLib.class.getResource("/view/MainScreen.fxml"));
            AnchorPane primaryDisplay = (AnchorPane) loader.load();
            // Set the song table and detail view into the center of the screen.
            windowMenu.setCenter(primaryDisplay);
            
            MainScreenController controller = loader.getController();
            controller.setSongLib(this);
            if(returnedSong != null)
            	controller.updateSongList(returnedSong);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showSongEditDialog(Song song) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(SongLib.class.getResource("/view/Editor.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        windowMenu.setCenter(page);
	        
	        EditorController controller = loader.getController();
	        controller.setSongLib(this);
	        controller.setSong(song);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        return;
	    }
	}
    
    public boolean showDeleteDialog() {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(SongLib.class.getResource("/view/DeleteWarning.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Delete Song?");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(openingStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        DeleteWarningController controller = loader.getController();
	        controller.setDialogStage(dialogStage);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        return controller.isOkClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
    
    public File getSongFilePath(){
		Preferences prefs = Preferences.userNodeForPackage(SongLib.class);
		String filePath = prefs.get("filePath", null);
		if(filePath != null)
		{
		    return new File (filePath);
		}
		else
		    return null;
    }
    
    public void setSongFilePath(File file){
		Preferences prefs = Preferences.userNodeForPackage(SongLib.class);
		if(file != null)
		{
		    prefs.put("filePath", file.getPath());
		}
		else{
		    prefs.remove("filePath");
		}
    }

    //Returns the main stage for new windows to reference.
    public Stage getPrimaryStage() {
        return openingStage;
    }
    
    public void loadSongDataFromFile(File file) {
	    try {
	        JAXBContext context = JAXBContext.newInstance(SongListWrapper.class);
	        Unmarshaller um = context.createUnmarshaller();

	        // Reading XML from the file and unmarshalling.
	        
	        SongListWrapper wrapper = (SongListWrapper) um.unmarshal(file);

	        songData.clear();
	        songData.addAll(wrapper.getSongs());

	        // Save the file path to the registry.
	        setSongFilePath(file);

	    } catch (Exception e) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Error loading data");
	        alert.setContentText("There was a problem reading this file:\n" + file.getPath());

	        alert.showAndWait();
	    }
	}
    
    public void saveSongDataToFile(File file) {
	    try {
	        JAXBContext context = JAXBContext.newInstance(SongListWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        //TODO: Wrapping the song data.
	        SongListWrapper wrapper = new SongListWrapper();
	        wrapper.setSongs(songData);

	        // Marshalling and saving XML to the file.
	        m.marshal(wrapper, file);

	        // Save the file path to the registry.
	        setSongFilePath(file);
	    } catch (Exception e) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Error saving data");
	        alert.setContentText("There was a problem saving to this file:\n" + file.getPath());

	        alert.showAndWait();
	    }
	}

    public static void main(String[] args) {
        launch(args);
    }
}