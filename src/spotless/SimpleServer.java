package spotless;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class SimpleServer implements Runnable{

	protected int          serverPort   = 8080;
	protected ServerSocket serverSocket = null;
	protected boolean      isStopped    = false;
	protected Thread       runningThread= null;
	protected String[] data = new String[2];

	public SimpleServer(int port){
		this.serverPort = port;
	}

	public void run(){
		synchronized(this){
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		
		
		while(! isStopped()){
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (IOException e) {
				if(isStopped()) {
					// System.out.println("Server Stopped.") ;
					return;
				}
				throw new RuntimeException(
						"Error accepting client connection", e);
			}
			WorkerRunnable worker = new WorkerRunnable(clientSocket, "Multithreaded Server");
			worker.run();
			data = worker.getData();
			
		}
		
		System.out.println("Server Stopped.") ;
	}
	
	public String[] getData(){
		return data;
	}
	
	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop(){
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port 8080", e);
		}
	}

}