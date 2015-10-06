package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import controller.AbstractPlaylistController;
import controller.TrackPaneController;

public class TrackPane extends Region{

	static final Double ROW_HEIGHT_PCT = 0.05;

	private TrackPaneController controller;


	double width;
	double height;
	TableView<Track> table;
	StackPane noPlaylistSelectedDialog;


	public TrackPane(double x, double y) {

		this.width = x;
		this.height = y;

		makeTable();
		makeNoPlaylistSelectedDialog();

		this.getChildren().add(noPlaylistSelectedDialog);
	}

	private void makeNoPlaylistSelectedDialog() {
		noPlaylistSelectedDialog = new StackPane();
		noPlaylistSelectedDialog.setPrefSize(width, height);
		Label label = new Label("No playlist selected");
		noPlaylistSelectedDialog.getChildren().add(label);
		label.getStyleClass().add("no_playlist_dialog");
	}

	private void makeTable(){
		table = new TableView<Track>();
		table.setPrefWidth(width);
		table.setPrefHeight(height);
		table.getStylesheets().add("GUIStyle.css");
		table.setItems(FXCollections.observableArrayList(new ArrayList<Track>()));
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		makeTrackColumn();
		makeArtistColumn();
		makeAlbumColumn();
	}

	public void update(){
		if(controller.isPlaylistSelected()){
			updateItems(controller.getTrackInfo());
			if(!this.getChildren().contains(table)){
				this.getChildren().clear();
				this.getChildren().add(table);
			}
		}
		else{
			this.getChildren().clear();
			this.getChildren().add(noPlaylistSelectedDialog);
		}

	}

	public void updateItems(List<Track> newItems){
		table.setItems(FXCollections.observableArrayList(newItems));
	}

	public void addTrackToTable(Track t){ 
		// TODO -- do a check to make sure the data gets synchronized to the playlist as well
		this.table.setVisible(false);
		this.table.getItems().add(0,t);
		this.table.setVisible(true);
	}

	private void makeTrackColumn(){
		TableColumn<Track, Hyperlink> trackNameCol = new TableColumn<Track, Hyperlink>("Song");
		trackNameCol.setMinWidth(100);

		trackNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Track, Hyperlink>, ObservableValue<Hyperlink>>(){
			public ObservableValue<Hyperlink> call(TableColumn.CellDataFeatures<Track, Hyperlink> p) {
				if (p.getValue() != null) {
					//TODO add handler for opening the spotify URL
					return new ReadOnlyObjectWrapper<Hyperlink>(new Hyperlink(p.getValue().getName()));
				} else {
					return null;
				}
			}
		});

		trackNameCol.setCellFactory(new Callback<TableColumn<Track, Hyperlink>, TableCell<Track, Hyperlink>>(){
			public TableCell<Track, Hyperlink> call(
					TableColumn<Track, Hyperlink> param) {

				return new TrackCell();
			}	
		}     
				);

		trackNameCol.setPrefWidth(width/3.0);
		trackNameCol.setMaxWidth(width/2.0);
		trackNameCol.setMinWidth(width/9.0);
		table.getColumns().add(trackNameCol);
	}

	private void makeArtistColumn(){
		TableColumn<Track, List<Hyperlink>> artistNameCol = new TableColumn<Track, List<Hyperlink>>("Artist");
		artistNameCol.setMinWidth(100);

		artistNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Track, List<Hyperlink>>, ObservableValue<List<Hyperlink>>>(){
			public ObservableValue <List<Hyperlink>> call(TableColumn.CellDataFeatures<Track, List<Hyperlink>> p) {
				if (p.getValue() != null) {

					List<Hyperlink> artists = new ArrayList<Hyperlink>();
					for(SimpleArtist a : p.getValue().getArtists()){
						artists.add(new Hyperlink(a.getName()));
						//TODO add handler for opening the spotify URLs
					}
					return new ReadOnlyObjectWrapper<List<Hyperlink>>(artists);

				} else {
					return null;
				}
			}
		});

		artistNameCol.setCellFactory(new Callback<TableColumn<Track, List<Hyperlink>>, TableCell<Track, List<Hyperlink>>>(){
			public TableCell<Track, List<Hyperlink>> call(
					TableColumn<Track, List<Hyperlink>> param) {

				return new ArtistCell();
			}	
		}     
				);

		artistNameCol.setPrefWidth(width/3.0);
		artistNameCol.setMaxWidth(width/2.0);
		artistNameCol.setMinWidth(width/9.0);
		table.getColumns().add(artistNameCol);
	}

	private void makeAlbumColumn(){
		TableColumn<Track, Hyperlink> albumNameCol = new TableColumn<Track, Hyperlink>("Album");
		albumNameCol.setMinWidth(100);

		albumNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Track, Hyperlink>, ObservableValue<Hyperlink>>(){
			public ObservableValue<Hyperlink> call(TableColumn.CellDataFeatures<Track, Hyperlink> p) {
				if (p.getValue() != null) {
					//TODO add handler for opening the spotify URL
					return new ReadOnlyObjectWrapper<Hyperlink>(new Hyperlink(p.getValue().getAlbum().getName()));
				} else {
					return null;
				}
			}
		});

		albumNameCol.setCellFactory(new Callback<TableColumn<Track, Hyperlink>, TableCell<Track, Hyperlink>>(){
			public TableCell<Track, Hyperlink> call(
					TableColumn<Track, Hyperlink> param) {

				return new AlbumCell();
			}	
		}     
				);
		albumNameCol.setPrefWidth(width/3.0);
		albumNameCol.setMaxWidth(width/2.0);
		albumNameCol.setMinWidth(width/9.0);

		table.getColumns().add(albumNameCol);
	}

	public void setController(AbstractPlaylistController c){
		this.controller = (TrackPaneController) c;
	}

	private class TrackCell extends TableCell<Track, Hyperlink>{

		public void updateItem(Hyperlink item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				setGraphic(item);
			}
		}
	}

	private class ArtistCell extends TableCell<Track, List<Hyperlink>>{
		public void updateItem(List<Hyperlink> item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				HBox links = new HBox();
				for(Hyperlink l : item){
					links.getChildren().add(l);
					links.getChildren().add(new Label(", "));
				}
				links.getChildren().remove(links.getChildren().size()-1);
				setGraphic(links);
			}
		}

	}

	private class AlbumCell extends TableCell<Track, Hyperlink>{
		public void updateItem(Hyperlink item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				setGraphic(item);
			}
		}
	}


}


