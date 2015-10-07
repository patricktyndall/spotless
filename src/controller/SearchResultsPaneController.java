package controller;

import java.util.List;

import com.wrapper.spotify.models.Track;

public class SearchResultsPaneController extends AbstractPlaylistController{

	public SearchResultsPaneController(){
		super();
	}
	
	public List<Track> getSearchResults(String search){
		
		return library.searchForTrackTitle(search);
		
	}

	public void trackSelected(Track selectedItem) {
		library.addTrackToCurrentPlaylist(selectedItem);
		
	}
	
	

}
