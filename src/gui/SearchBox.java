package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import controller.AbstractPlaylistController;
import controller.SearchBoxController;

public class SearchBox extends HBox{

	static final Double BUTTON_PCT = 0.15;

	private SearchBoxController controller;

	Double width;
	Double height;
	Button searchButton;
	TextField textField;
	// SearchResultsPane pane;


	public SearchBox(double x, double y) {
		this.width = x;
		this.height = y;
		this.getStylesheets().add("GUIStyle.css");
		makeTextField();
		makeButton();




	}

	private void makeButton(){
		searchButton = new Button("Search");
		searchButton.getStyleClass().add("search_button");
		searchButton.setPrefWidth(width*BUTTON_PCT);

		this.getChildren().add(searchButton);
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

		this.getChildren().add(textField);
	}


	public void setController(AbstractPlaylistController c){
		this.controller = (SearchBoxController) c;
	}

	public void setListener(final PlaylistEditor playlistEditor) {
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				playlistEditor.search(textField.getText());
			}
		});

	}




}
