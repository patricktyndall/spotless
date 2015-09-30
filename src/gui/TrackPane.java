package gui;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import controller.AbstractPlaylistController;
import controller.SearchBoxController;
import controller.TrackPaneController;
import data.Pair;

public class TrackPane extends VBox{
	
	private TrackPaneController controller;
	
	ListView<Pair> list;
	double width;
	double height;
	
	public TrackPane(TrackPaneController controller){
		
		this.controller = controller;
		
	}
	
	public TrackPane(double x, double y) {
		this.width = x;
		this.height = y;
	}

	public void display(){
		
		makeList();
		
	}
	
	private void makeList(){
		list = new ListView<Pair>();
		List<Pair> items = controller.getTrackInfo();
		list.setItems(FXCollections.observableArrayList(items));
		list.setPrefWidth(width);
		list.setPrefHeight(height);

		
		list.setCellFactory(new Callback<ListView<Pair>, 
	            ListCell<Pair>>() { 
					public ListCell<Pair> call(ListView<Pair> param) {
						return new TrackListCell();
					}

	            }
	        );
		

		this.getChildren().add(list);
	}
	
	public void setController(AbstractPlaylistController c){
		this.controller = (TrackPaneController) c;
	}
	
	public class TrackListCell extends ListCell<Pair> {
		
	    private static final String CACHE_LIST_FOUND_CLASS = "cache-list-found";
	    private static final String CACHE_LIST_NOT_FOUND_CLASS = "cache-list-not-found";
	    private static final String CACHE_LIST_NAME_CLASS = "cache-list-name";
	    private static final String CACHE_LIST_DT_CLASS = "cache-list-dt";
	    private static final String CACHE_LIST_ICON_CLASS = "cache-list-icon";
	    private static final String FONT_AWESOME = "FontAwesome";
	 
	    private HBox hbox = new HBox();
	    private Label track = new Label();
	    private Label artist = new Label();
	   //  private Label dt = new Label();
	 
	    public TrackListCell() {
	        configureHbox();        
	        // configureIcon();
	       //  configureName();
	        //configureDifficultyTerrain();
	        addControlsToGrid();            
	    }
	 
	    private void configureHbox() {
	        hbox.setSpacing(width/2);
	        
	        // hbox.setPadding(new Insets(0, 10, 0, 10));
	    }
	 
	   /* private void configureIcon() {
	        icon.setFont(Font.font(FONT_AWESOME, FontWeight.BOLD, 24));
	        icon.getStyleClass().add(CACHE_LIST_ICON_CLASS);
	    }
	 */
	    private void configureTrack() {
	        track.getStyleClass().add(CACHE_LIST_NAME_CLASS);
	    }
	
	 
	    private void addControlsToGrid() {
	    	hbox.getChildren().add(track);                    
	    	hbox.getChildren().add(artist);        
	    }
	 
	   @Override
	    public void updateItem(Pair pair, boolean empty) {
	        super.updateItem(pair, empty);
	        if (empty) {
	            clearContent();
	        } else {
	            addContent(pair);
	        }
	    }
	 
	    private void clearContent() {
	        setText(null);
	        setGraphic(null);
	    }
	 
	   private void addContent(Pair pair) {
	        setText(null);
	        track.setText(pair.song);
	        String a = "";
	        for(String s : pair.artists)
	        	a = a.concat(s+", "); // TODO this will have a trailing comma
	        
	        artist.setText(a);
	       
	        // setStyleClassDependingOnFoundState(cache);        
	        setGraphic(hbox);
	    }
	 
	   /* private void setStyleClassDependingOnFoundState(TrackPane trackPane) {
	        if (CacheUtils.hasUserFoundCache(cache, new Long(3906456))) {
	            addClasses(this, CACHE_LIST_FOUND_CLASS);
	            removeClasses(this, CACHE_LIST_NOT_FOUND_CLASS);
	        } else {
	            addClasses(this, CACHE_LIST_NOT_FOUND_CLASS);
	            removeClasses(this, CACHE_LIST_FOUND_CLASS);
	        }
	    }*/
	}

}
