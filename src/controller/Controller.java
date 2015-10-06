package controller;

import com.wrapper.spotify.Api;

public class Controller {

	static Library library;
	
	public Controller(){
		
	}

	public Controller(Api api){

		Controller.library = new Library(api);
	}



}
