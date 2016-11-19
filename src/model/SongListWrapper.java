package model;
/*Authors: Brandon Diaz-Abreu and Murtala Aliyu*/

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

 //Helper class to wrap a list of persons. This is used for reading and saving the list
 //  of songs to and from an XML file.

@XmlRootElement(name = "songs")
public class SongListWrapper {

    private List<Song> songs;

    @XmlElement(name = "song")
    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}