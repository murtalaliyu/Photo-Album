package model;

import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class User implements Serializable {
    private transient StringProperty login;
    private ObservableList<Picture> photoz;
    private ObservableList<Album> albumz;
    transient ArrayList<StringProperty> albums;
    private ArrayList<String> albumStrings;
    private static final long serialVersionUID = 1L;
    
  
            
/*Constructor*/
    public User(String loginName){
		// This can be handled by the UI: if(!UserList.contains(s)){}
		// Originally, both photos and albums should be empty for the new User
		    this.login = new SimpleStringProperty(loginName);
		    this.albums = new ArrayList<StringProperty>();
		    this.albumStrings = new ArrayList<String>();
		    this.photoz = FXCollections.observableArrayList();
		    this.albumz = FXCollections.observableArrayList();
        }

    public String getLogin(){
    	return this.login.getValue();
    }
    
    public void setLogin(String login){
    	this.login = new SimpleStringProperty(login);
    }
    
    public StringProperty loginProperty(){
    	return login;
    }
    
    public ObservableList<Album> getAlbums(){
    	return this.albumz;
    }
        
    public boolean createAlbum(String newName){
    	//Guaranteed not to be a duplicate
    	this.albumz.add(new Album(newName));
    	return true;
    }
    
    public void createAlbum(String name, ObservableList<Picture> results){
    	albumz.add(new Album(name, results) );
    }
    
    public void deleteAlbum(String oldName){
    	for(int i = 0; i<albumz.size(); ++i){
			if(albumz.get(i).getName().equals(oldName)){
				albumz.remove(i);
				return;
    		}
		}
    }
    
    public boolean checkNameAvailability(String newAlbumName){
    	for(Album tempAlbum : this.albumz){
    		if(tempAlbum.getName().equals(newAlbumName))
    			return false;
    	}
    	return true;
    }
    
    public void setAlbumStrings(){
    	if(albums == null){
    		albumStrings = new ArrayList<String>();
    		return;
    	}
    	albumStrings = new ArrayList<String>();
    	for(int b=0; b<albums.size(); ++b){
    		albumStrings.add(albums.get(b).getValue());
    	}
    }
    
    public void populateAlbums(){
    	if(albumStrings == null){
    		albums = new ArrayList<StringProperty>();
    		return;
    	}
    	albums = new ArrayList<StringProperty>();
    	for(int b=0; b<albumStrings.size(); ++b){
    		albums.add((new SimpleStringProperty(albumStrings.get(b) ) ) );
    	}
    }
    
    public Picture readPicture(String name) throws IOException, ClassNotFoundException{
	    File fileName = new File("../PhotoAlbum29/data/" + this.login + "/photos/" + name + ".txt");
	    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
	    Picture p = (Picture) ois.readObject();
	    ois.close();
	    return p;
	}
    
	public void writePicture(Picture p) throws IOException{
	    File fileName = new File("../PhotoAlbum29/data/" + this.login + "/photos/" + p.getName() + ".txt");
	    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
	    oos.writeObject(p);
	    oos.close();
	}
	
    /*
    public Date getOldestDateInAlbum(String album){
		Date oldestDate = null;
		boolean flag = false;
		
		for(int i = 0; i<photos.size(); i++){
		    if(photos.get(i).getAlbum().equals(album)){
				if(flag == false){
				    oldestDate = photos.get(i).getDate();
				    flag = true;
				}
				else{
				    if(photos.get(i).getDate().before(oldestDate)){
					oldestDate = photos.get(i).getDate();
				    }
				}
		    }
		}
		return oldestDate;
    }
    
    public Date getNewestDateInAlbum(String album){
		Date newestDate = null;
		boolean flag = false;
		for(int i = 0; i<photos.size(); i++){
		    if(photos.get(i).getAlbum().equals(album)){
				if(flag == false){
				    flag = true;
				    newestDate = photos.get(i).getDate();
				}
				else{
				    if(photos.get(i).getDate().before(newestDate)){
					newestDate = photos.get(i).getDate();
				    }
				}
		    }
		}
		return newestDate;
    }
    */
    
    //to search by a date range
    /*
    public ArrayList<Picture> searchByDateRange(Date startDate, Date endDate){
    	ArrayList<Picture> temp = new ArrayList<Picture>();
    	for(int i = 0; i<photos.size(); i++){
    	    if(photos.get(i).getCal().getTime().after(startDate) && photos.get(i).getCal().getTime().before(endDate)){
    		temp.add(photos.get(i));
    	    }
    	}
    	return temp;
    }
    */
    
    //to search by tags
    //need to add tag getter method
    
    public ObservableList<Picture> searchByTags (String query){
    	ObservableList<Picture> result = FXCollections.observableArrayList();
    	
    	/*Assume that the UI has formatted the input properly*/
		ArrayList<String> searchTags = new ArrayList<String>();
		
		String tempTag = "";
		boolean hasSpace = false;
		
		for(int i=0; i<query.length(); ++i){
			switch(query.charAt(i)){
			case ',':
				if(tempTag.length() > 0 && !searchTags.contains(tempTag)){
					searchTags.add(tempTag);
				}
				tempTag = "";
				break;
			case ' ':
				if(hasSpace){
					continue;
				}
				else
					hasSpace = true;
			default:
				tempTag+=Character.toString(query.charAt(i));
				if(query.charAt(i) != ' ')
					hasSpace = false;
			}
			//done with this character
		}
		//done setting search tags
		
		for(Album tempAlbum : albumz){
			for(Picture photo : tempAlbum.getPhotos()){
	    		String currentTags = photo.getTags();
	    		for(String queriedTag : searchTags){
	    			if(currentTags.contains(queriedTag)){
	    				result.add(photo);
	    				break;
	    			}
	    		}
	    		//finished searching this photo's tags
	    	}
			//finished searching all photos in this album
		}
    	return result;
    }
    
    public ObservableList<Picture> searchByDate(String dateQuery) {
    	ObservableList<Picture> result = FXCollections.observableArrayList();
    	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    	String individual = "";
    	
    	for(Album tempAlbum : albumz){
			for(Picture photo : tempAlbum.getPhotos()){
	    		individual = df.format(photo.getDate());
	    		System.out.printf("We are comparing %s to %s\n",individual,dateQuery);
	    		if (dateQuery.equals(individual)) {
	    			result.add(photo);
	    		}
	    	}
			//finished searching all photos in this album
		}

		System.out.println("observable list<pics> :" + result.size());
    	return result;
    }
    
    public ObservableList<Picture> getAllPhotoData(){
    	return photoz;
    }
    
    public String toString(){
    	return this.login.getValue();
    }
    
}