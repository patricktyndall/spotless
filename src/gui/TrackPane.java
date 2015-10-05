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
import javafx.util.Callback;

import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import controller.AbstractPlaylistController;
import controller.TrackPaneController;

public class TrackPane extends TableView<Track>{

	static final Double ROW_HEIGHT_PCT = 0.05;


	private TrackPaneController controller;


	double width;
	double height;


	public TrackPane(double x, double y) {
		this.width = x;
		this.height = y;

		this.getStylesheets().add("GUIStyle.css");
		this.setItems(FXCollections.observableArrayList(new ArrayList<Track>()));

		makeTable();
	}

	private void makeTable(){
		makeTrackColumn();
		makeArtistColumn();
		makeAlbumColumn();
	}
	
	public void update(){
		updateItems(controller.getTrackInfo());
	}
	
	public void updateItems(List<Track> newItems){
		this.setItems(FXCollections.observableArrayList(newItems));
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


		this.getColumns().add(trackNameCol);
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


			this.getColumns().add(artistNameCol);
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


		this.getColumns().add(albumNameCol);
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


