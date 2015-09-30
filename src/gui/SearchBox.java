package gui;

import javafx.scene.layout.HBox;
import controller.AbstractPlaylistController;
import controller.SearchBoxController;

public class SearchBox extends HBox{
	
	private SearchBoxController controller;

	
	public SearchBox(double x, double y) {
		// TODO Auto-generated constructor stub
	}

	private void makeDisplay(){
		
	}
	
	public void setController(AbstractPlaylistController c){
		this.controller = (SearchBoxController) c;
	}

}
