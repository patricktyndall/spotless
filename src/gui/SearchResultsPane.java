package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.GroupBuilder;
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

public class SearchResultsPane extends ListView<Track>{

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
		stage.setScene(new Scene(this, width, height));
		this.setItems(FXCollections.observableArrayList(list));

	}

	public void updateContents(final List<Track> newList){
		// 
		if(this.getItems().size()==0)
			this.setItems(FXCollections.observableArrayList(newList));
		else{	
			for(Track track : this.getItems()){
				int index = this.getItems().indexOf(track);
				this.getItems().set(index, newList.get(index));
			}
		}
	}

	public void hide(){
		stage.hide();
	}

	public void show(){
		stage.show();
	}

	public class ResultListCell extends ListCell<Track> {

		private HBox hbox = new HBox();
		private Label name = new Label();
		private Label artist = new Label();
		private ImageView albumArt = new ImageView();

		public ResultListCell() {
			configureHbox();        
			addControlsToGrid();            
		}

		private void configureHbox() {
			// hbox.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5)");
			this.setPrefHeight(100);
			this.setPrefWidth(SearchResultsPane.this.width);
			albumArt.setFitWidth(70);
			albumArt.setFitHeight(70);
			hbox.setSpacing(width/5);

			hbox.setPadding(new Insets(0, 10, 0, 10));
		}

		private void addControlsToGrid() {
			hbox.getChildren().add(albumArt);
			VBox textInfo = new VBox();
			textInfo.getChildren().addAll(name, artist);
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

					setText(null);
					name.setText(track.getName());
					String a = "";
					for(SimpleArtist s : track.getArtists())
						a = a.concat(s.getName()+", "); // TODO this will have a trailing comma

					artist.setText(a);

					List<com.wrapper.spotify.models.Image> URLS = track.getAlbum().getImages();
					String imageSource = URLS.get(URLS.size()-1).getUrl();

					albumArt.setImage(new Image(imageSource));
					
				
				}
			};
			
			t.start();
			setGraphic(hbox);
			System.out.println("Done");  
			
			
		}

	}

}
