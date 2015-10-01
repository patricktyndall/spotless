package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.Pair;

public class TrackPaneController extends AbstractPlaylistController{
	
	
	
	public TrackPaneController(){
		super();
	}
	
	public List<Pair> getTrackInfo(){
		if(library.playlistSet()){
			return library.getPlaylistTrackData(); 
		}
		else return new ArrayList<Pair>();

	}
	

}
