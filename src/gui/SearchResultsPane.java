package gui;

import java.util.ArrayList;
import java.util.List;

import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SearchResultsPane extends ListView{
	
	Stage stage = new Stage();
	List<Track> list;
	// VBox content;
	double width;
	double height;
	
	
	public SearchResultsPane(double x, double y){
		list = new ArrayList<Track>();
		// content = new VBox();
		width = x;
		height = y;
		setupFactory();
	}
	
	private void setupFactory(){
		this.setCellFactory(new Callback<ListView<Track>, 
	            ListCell<Track>>() { 
					public ListCell<Track> call(ListView<Track> param) {
						return new ResultListCell();
					}

	            }
	        );
	}
	
	public void display(){
		
		stage = new Stage();
        stage.setTitle("Search Results");
        stage.setScene(new Scene(this, 450, 450));
        this.setItems(FXCollections.observableArrayList(list));
        
	}
	
	public void updateContents(List<Track> newList){
		// this.setItems(FXCollections.observableArrayList(newList));
		list = new ArrayList(newList);
		/*list = new ArrayList<Track>();
		list = newList;*/
	}
	
	public void show(){
		stage.show();
	}
	
public class ResultListCell extends ListCell<Track> {
		
	    private static final String CACHE_LIST_FOUND_CLASS = "cache-list-found";
	    private static final String CACHE_LIST_NOT_FOUND_CLASS = "cache-list-not-found";
	    private static final String CACHE_LIST_NAME_CLASS = "cache-list-name";
	    private static final String CACHE_LIST_DT_CLASS = "cache-list-dt";
	    private static final String CACHE_LIST_ICON_CLASS = "cache-list-icon";
	    private static final String FONT_AWESOME = "FontAwesome";
	 
	    private HBox hbox = new HBox();
	    private Label name = new Label();
	    private Label artist = new Label();
	    private ImageView albumArt = new ImageView();
	   //  private Label dt = new Label();
	 
	    public ResultListCell() {
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
	        name.getStyleClass().add(CACHE_LIST_NAME_CLASS);
	    }
	
	 
	    private void addControlsToGrid() {
	    	hbox.getChildren().add(albumArt);
	    	VBox textInfo = new VBox();
	    	textInfo.getChildren().addAll(name, artist);
	    	hbox.getChildren().add(textInfo);                    
	    	// this.getChildren().add(hbox);    
	    }
	 
	   @Override
	    public void updateItem(Track track, boolean empty) {
	        super.updateItem(track, empty);
	        if (empty) {
	            clearContent();
	        } else {
	            addContent(track);
	        }
	    }
	 
	    private void clearContent() {
	        setText(null);
	        setGraphic(null);
	    }
	 
	   private void addContent(Track track) {
	        setText(null);
	        name.setText(track.getName());
	        String a = "";
	        for(SimpleArtist s : track.getArtists())
	        	a = a.concat(s.getName()+", "); // TODO this will have a trailing comma
	        
	        artist.setText(a);
	        
	        List<com.wrapper.spotify.models.Image> URLS = track.getAlbum().getImages();
			String imageSource = URLS.get(URLS.size()-1).getUrl();
	        
	        albumArt = ImageViewBuilder.create()
	                .image(new Image(imageSource))
	                .build();
	       
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
