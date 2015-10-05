package controller;

import java.util.List;

import com.wrapper.spotify.models.Track;

public class SearchResultsPaneController extends AbstractPlaylistController{

	public List<Track> getSearchResults(String search){
		
		return library.searchForTrackTitle(search);
		
	}

}
