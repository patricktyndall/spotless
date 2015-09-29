package authenticate;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class ServerThread implements Runnable{

	protected int          serverPort   = 8080;
	protected ServerSocket serverSocket = null;
	public boolean      isStopped    = false; // TODO public?
	protected Thread       runningThread= null;
	protected String[] data;

	public ServerThread(int port){
		this.serverPort = port;
	}

	public void run(){
		
		synchronized(this){
			this.runningThread = Thread.currentThread();
		}
		
		openServerSocket();

		Socket clientSocket = null;

		try {
			clientSocket = this.serverSocket.accept();
		} catch (IOException e) {
			if(isStopped()) {
				return;
			}
			throw new RuntimeException(
					"Error accepting client connection", e);
		}

		WorkerRunnable worker = new WorkerRunnable(clientSocket, "Multithreaded Server");
		worker.run();
		data = worker.getData();
		this.stop();
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
			throw new RuntimeException("Cannot open port " + serverPort, e);
		}
	}
}