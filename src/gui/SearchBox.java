package gui;

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
	SearchResultsPane pane;
	

	public SearchBox(double x, double y) {
		this.width = x;
		this.height = y;
		this.getStylesheets().add("GUIStyle.css");
		makeTextField();
		makeButton();
		pane = new SearchResultsPane(400, 300); // TODO fix these sizes
		pane.display();

		

	}
	
	private void makeButton(){
		searchButton = new Button("Search");
		searchButton.getStyleClass().add("search_button");
		searchButton.setPrefWidth(width*BUTTON_PCT);
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                launchResultsPane();
            }
        });
		this.getChildren().add(searchButton);
	}
	
	private void makeTextField(){
		textField = new TextField();
		textField.getStyleClass().add("search_field");
		textField.setPrefWidth(width*(1-BUTTON_PCT));
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
	}

	
	private void launchResultsPane(){
		pane.hide();
		pane.updateContents(controller.getSearchResults(textField.getText()));
		
		pane.show();
		System.out.println("Results rendering complete");

	}
	
	

	public void setController(AbstractPlaylistController c){
		this.controller = (SearchBoxController) c;
	}

	

}
