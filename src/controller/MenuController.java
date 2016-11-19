package controller;
/*AUthors: Brandon Diaz-Abreu and Murtala Aliyu*/


import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 */
public class MenuController {

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

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew() {
        songLib.getSongData().clear();
        songLib.setSongFilePath(null);
    }

    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(songLib.getPrimaryStage());

        if (file != null) {
            songLib.loadSongDataFromFile(file);
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File songFile = songLib.getSongFilePath();
        if (songFile != null) {
            songLib.saveSongDataToFile(songFile);
        } else {
        	songFile = new File("_yourLibrary.xml");
        	songLib.saveSongDataToFile(songFile);
        	//handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(songLib.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            songLib.saveSongDataToFile(file);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("SongLibrary");
        alert.setHeaderText("About");
        alert.setContentText("This application is brought to you by the very awesome Group 58: Brandon and Murtala.");
        alert.showAndWait();
    }
    
}