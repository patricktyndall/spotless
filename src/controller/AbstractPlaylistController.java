package controller;

import com.wrapper.spotify.Api;

public class AbstractPlaylistController{
	
	static Library library;

	public static void setApi(Api api){
		AbstractPlaylistController.library = new Library(api);
	}
}
