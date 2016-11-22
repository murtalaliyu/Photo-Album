package model;
/*Authors: Brandon Diaz-Abreu and Murtala Aliyu*/

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Song{

    private final StringProperty title;
    private final StringProperty artist;
    private final StringProperty album;
    private final StringProperty year;

    /**
     * Default constructor.
     */
    public Song() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param title
     * @param artist
     */
    public Song(String title, String artist) {
        this.title = new SimpleStringProperty(title);
        this.artist = new SimpleStringProperty(artist);

        // Some initial dummy data, just for convenient testing.
        this.album = new SimpleStringProperty(null);
        this.year = new SimpleStringProperty(null);
    }

    public String gettitle() {
        return title.get();
    }

    public void settitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getartist() {
        return artist.get();
    }

    public void setartist(String artist) {
        this.artist.set(artist);
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public String getalbum() {
        return album.get();
    }

    public void setalbum(String album) {
        this.album.set(album);
    }

    public StringProperty albumProperty() {
        return album;
    }

    public String getyear() {
        return year.get();
    }

    public void setyear(String year) {
        this.year.set(year);
    }

    public StringProperty yearProperty() {
        return year;
    }

}