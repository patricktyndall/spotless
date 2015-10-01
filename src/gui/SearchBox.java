package gui;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	Button addButton;
	ComboBox<String> searchBox;

	public SearchBox(double x, double y) {
		searchBox = new ComboBox<String>();
		searchBox.setItems(FXCollections.observableArrayList(new ArrayList<String>()));
		searchBox.addEventHandler(KeyEvent.ANY, new AutoCompleteComboBoxListener<KeyEvent>(searchBox));
		this.width = x;
		this.height = y;

	}

	public void display(){
		addButton = new Button("Add");
		this.getChildren().add(searchBox);
		this.getChildren().add(addButton);
	}

	public void setController(AbstractPlaylistController c){
		this.controller = (SearchBoxController) c;
	}

	public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent> {

		private ComboBox comboBox;
		private StringBuilder sb;
		private ObservableList<T> data;
		private boolean moveCaretToPos = false;
		private int caretPos;

		public AutoCompleteComboBoxListener(final ComboBox comboBox) {
			this.comboBox = comboBox;
			sb = new StringBuilder();
			data = comboBox.getItems();

			this.comboBox.setEditable(true);
			this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

				public void handle(KeyEvent t) {
					comboBox.hide();
				}
			});
			this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
		}

		public void handle(KeyEvent event) {

			if(event.getCode() == KeyCode.UP) {
				caretPos = -1;
				moveCaret(comboBox.getEditor().getText().length());
				return;
			} else if(event.getCode() == KeyCode.DOWN) {
				if(!comboBox.isShowing()) {
					comboBox.show();
				}
				caretPos = -1;
				moveCaret(comboBox.getEditor().getText().length());
				return;
			} else if(event.getCode() == KeyCode.BACK_SPACE) {
				moveCaretToPos = true;
				caretPos = comboBox.getEditor().getCaretPosition();
			} else if(event.getCode() == KeyCode.DELETE) {
				moveCaretToPos = true;
				caretPos = comboBox.getEditor().getCaretPosition();
			}

			if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
					|| event.isControlDown() || event.getCode() == KeyCode.HOME
					|| event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
				return;
			}

			ObservableList<String> list = FXCollections.observableArrayList();
			
			list.addAll(controller.getSearchResults(AutoCompleteComboBoxListener.this.comboBox
						.getEditor().getText().toLowerCase()));
			
			
			String t = comboBox.getEditor().getText();

			comboBox.setItems(list);
			comboBox.getEditor().setText(t);
			if(!moveCaretToPos) {
				caretPos = -1;
			}
			moveCaret(t.length());
			if(!list.isEmpty()) {
				comboBox.show();
			}
		}

		private void moveCaret(int textLength) {
			if(caretPos == -1) {
				comboBox.getEditor().positionCaret(textLength);
			} else {
				comboBox.getEditor().positionCaret(caretPos);
			}
			moveCaretToPos = false;
		}

	}

}
