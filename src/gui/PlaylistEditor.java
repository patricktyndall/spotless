package gui;

import com.wrapper.spotify.models.Track;

import controller.PlaylistPaneController;
import controller.SearchBoxController;
import controller.SearchResultsPaneController;
import controller.TrackPaneController;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
	
	private PlaylistPane playlistPane;
	private SearchBox searchBox;
	private SearchResultsPane searchResultsPane;
	private TrackPane trackPane; 
	
	private Group root;
	
	/* Display vars */
	static final double PLAYLISTPANE_X = 0.18;
	static final double SEARCHBOX_Y = 0.15;
	double width;
	double height;
	Stage stage;
	
	public PlaylistEditor(double x, double y){
		this.width = x;
		this.height = y;
		this.getStylesheets().add("GUIStyle.css");
		this.getStyleClass().add("playlist_editor");
		
		searchBox = new SearchBox(x*(1-PLAYLISTPANE_X), y*SEARCHBOX_Y);
		playlistPane = new PlaylistPane(x*PLAYLISTPANE_X, y);
		trackPane = new TrackPane(x*(1-PLAYLISTPANE_X), y*(1-SEARCHBOX_Y));
		searchResultsPane = new SearchResultsPane(250, 500); // TODO fix these sizes
		
		searchBox.setController(new SearchBoxController());
		playlistPane.setController(new PlaylistPaneController());
		trackPane.setController(new TrackPaneController());
		searchResultsPane.setController(new SearchResultsPaneController());
		
		playlistPane.display();
		searchResultsPane.display(); 
		
		playlistPane.setListener(this);
		searchBox.setListener(this);
		searchResultsPane.setListener(this);
		
		
		
		
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

	public void refresh() {
		trackPane.update(); 
	}

	public void search(String text) {
		searchResultsPane.hide();
		searchResultsPane.showResults(text);
	}

	public void trackSelected(Track selectedItem) {
		System.out.println(selectedItem.getName());
		// TODO add this to the playlist
		// force the trackPane to refresh, and scroll down
		
	}

	public void setStage(Stage stage) { // TODO best way to do this?
		this.stage = stage;
		this.searchResultsPane.setParentStage(stage);
		
	}
	
	

}
