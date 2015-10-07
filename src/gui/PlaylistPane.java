package gui;

import java.util.List;
import java.util.Optional;

import controller.AbstractPlaylistController;
import controller.PlaylistPaneController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

public class PlaylistPane extends AbstractEditorPane{

	private PlaylistPaneController controller;
	
	VBox vbox = new VBox();
	ListView<String> list;
	Button button;
	double width;
	double height;
	static final double BUTTON_HEIGHT_PCT = 0.1;

	public PlaylistPane(double x, double y) {
		vbox.getStylesheets().add("GUIStyle.css");
		vbox.getStyleClass().add("playlist_pane");
		this.width = x;
		this.height = y;
		vbox.setPrefHeight(y);
		vbox.setPrefWidth(x);
		vbox.setSpacing(0);
		this.getChildren().add(vbox);
		
		vbox.setAlignment(Pos.TOP_CENTER);
	}
	
	public void initializeData(){
		makeAddNewButton();
		makeList();
	}

	private void makeAddNewButton(){

		button = new Button("Add new");
		button.setPrefWidth(width);
		button.setPrefHeight(height*BUTTON_HEIGHT_PCT);
		button.getStyleClass().add("add_new_button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				newPlaylistWizard();
			}
		});
		vbox.getChildren().add(button);
	}

	private void newPlaylistWizard(){
		// TODO this isn't CSS-stylable, but it'll work for testing
		TextInputDialog dialog = new TextInputDialog("New Playlist");
		dialog.setTitle("Playlist name request");
		dialog.setHeaderText("Please enter a name for your new playlist");
		Optional<String> result = dialog.showAndWait();
		controller.makePlaylist(result.get());
		// TODO add catch if this isn't null
		// TODO get a String from the Optional<String> and add it to the items
		list.getItems().add(0, result);
		list.getSelectionModel().select(0);
	}

	private void makeList(){
		list = new ListView<String>();
		list.getStyleClass().add("playlist_list");
		List<String> items = controller.getPlaylistNames();
		items.add(""); // TODO dummy item to allow list to be scrolled
		list.setItems(FXCollections.observableArrayList(items));
		list.setMaxWidth(width);
		list.setMinWidth(width);
		list.setPrefHeight(height*(1-(BUTTON_HEIGHT_PCT))); 
		vbox.getChildren().add(list);
	}

	public void setController(AbstractPlaylistController c){
		this.controller = (PlaylistPaneController) c;
	}

	public void setListener(final PlaylistEditor playlistEditor){ 
		list.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> ov, 
							String old_val, String new_val) {
						if(new_val != ""){
							controller.setCurrentPlaylist(new_val);
							playlistEditor.refresh();
						}
					}
				});
	}
}