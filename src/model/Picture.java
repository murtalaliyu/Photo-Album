package model;

import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;

/**
 * This class creates the Picture object in which we store Pictures.
 * @author ashnimehta
 * @author Brandon
 *
 */

public class Picture implements Serializable{
	
    private static final long serialVersionUID = 1L;
    private String name;
    private StringProperty zname;
    private String album;
    private ArrayList<String> tags;
    private String caption;
    private Calendar cal;
    private File photoFile;
    /*TODO: use a Date object so that photos can be sorted by date!*/
    
    /*stores the location of the directory containing the serialized photos*/
    public static String phoDir = "";

    /**
     * The constructor for the Picture object.
     * @param album, the album in which the Picture goes
     * @param tags, the linked list of tags associated with this Picture
     * @param caption, the caption for this picture
     * @param lastModified, the date when the picture was last modified
     * @param photoFile, location where the photo is stored on disk
     */
    public Picture(String album, Date lastModified, File photoFile) {
	this.album = album;
	this.tags = new ArrayList<String>();
	this.tags.add(album);
	this.caption = "No Caption";
	cal = new GregorianCalendar();
	cal.setTime(lastModified);
	cal.set(Calendar.MILLISECOND,0);
	this.photoFile = photoFile;
	this.zname = new SimpleStringProperty("basic zname");
	
	/*try {
		writePicture(this);
	} catch (IOException e) {
		
	}*/
}
    
    public StringProperty getZname(){
    	return this.zname;
    }
    
    public void setZname(String newZname){
    	this.zname.set(newZname);
    }
    
    /**
     * Gets the date of the photo
     * @return
     */
    public Date getDate(){
    	return this.cal.getTime();
    }
    
    //TODO: Add setter methods and make modification to the PhotoAlbumController
    // and the EditDialogBoxController so that they will compile.

	
	public Calendar getCal(){
		return this.cal;
	}
	
	/**
	 * Changes the name of the albums
	 * @param newAlbum, the new name of the album
	 */
	
	public void changeAlbum(String newAlbum){
	    //if the album is a valid album for the user
	    this.album = newAlbum;
	}
	
	/**
	 * Gets the string representation of the album this picture is in
	 * @return
	 */
	
	public String getAlbum(){
	    return this.album;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getTags(){
		String allTags = "";
		for(String tag: tags){
			allTags+=tag+",";
		}
		return allTags;
	}
	
	public void setTags(String tagList){
	    /*Assume that the UI has formatted the input properly so that there are no beginning spaces or commas
	     * and the input string ends with a comma
	     * */
		this.tags = new ArrayList<String>();
		if(tagList.length() < 1)
			return;
		
		String tempTag = "";
		boolean hasSpace = false;
		
		for(int i=0; i<tagList.length(); ++i){
			switch(tagList.charAt(i)){
			case ',':
				if(tempTag.length() > 0 && !this.tags.contains(tempTag)){
					this.tags.add(tempTag);
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
				tempTag+=Character.toString(tagList.charAt(i));
				if(tagList.charAt(i) != ' ')
					hasSpace = false;
			}
			//done with this character
		}
		//done setting tags
	    return;
	}
	
	public File getPhotoFile(){
		return this.photoFile;
	}
	
	public void setCaption(String s){
	    this.caption = s;
	    return;
	}
	
	public String getCaption(){
	    return this.caption;
	}
	
	/** TODO: why?
	 * Deletes the photo currently being handled
	
	public void deletePhoto(){
	    this.album = null;
	    this.tags = null;
	    this.caption = null;
	    this.cal = null;
	    this.photoFile = null;
	}
	*/
	
}