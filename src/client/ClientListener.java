package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientListener extends Thread {

	private Client client;
	private Socket clientSocket;
	private BufferedReader is;
	private boolean running = true;

	public ClientListener(Client client, Socket clientSocket) {
		this.client = client;
		this.clientSocket = clientSocket;
	}

	public void run() {
		try {
			String responseLine;
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			while (running) {
				responseLine = is.readLine();
				if (responseLine.indexOf("disconnect") != -1) {
					client.write = false;
					client.setStatus();
					break;
				}
				client.execute(responseLine);
				System.out.println(responseLine);
			}
		} catch(SocketException e){
			System.err.println("Connection-Reset");
			client.setConnected(false);
			client.setStatus();
			client.popup("Lost connection to Server");
			client.blockGame();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void terminate() throws IOException{
		running = false;
	}
}
