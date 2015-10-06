package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import controller.SearchResultsPaneController;

public class SearchResultsPane extends ListView<Track>{

	Stage stage = new Stage();
	List<Track> list;
	double width;
	double height;
	SearchResultsPaneController controller = new SearchResultsPaneController();
	List<Thread> runningThreads;


	public SearchResultsPane(double x, double y){
		runningThreads = new ArrayList<Thread>();
		this.getStylesheets().add("GUIStyle.css");
		list = new ArrayList<Track>();
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

		});
	}

	public void display(){

		stage = new Stage();
		stage.setTitle("Search Results");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setScene(new Scene(this, width, height));
		this.setItems(FXCollections.observableArrayList(list));

	}

	public void updateContents(final List<Track> newList){
		if(this.getItems().size()==0)
			this.setItems(FXCollections.observableArrayList(newList));
		else{	// TODO make this iterate over the newList instead of items
			this.runningThreads.clear();
			for(Track track : this.getItems()){
				int index = this.getItems().indexOf(track);
				this.getItems().set(index, newList.get(index));
			}
		}
		
		for(Thread t : runningThreads){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}}
	}


	public class ResultListCell extends ListCell<Track> {

		private HBox hbox = new HBox();
		private Hyperlink name = new Hyperlink();
		private HBox artistBox = new HBox();
		private Hyperlink album = new Hyperlink();
		private ImageView albumArt = new ImageView();
		private StackPane albumArtPane = new StackPane();

		public ResultListCell() {
			this.getStylesheets().add("GUIStyle.css");
			this.getStyleClass().clear();
			this.getStyleClass().add("result_cell");
			
			configureAlbumArt();     
			setTextInfo();
			
		}

		private void configureAlbumArt() {
			
			this.setPrefHeight(90);
			this.setPrefWidth(SearchResultsPane.this.width);
			
			albumArtPane.getChildren().add(albumArt);
			albumArtPane.setPrefSize(70, 70);
			albumArtPane.setMaxSize(70, 70);
			albumArt.setFitWidth(70);
			albumArt.setFitHeight(70);
			albumArtPane.setTranslateX(10);	
			albumArtPane.getStyleClass().add("album_art");
		
			
			hbox.setSpacing(15);
			hbox.setAlignment(Pos.CENTER_LEFT);
			hbox.getChildren().add(albumArtPane);

					
		}

		private void setTextInfo(){
			name.getStyleClass().add("result_track_name");
			album.getStyleClass().add("result_album_name");
			
			VBox textInfo = new VBox();
			textInfo.setTranslateY(5);
			textInfo.getChildren().add(name);
			textInfo.getChildren().add(artistBox);
			textInfo.getChildren().add(album);
			textInfo.setTranslateX(5);
			hbox.getChildren().add(textInfo);      
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

		private void addContent(final Track track) {
			Thread t = new Thread(){
				public void run(){
					artistBox.getChildren().clear();
					name.setText(track.getName());
					album.setText(track.getAlbum().getName());
					for(SimpleArtist s : track.getArtists()){
						Hyperlink h = new Hyperlink(s.getName());
						h.getStyleClass().add("result_artist_name");
						artistBox.getChildren().add(h);
						artistBox.getChildren().add(new Label("/"));
					}
					artistBox.getChildren().remove(artistBox.getChildren().size()-1);
					List<com.wrapper.spotify.models.Image> URLS = track.getAlbum().getImages();
					String imageSource = URLS.get(URLS.size()-1).getUrl();
					albumArt.setImage(new Image(imageSource));
				}
			};
			SearchResultsPane.this.runningThreads.add(t);
			t.start();
			setGraphic(hbox);
		}
	}

	public void showResults(String text) {
		stage.hide();
		updateContents(controller.getSearchResults(text));
		stage.show();
		this.requestFocus();
	}


	public void setController(
			SearchResultsPaneController controller) {
		this.controller = controller;
		
	}


	public void setListener(final PlaylistEditor playlistEditor) {
		final EventHandler<KeyEvent> keyEventHandler =
                new EventHandler<KeyEvent>() {
                    public void handle(final KeyEvent keyEvent) {
                        if (keyEvent.getCode()==KeyCode.ENTER) {
                           playlistEditor.trackSelected(SearchResultsPane.this.getSelectionModel().getSelectedItem());
                           SearchResultsPane.this.stage.hide();
                        }
                        if(keyEvent.getCode() == KeyCode.ESCAPE){
                        	 SearchResultsPane.this.stage.hide();
                        }
                    }
                };
		this.setOnKeyPressed(keyEventHandler);
	}

	public void setParentStage(Stage stage2) {
		
		this.stage.setX(stage2.getX() + stage2.getWidth()); // TODO won't work when the stage's size is readjusted
		this.stage.setY(stage2.getY());
		
		this.stage.initModality(Modality.WINDOW_MODAL);
		this.stage.initOwner(stage2.getScene().getWindow());
		
	}

}
