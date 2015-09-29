package main;


import authenticate.Authenticator;

import com.wrapper.spotify.Api;

import controller.Controller;
import controller.Library;


public class Main {

	public static void main(String[] args) {

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
		
		Controller controller = new Controller(new Library(api));
		
		controller.launch();
	
	}
	
	static void Authenticate(){
		
	}

}

