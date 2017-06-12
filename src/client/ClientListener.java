package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import org.tritol.server.GameExchange;

public class ClientListener extends Thread {

	private Client client;
	private Socket clientSocket;
	private ObjectInputStream ois;
	private boolean running = true;

	public ClientListener(Client client, Socket clientSocket) {
		this.client = client;
		this.clientSocket = clientSocket;
	}

	public void run() {
		try {
			GameExchange response;
			ois = new ObjectInputStream(clientSocket.getInputStream());

			while (running) {
				response = (GameExchange) ois.readObject();
				if (response.getMethod()=="disconnect") {
					client.write = false;
					client.setStatus();
					break;
				}
				client.handle(response);
				System.out.println(response);
			}
		} catch(SocketException e){
			System.err.println("Connection-Reset");
			client.setConnected(false);
			client.setStatus();
			client.popup("Lost connection to Server");
			client.blockGame();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void terminate() throws IOException{
		running = false;
	}
}
