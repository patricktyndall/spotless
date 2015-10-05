package gui;

import java.util.List;

import com.wrapper.spotify.models.Track;

import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import controller.AbstractPlaylistController;
import controller.TrackPaneController;
import data.Pair;

public class TrackPane extends TableView{

	static final Double ROW_HEIGHT_PCT = 0.05;


	private TrackPaneController controller;
	List<Trac>

	double width;
	double height;
	ListView<Pair> list;


	public TrackPane(double x, double y) {
		this.width = x;
		this.height = y;

		this.list = new ListView<Pair>();
		this.getStylesheets().add("GUIStyle.css");

		makeList();

		makeTable();
	}

	private void makeTable(){

		Callback<TableColumn, TableCell> percantageCellFactory =
				new Callback<TableColumn, TableCell>() {
			public TableCell call(TableColumn p) {
				return new PercantageFormatCell();
			}
		};

		levelOfGrowthColumn.setCellFactory(percantageCellFactory);



		this.setEditable(true);

		TableColumn songNameCol = new TableColumn("Song");
		songNameCol.setMinWidth(100);
		songNameCol.setCellValueFactory(
				new PropertyValueFactory<TrackCell, String>("firstName"));

		TableColumn lastNameCol = new TableColumn("Artist");
		lastNameCol.setMinWidth(100);
		lastNameCol.setCellValueFactory(
				new PropertyValueFactory<TrackCell, String>("lastName"));

		TableColumn emailCol = new TableColumn("Album");
		emailCol.setMinWidth(200);
		emailCol.setCellValueFactory(
				new PropertyValueFactory<TrackCell, String>("email"));

		this.setItems(data);
		table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
	}



	public void refreshList(){
		list.getItems().clear();
		List<Pair> items = controller.getTrackInfo();
		list.setItems(FXCollections.observableArrayList(items));
	}

	private void makeList(){

		List<Pair> items = controller.getTrackInfo();
		list.setItems(FXCollections.observableArrayList(items));
		list.setPrefWidth(width);
		list.setPrefHeight(height*(1-ROW_HEIGHT_PCT));

		for(Pair p : items)
			System.out.println(p.song);

		list.setCellFactory(new Callback<ListView<Pair>, 
				ListCell<Pair>>() { 
			public ListCell<Pair> call(ListView<Pair> param) {
				// return new TrackListCell();
			}

		}
				);

		this.getChildren().add(list);
	}

	public void setController(AbstractPlaylistController c){
		this.controller = (TrackPaneController) c;
	}

	private class SongCell extends TableCell<Object, Track>{

	}

	private class ArtistCell extends TableCell<Object, Track>{

	}

	private class AlbumCell extends TableCell<Object, Track>{

	}


	public class TrackCell extends ListCell<Pair> {



		private HBox hbox = new HBox();
		private Label track = new Label();
		private Label artist = new Label();

		public TrackCell() {
			configureHbox();        
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
