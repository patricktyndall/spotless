package gui;

import java.util.List;
import java.util.Optional;

import controller.AbstractPlaylistController;
import controller.PlaylistPaneController;
import controller.SearchBoxController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

public class PlaylistPane extends VBox{

	/*
	 * Needs:
	 * current playlist data
	 * a button to create a`	 new playlist and communicate this
	 * a means with which to communicate with the other structures about which playlist is being edited
	 */

	private PlaylistPaneController controller;
	
	ListView<String> list;
	Button button;
	double width;
	double height;

	public PlaylistPane(double x, double y) {
		this.getStylesheets().add("GUIStyle.css");
		this.getStyleClass().add("playlist_pane");
		this.width = x;
		this.height = y;

	}

	public void display(){
		makeAddNewButton();
		makeList();
		this.setAlignment(Pos.TOP_CENTER);
	}
	
	public void setListener(final PlaylistEditor playlistEditor){ // TODO why must this be final
		list.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> ov, 
							String old_val, String new_val) {
						controller.setCurrentPlaylist(new_val);
						playlistEditor.refresh();
					}
				});
	}

	private void makeAddNewButton(){

		button = new Button("Add new");
		button.setPrefWidth(width);
		button.getStyleClass().add("add_new_button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				newPlaylistWizard();
			}
		});

		this.getChildren().add(button);
	}

	private void newPlaylistWizard(){
		
		// TODO this isn't CSS-stylable, but it'll work for testing
		TextInputDialog dialog = new TextInputDialog("New Playlist");
		dialog.setTitle("Playlist name request");
		dialog.setHeaderText("Please enter a name for your new playlist");
		Optional<String> result = dialog.showAndWait();
		/*if (result.isPresent()){
			System.out.println("Your choice: " + result.get());
		} */
		
		controller.makePlaylist(result.get());
		
		makeList();
		// TODO
		// launch a popup that takes a string
		// create a playlist with that name
		// add it to the ListView and select it
		// ideally the listView handles it from here
	}

	private void makeList(){
		list = new ListView<String>();
		list.getStyleClass().add("playlist_list");
		List<String> items = controller.getPlaylistNames();
		list.setItems(FXCollections.observableArrayList(items));
		list.setPrefWidth(width);
		list.setPrefHeight(height);
		this.getChildren().add(list);
	}

	public void setController(AbstractPlaylistController c){
		this.controller = (PlaylistPaneController) c;
	}



}
