package controller;

import java.util.ArrayList;
import java.util.List;

public class PlaylistPaneController extends AbstractPlaylistController {
	
	public PlaylistPaneController(){
		super();
	}
	
	public List<String> getPlaylistNames(){
		return library.getPlaylistNames();
	}
	
	public void makePlaylist(String title){
		library.makeNewPlaylist(title);
	}
	
	public void changePlaylist(String title){
		library.setCurrentPlaylist(title);
	}

}
