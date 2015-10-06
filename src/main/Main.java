package main;


import gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;
import authenticate.Authenticator;

import com.wrapper.spotify.Api;

import controller.Controller;


public class Main extends Application{

	public static void main(String[] args) {

		launch(args);
	
	}
	
	static void Authenticate(){
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		/* Application details necessary to get an access token */
		final String clientId = "554bc26ca72a4a9fa204b5bc8539ae17";
		final String clientSecret = "0829c1d4af084dfbb6028202dc94f66c";
		final String redirectUri = "http://localhost:8888/callback";

		/* Create a default API instance that will be used to make requests to Spotify */
		final Api api = Api.builder()
				.clientId(clientId)
				.clientSecret(clientSecret)
				.redirectURI(redirectUri)
				.build();
		
		/* Authenticate the API with user data */
		Authenticator auth = new Authenticator();
		
		auth.authenticate(api);
		
		Controller controller = new Controller(api); //TODO pass the gui as an agument to controller
		
		// Controller controller = new Controller();
		
		GUI gui = new GUI(controller);
		
		gui.start(primaryStage);
		
	}


}

