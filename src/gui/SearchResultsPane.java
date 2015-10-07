package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.util.Callback;

import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import controller.AbstractPlaylistController;
import controller.SearchResultsPaneController;

public class SearchResultsPane extends AbstractEditorPane{

	final String dummyName = "DUMMY";

	Stage stage = new Stage();
	ListView<Track> list;
	Track dummyTrack;
	double width;
	double height;
	SearchResultsPaneController controller = new SearchResultsPaneController();
	List<Thread> runningThreads;


	public SearchResultsPane(double x, double y){
		runningThreads = new ArrayList<Thread>();
		list = new ListView<Track>();
		list.setPrefHeight(y);

		dummyTrack = new Track();
		dummyTrack.setName(dummyName);
		list.getStylesheets().add("GUIStyle.css");
		width = x;
		height = y;
		setupFactory();

		this.getChildren().add(list);
		stage = new Stage();
		stage.setTitle("Search Results");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setScene(new Scene(this, width, height));
	}

	private void setupFactory(){
		list.setCellFactory(new Callback<ListView<Track>, 
				ListCell<Track>>() { 
			public ListCell<Track> call(ListView<Track> param) {
				return new ResultListCell();
			}

		});
	}


	public void updateContents(final List<Track> newList){
		this.runningThreads.clear();
		for(Track track : newList){
			int index = newList.indexOf(track);
			if(index < list.getItems().size())
				list.getItems().set(index, newList.get(index));
			else{
				list.getItems().add(new Track());
				list.getItems().set(index, newList.get(index));
			}
		}
		if(list.getItems().size() > newList.size()){
			list.getItems().subList(newList.size(), list.getItems().size()).clear();
		}
		list.getItems().add(dummyTrack);
		for(Thread t : runningThreads){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public class ResultListCell extends ListCell<Track> {

		private HBox hbox = new HBox();
		private Hyperlink name = new Hyperlink();
		private HBox artistBox = new HBox();
		private Hyperlink album = new Hyperlink();
		private ImageView albumArt = new ImageView();
		private StackPane albumArtPane = new StackPane();
		private StackPane addMorePane = new StackPane();

		public ResultListCell() {
			this.getStylesheets().add("GUIStyle.css");
			this.getStyleClass().add("result_cell");

			configureAlbumArt();     
			setTextInfo();
		}

		private void configureAlbumArt() {
			this.setPrefHeight(90);

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
			} else if(track.getName() != SearchResultsPane.this.dummyName){
				addContent(track);
			}
			else{
				addMoreResultsButton();
			}
		}

		private void addMoreResultsButton(){
			Thread t = new Thread(){
				public void run(){
					ResultListCell.this.setPrefHeight(45);
					ResultListCell.this.hbox.getChildren().clear();
					Label label = new Label("Load 5 More");
					addMorePane.getChildren().add(label);
					addMorePane.setPrefSize(SearchResultsPane.this.width, 45);
					ResultListCell.this.hbox.getChildren().add(addMorePane);
				}
			};
			t.start();
			SearchResultsPane.this.runningThreads.add(t);
			setGraphic(hbox);
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
						artistBox.getChildren().add(h);
						Label slash = new Label("/");
						slash.setTranslateY(2);
						artistBox.getChildren().add(slash);
					}
					artistBox.getChildren().remove(artistBox.getChildren().size()-1);
					for (Node n : artistBox.getChildren())
						n.getStyleClass().add("result_artist_name");

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
		this.list.requestFocus();
	}

	public void setParentStage(Stage stage2) {
		this.stage.setX(stage2.getX() + stage2.getWidth() + 100); // magic number 
		this.stage.setY(stage2.getY());
		this.stage.initModality(Modality.WINDOW_MODAL);
		this.stage.initOwner(stage2.getScene().getWindow());
	}

	public void setController(AbstractPlaylistController controller) {
		this.controller = (SearchResultsPaneController) controller;
	}

	public void setListener(final PlaylistEditor playlistEditor) {
		final EventHandler<KeyEvent> keyEventHandler =
				new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode()==KeyCode.ENTER) {
					controller.trackSelected(SearchResultsPane.this.list.getSelectionModel().getSelectedItem());
					playlistEditor.trackSelected(SearchResultsPane.this.list.getSelectionModel().getSelectedItem());
					SearchResultsPane.this.stage.hide();
				}
				if(keyEvent.getCode() == KeyCode.ESCAPE){
					SearchResultsPane.this.stage.hide();
				}
			}
		};
		this.list.setOnKeyPressed(keyEventHandler);
	}
}