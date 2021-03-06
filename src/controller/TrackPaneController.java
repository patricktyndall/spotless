package controller;

import java.util.ArrayList;
import java.util.List;

import com.wrapper.spotify.models.Track;

public class TrackPaneController extends AbstractPlaylistController{
	
	public TrackPaneController(){
		super();
	}
	
	public List<Track> getTrackInfo(){
		if(library.playlistSet()){
			return library.getPlaylistTrackData(); 
		}
		else return new ArrayList<Track>();

	}
	
	public boolean isPlaylistSelected(){
		return library.playlistSet();
	}
	

}
