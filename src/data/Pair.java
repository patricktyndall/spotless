package data;

import java.util.ArrayList;
import java.util.List;

import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

public class Pair {
	
	public String song;
	public List<String> artists;
	
	public Pair(Track track){
		this.song = track.getName();
		this.artists = new ArrayList<String>();
		for(SimpleArtist a : track.getArtists()){
			artists.add(a.getName());
		}
		
		
		
	}
	
	public Pair(String song, List<String> artists){
		this.song = song;
		this.artists = artists;
		
	}

}
