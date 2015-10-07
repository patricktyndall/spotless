package gui;

import controller.AbstractPlaylistController;
import controller.SearchBoxController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class SearchBox extends AbstractEditorPane{

	static final Double BUTTON_PCT = 0.15;

	Double width;
	Double height;
	Button searchButton;
	TextField textField;
	HBox hbox;
	private SearchBoxController controller;

	public SearchBox(double x, double y) {
		hbox = new HBox();
		this.width = x;
		this.height = y;
		hbox.getStylesheets().add("GUIStyle.css");
		hbox.getStyleClass().add("search_section");
		makeTextField();
		makeButton();
		this.getChildren().add(hbox);
	}

	private void makeButton(){
		searchButton = new Button("Search");
		searchButton.getStyleClass().add("search_button");
		searchButton.setPrefWidth(width*BUTTON_PCT);
		hbox.getChildren().add(searchButton);
	}

	private void makeTextField(){
		textField = new TextField();
		textField.getStyleClass().add("search_field");
		textField.setPrefWidth(width*(1-BUTTON_PCT));
		textField.setPromptText("Search for a track...");
		textField.setOnKeyPressed(new EventHandler<KeyEvent>()
				{
			public void handle(KeyEvent ke)
			{
				if (ke.getCode().equals(KeyCode.ENTER))
				{
					searchButton.fire();
				}
			}
				});
		
		textField.focusedProperty().addListener(new ChangeListener<Boolean>()
				{
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
			{
				if (newPropertyValue)
				{
					textField.selectAll();
				}
			}
				});

		hbox.getChildren().add(textField);
	}
	
	public void setController(AbstractPlaylistController c){
		this.controller = (SearchBoxController) c;
	}

	public void setListener(final PlaylistEditor playlistEditor) {
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(controller.isPlaylistSelected())
					playlistEditor.search(textField.getText());
				else{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Please select a playlist first");
					alert.setTitle("Playlist name request");
					alert.showAndWait();
				}
					
			}
		});
	}
}
