package spotless;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import com.wrapper.spotify.Api;

/**
 * This example shows how to get information about the user that is 'connected' to the
 * access token. The methods used (api.authorizationCodeGrant and api.getMe) are synchronous, but are
 * available asynchronously as well. The scopes necessary for this example are 'user-read-private'
 * and 'user-read-email'.
 *
 * The authorization flow used is documented in detail at
 * https://developer.spotify.com/spotify-web-api/authorization-guide/#authorization_code_flow
 *
 * Details about requesting the current user's information is documented at
 * https://developer.spotify.com/spotify-web-api/get-users-profile/ in the
 * "Authorization Code" section.
 */
public class Main {

	public static void main(String[] args) {

		/* Application details necessary to get an access token */
		final String clientId = "554bc26ca72a4a9fa204b5bc8539ae17";
		final String clientSecret = "0829c1d4af084dfbb6028202dc94f66c";
		final String code = "<insert code>";
		final String redirectUri = "http://localhost:8888/callback";

		/* Create a default API instance that will be used to make requests to Spotify */
		final Api api = Api.builder()
				.clientId(clientId)
				.clientSecret(clientSecret)
				.redirectURI(redirectUri)
				.build();

		/* Set the necessary scopes that the application will need from the user */
		final List<String> scopes = Arrays.asList("user-read-private", "user-read-email");

		/* Set a state. This is used to prevent cross site request forgeries. */
		final String state = "someExpectedStateString";

		String authorizeURL = api.createAuthorizeURL(scopes, state);

		//Dispatch();
		
		
		SimpleServer server = new SimpleServer(8888);
		new Thread(server).start();

		if(Desktop.isDesktopSupported())
		{
			try {
				Desktop.getDesktop().browse(new URI(authorizeURL));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("Stopping Server");
		String[] data = server.getData();
		for(String d : data){
			System.out.println(d);
		}
		server.stop();
	

	}

	static void Dispatch(){
		SimpleServer server = new SimpleServer(8888);
		new Thread(server).start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();
	}
}

