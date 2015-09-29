package authenticate;

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

public class Authenticator {
	
	final int portNumber = 8888;

	public void authenticate(final Api api){
		
		/* Set the necessary scopes that the application will need from the user */
		final List<String> scopes = Arrays.asList("playlist-modify-public", "user-read-private", "user-read-email");

		/* Set a state. This is used to prevent cross site request forgeries. */
		final String state = "someExpectedStateString";

		String authorizeURL = api.createAuthorizeURL(scopes, state);

		ServerThread server = new ServerThread(8888);
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

		String[] data = server.getData();
	
		/* Application details necessary to get an access token */
		String code = data[0];
		// TODO make sure the state parameter matches--for security reasons

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
				System.out.println("Authentication failed");
				/* Let's say that the client id is invalid, or the code has been used more than once,
				 * the request will fail. Why it fails is written in the throwable's message. */

			}
		});
		
	}

}
