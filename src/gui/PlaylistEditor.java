package gui;

import controller.PlaylistPaneController;
import controller.SearchBoxController;
import controller.TrackPaneController;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
/*
 * Controller for the playlist-editing screen. 
 * Consists of three parts:
 * 1. A pane on the left of playlists, with the option to create a new one
 * 2. A search bar top right with fillable fields to enable the searching and adding of songs
 * 3. A list below the search bar of songs that have already been added
 * 
 * TODO need some way for the playlistPane to notify the playlistEditor of the current playlist
 */
public class PlaylistEditor extends Region{
	
	String currentPlaylist; // TODO
	
	private PlaylistPane playlistPane;
	private SearchBox searchBox;
	private TrackPane trackPane;
	
	private Group root;
	
	/* Display vars */
	static final double PLAYLISTPANE_X = 0.3;
	static final double SEARCHBOX_Y = 0.2;
	
	
	public PlaylistEditor(double x, double y){
		
		searchBox = new SearchBox(x*(1-PLAYLISTPANE_X), y*SEARCHBOX_Y);
		playlistPane = new PlaylistPane(x*PLAYLISTPANE_X, y);
		trackPane = new TrackPane(x*(1-PLAYLISTPANE_X), y*(1-SEARCHBOX_Y));
		
		searchBox.setController(new SearchBoxController());
		playlistPane.setController(new PlaylistPaneController());
		trackPane.setController(new TrackPaneController());
		
		playlistPane.display();
		trackPane.display();
		
		root = new Group();
		HBox hbox = new HBox();
		VBox vbox = new VBox();
		
		vbox.getChildren().add(searchBox);
		vbox.getChildren().add(trackPane);
		hbox.getChildren().add(playlistPane);
		hbox.getChildren().add(vbox);
		
		root.getChildren().add(hbox);
		
		this.getChildren().add(root);
		
	}
	
	public void setCurrentPlaylist(String title){
		this.currentPlaylist = title;
	}
	
	

}
