package gui;

import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUI extends Application {

	/*
	 * Instanciate all of the sub-components, and then instanciate the different 
	 * screens with the sub-components as params
	 * 
	 * Create childViewControllers?
	 */
	
	Controller controller;

	/* Display vars--proportion of said dimension to occupy */
	
	static final double X_RATIO = 0.7;
	static final double Y_RATIO = 0.4;
	
	double screenHeight;
	double screenWidth;
	Stage stage;

	public GUI(Controller controller){

		this.controller = controller;
		

	}
	
	private void setupBounds(){
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();

		screenHeight = bounds.getHeight();
		screenWidth = bounds.getWidth();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		setupBounds();
		stage.setTitle("Spotless");
		StackPane root = new StackPane();
		
		
		PlaylistEditor editor = makePlaylistEditor();
		root.getChildren().add(editor);
		
		if(Platform.isSupported(javafx.application.ConditionalFeature.UNIFIED_WINDOW))
			stage.initStyle(StageStyle.UNIFIED);
		else
			stage.initStyle(StageStyle.DECORATED);
		
		stage.setScene(new Scene(root, screenWidth*X_RATIO, screenHeight*Y_RATIO));
		
		
		/* lock stage size */
		stage.setMinWidth(screenWidth*X_RATIO);
		stage.setMaxWidth(screenWidth*X_RATIO);
		stage.setMinHeight(screenHeight*Y_RATIO);
		stage.setMaxHeight(screenHeight*Y_RATIO);
		
		
		
		stage.show();
		editor.setStage(stage);
		
	}
	
	private PlaylistEditor makePlaylistEditor(){
		
		return new PlaylistEditor(screenWidth*X_RATIO, screenHeight*Y_RATIO);
	}



}
