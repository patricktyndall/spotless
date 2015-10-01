package gui;

import java.util.ArrayList;
import java.util.List;

import com.wrapper.spotify.models.Track;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import controller.AbstractPlaylistController;
import controller.SearchBoxController;

public class SearchBox extends HBox{

	private SearchBoxController controller;

	Double width;
	Double height;
	Button searchButton;
	TextField textField;
	SearchResultsPane pane;

	public SearchBox(double x, double y) {
		textField = new TextField();
		pane = new SearchResultsPane(x, y); // TODO fix these sizes
		pane.display();
		this.width = x;
		this.height = y;
		

	}


	public void display(){
		searchButton = new Button("Search");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                launchResultsPane();
            }
        });
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
		
		this.getChildren().add(textField);
		this.getChildren().add(searchButton);
	}
	
	private void launchResultsPane(){

		pane.updateContents(controller.getSearchResults(textField.getText()));
		pane.show();

	}
	
	

	public void setController(AbstractPlaylistController c){
		this.controller = (SearchBoxController) c;
	}

	

}
