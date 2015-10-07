package controller;

public class SearchBoxController extends AbstractPlaylistController{
	
	public boolean isPlaylistSelected(){
		return library.playlistSet();
	}

}
