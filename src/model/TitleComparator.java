package model;
/*Authors: Brandon Diaz-Abreu and Murtala Aliyu*/

import java.util.Comparator;

public class TitleComparator  implements Comparator<Song> {
	    @Override
	    public int compare(Song a, Song b) {
	        if(a == null || b == null){
	        	return 0;
	        }
	    	return a.gettitle().compareToIgnoreCase(b.gettitle());
	    }
	}
