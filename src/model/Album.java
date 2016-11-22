package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Album{
    private String name;
    private StringProperty nameProperty;
    private ObservableList<Picture> photos;
    
    public Album (String name){
    	this.name = name;
    	this.nameProperty = new SimpleStringProperty(name);
    	photos = FXCollections.observableArrayList();
    }
    
    public Album (String name, ObservableList<Picture> firstPhotos){
    	this.name = name;
    	this.nameProperty = new SimpleStringProperty(name);
    	photos = firstPhotos;
    }
    
    public String getName(){
    	return this.name;
    }
    
    public StringProperty getNameProperty(){
    	return this.nameProperty;
    }
    
    public void setName(String newName){
    	this.name = newName;
    	this.nameProperty = new SimpleStringProperty(newName);
    }
    
    public ObservableList<Picture> getPhotos(){
    	return photos;
    }
}