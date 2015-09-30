package controller;

import com.wrapper.spotify.Api;

import data.Parser;

public class Controller {

	static Library library;
	
	public Controller(){
		
	}

	public Controller(Api api){

		Controller.library = new Library(api);
	}

	public void makePlaylistFromFile(String filePath, String title){
		Parser parser = new Parser(filePath);
		library.createPlaylist(title, parser.parse());
	}


}
