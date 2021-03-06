package authenticate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

public class WorkerRunnable {// TODO implement runnable?

	protected Socket clientSocket = null;
	protected String serverText   = null;
	private String[] myData = new String[2];

	public WorkerRunnable(Socket clientSocket, String serverText) {
		this.clientSocket = clientSocket;
		this.serverText   = serverText;
	}

	public void run() {
		try {
			InputStream input  = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			output.write(("HTTP/1.1 200 OK\n\nAuthentication Complete.").getBytes());
			parseInputStream(input);
			output.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getData(){
		return myData;
	}

	private void parseInputStream(InputStream is) {

		BufferedReader br = null;

		String line = "";
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				if(line.startsWith("GET")){ // TODO make this a more reliable method of parsing
					break;
				}
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
		// TODO make sure these operations are completely reliable
		String[] data = new String[2];
		data = line.substring(line.lastIndexOf("code=") + 5).split("&state=");
		data[1] = data[1].split(" ")[0];
		
		myData = data;
		

	}
}