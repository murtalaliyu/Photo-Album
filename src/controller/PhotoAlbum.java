package controller;
/*Authors: Brandon Diaz-Abreu and Murtala Aliyu*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
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
import model.Album;
import model.Picture;
import model.Song;
import model.SongListWrapper;
import model.TitleComparator;
import model.User;
import javafx.collections.*;
import javafx.event.EventHandler;

public class PhotoAlbum extends Application {
	private ObservableList<Album> albumData = FXCollections.observableArrayList();
	private ObservableList<User> userData = FXCollections.observableArrayList();
	public ArrayList<User> userList = new ArrayList<User>();  //find a way to make this private
	public User currentUser;
	public String fileName = "users.txt";
	public ArrayList<String> userLogins = new ArrayList<String>();
	public static String userDir = "../data";
	
	/**
	 * This function stores the list of users in a text file so that we can read them in upon
	 * restart of the application
	 * @author Brandon
	 * @author Murtala
	 */
	public void writeUserList(){
		File fileName = new File("../PhotoAlbum58/data/userData.txt");
		try{
			FileWriter fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0; i<userList.size(); i++){
				bw.write(userList.get(i).getLogin());
				bw.newLine();
			}
			bw.write("~"); //signifies end of file
			bw.close();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	
	
    private Stage openingStage;
    private BorderPane windowMenu;
    private ObservableList<Song> songData = FXCollections.observableArrayList();
    
    public ObservableList<Song> getSongData(){
    	if(!songData.isEmpty()){
    		FXCollections.sort(songData, new TitleComparator() );
    	}
    	return songData;
    }
    
    /**
    * Formats the user list into an observableArrayList that can be accessed by the
    * UI elements
    * @return userData
    * @author Brandon
    */
   public ObservableList<User> getUserData(){
 		userData = FXCollections.observableArrayList(userList);
 		return userData;
 	}
   
   /**
    * Formats the current user's album list into an observableArrayList that can be accessed
    * by the UI elements.
    * @return albumData
    * @author Brandon
    */
   public ObservableList<Album> getAlbumData(){
	   	albumData = currentUser.getAlbums();
	   	if(albumData == null)
	   		albumData = FXCollections.observableList(new ArrayList<Album>());
	 	return albumData;
 	}
    
    //Default Constructor
    public PhotoAlbum() {
    }

    @Override
    public void start(Stage openingStage) {
        this.openingStage = openingStage;
        this.openingStage.setTitle("Photos++");
        
     // Set the application icon.
        this.openingStage.getIcons().add(new Image("file:resources/photos.png") );
        
        initRootLayout();

        //showPrimaryDisplay(null);
        showLogin();
        
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
            loader.setLocation(PhotoAlbum.class.getResource("/view/WindowMenu.fxml"));
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
            loader.setLocation(PhotoAlbum.class.getResource("/view/AlbumList.fxml"));
            AnchorPane primaryDisplay = (AnchorPane) loader.load();
            // Set the song table and detail view into the center of the screen.
            windowMenu.setCenter(primaryDisplay);
            
            AlbumListController controller = loader.getController();
            controller.setPhotoAlbum(this);
            //if(returnedSong != null)
            	//controller.updateSongList(returnedSong);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PhotoAlbum.class.getResource("/view/StartPage.fxml"));
            AnchorPane startPage = (AnchorPane) loader.load();
            // Set the song table and detail view into the center of the screen.
            windowMenu.setCenter(startPage);
            
            StartPageController controller = loader.getController();
            controller.setPhotoAlbum(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showAdminPage() {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(PhotoAlbum.class.getResource("/view/AdminPage.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        windowMenu.setCenter(page);
	        
	        AdminController controller = loader.getController();
	        controller.setPhotoAlbum(this);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        return;
	    }
	}
    
    /**
     * Opens the Add User Dialog window
     * @return A string that represents the input name of the object the user is trying to add
     * @author Brandon
     */
    public String showAddUserDialog(){
    	try{
    	    FXMLLoader loader = new FXMLLoader();
    	    loader.setLocation(PhotoAlbum.class.getResource("/view/AddUser.fxml"));
    	    AnchorPane page = (AnchorPane) loader.load();
    	    
    	    //create the Dialog stage
	    	Stage dialogStage = new Stage();
	    	dialogStage.setTitle("Create New User");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(openingStage);
	        
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        AddNewUserController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        dialogStage.showAndWait();
	        return controller.returnValue();

    	} catch(IOException e){
    	    e.printStackTrace();
    	    return null;
    	}
    }
    
    public void showPhotoList(Album selectedAlbum) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PhotoAlbum.class.getResource("/view/PhotoList.fxml"));
            AnchorPane primaryDisplay = (AnchorPane) loader.load();
            // Set the song table and detail view into the center of the screen.
            windowMenu.setCenter(primaryDisplay);
            
            PhotoListController controller = loader.getController();
            controller.setState(this, selectedAlbum);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //slide show
    public void showSlideshow(Album zCurrentAlbum, Picture currentPicture) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(PhotoAlbum.class.getResource("/view/SlideshowPage.fxml"));
    		AnchorPane primaryDisplay = (AnchorPane) loader.load();
    		windowMenu.setCenter(primaryDisplay);
    		
    		//System.out.printf("This is the album: %s",currentAlbum.getName());
    		SlideshowController controller = loader.getController();
    		controller.setState(zCurrentAlbum, currentPicture, this);
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void showPhotoEditDialog(Album currentAlbum, Picture photoToEdit) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(PhotoAlbum.class.getResource("/view/Editor.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        windowMenu.setCenter(page);
	        
	        EditorController controller = loader.getController();
	        controller.setState(this,currentAlbum, photoToEdit);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        return;
	    }
	}
    
    public void showSearchResults(ObservableList<Picture> matches) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(PhotoAlbum.class.getResource("/view/SearchResults.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        windowMenu.setCenter(page);
	        
	        SearchResultsController controller = loader.getController();
	        controller.setState(this,matches);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        return;
	    }
	}
    
    public boolean showDeleteDialog() {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(PhotoAlbum.class.getResource("/view/DeleteWarning.fxml"));
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
		Preferences prefs = Preferences.userNodeForPackage(PhotoAlbum.class);
		String filePath = prefs.get("filePath", null);
		if(filePath != null)
		{
		    return new File (filePath);
		}
		else
		    return null;
    }
    
    public void setSongFilePath(File file){
		Preferences prefs = Preferences.userNodeForPackage(PhotoAlbum.class);
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