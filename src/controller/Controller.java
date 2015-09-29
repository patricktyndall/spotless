package controller;

import data.Parser;

public class Controller {

	Library library;
	

	public Controller(Library library){

		this.library = library;
	}
	
	public void launch() {
		// TODO Auto-generated method stub
		
	}

	public void makePlaylistFromFile(String filePath, String title){
		Parser parser = new Parser(filePath);
		library.createPlaylist(title, parser.parse());
	}


}
