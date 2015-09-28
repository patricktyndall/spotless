package spotless;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;


public class Main {

	public static void main(String[] args) {

		/* Application details necessary to get an access token */
		final String clientId = "554bc26ca72a4a9fa204b5bc8539ae17";
		final String clientSecret = "0829c1d4af084dfbb6028202dc94f66c";
		String code = "<insert code>";
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
		
		
	

		
		while(!server.isStopped){
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		System.out.println("Server Stopped");

		//System.out.println("Stopping Server");
		String[] data = server.getData();
		for(String d : data){
			System.out.println(d);
		}


		/* Application details necessary to get an access token */
		code = data[0];

		/* Make a token request. Asynchronous requests are made with the .getAsync method and synchronous requests
		 * are made with the .get method. This holds for all type of requests. */
		final SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = api.authorizationCodeGrant(code).build().getAsync();

		/* Add callbacks to handle success and failure */
		Futures.addCallback(authorizationCodeCredentialsFuture, new FutureCallback<AuthorizationCodeCredentials>() {
			public void onSuccess(AuthorizationCodeCredentials authorizationCodeCredentials) {
				/* The tokens were retrieved successfully! */
				System.out.println("Successfully retrieved an access token! " + authorizationCodeCredentials.getAccessToken());
				System.out.println("The access token expires in " + authorizationCodeCredentials.getExpiresIn() + " seconds");
				System.out.println("Luckily, I can refresh it using this refresh token! " +     authorizationCodeCredentials.getRefreshToken());

				/* Set the access token and refresh token so that they are used whenever needed */
				api.setAccessToken(authorizationCodeCredentials.getAccessToken());
				api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
			}

			public void onFailure(Throwable throwable) {
				System.out.println("failed");
				/* Let's say that the client id is invalid, or the code has been used more than once,
				 * the request will fail. Why it fails is written in the throwable's message. */

			}
		});




	}

	static void Dispatch(){
		SimpleServer server = new SimpleServer(8888);
		server.run();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();
	}
}

