package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import dataManagement.Data;
import userInterface.GUI;

public class Client extends Thread {
	public boolean write = true;
	private PrintStream os = null;

	private boolean connected = false;
	private boolean loggedIn = false;
	private GUI gui;
	private static String host;
	private static int port;
	private ClientListener listener;

	private String Username;

	public Client(GUI gui) {
		this.gui = gui;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isConnected() {
		return connected;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public String getUsername() {
		return Username;
	}

	public void run() {

		Socket clientSocket = null;

		/*
		 * TODO Open a socket on given port. Open the the output stream
		 */
		try {

			clientSocket = new Socket(host, port);
			os = new PrintStream(clientSocket.getOutputStream());

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to host");
		}

		/*
		 * If everything has been initialized then we want to write some data to
		 * the socket we have opened a connection to on port 2222.
		 */
		if (clientSocket != null && os != null) {

			connected = true;

			try {

				/*
				 * Create and start the listener. The listener constantly
				 * listens to the Server
				 */
				System.out.println("The client started.");

				listener = new ClientListener(this, clientSocket);
				listener.start();

				/*
				 * Disposes the dialog
				 */
				gui.getConnectDialog().dispose();

				/*
				 * Keeps the Thread alive, is stopped when the Server sends
				 * disconnect
				 */
				while (write) {

				}

				/*
				 * Close the output stream, close the input stream, close the
				 * socket.
				 */
				os.close();
				clientSocket.close();

			} catch (UnknownHostException e) {
				System.err.println("Trying to connect to unknown host: " + e);
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		} else {
			GUI.setClient(null);
			gui.popup("Verbindung fehlgeschlagen");
			try {
				this.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setConnection(String host, int port) {
		Client.host = host;
		Client.port = port;
	}

	public void execute(String toExecute) {
		String[] split = toExecute.split("\\?");
		String command = split[0];
		HashMap<String, String> params = Data.parseParameters(split[1], "&");

		switch (command) {
		case "register":
			if (params.get("register").equals("true")) {
				JOptionPane.showMessageDialog(null, "Registrierung erfolgreich");
			} else {
				JOptionPane.showMessageDialog(null, "Registrierung fehlgeschlagen");
			}
			break;
		case "login":
			if (params.get("login").equals("true")) {
				JOptionPane.showMessageDialog(null, "Login erfolgreich");
				Username = params.get("name");
				loggedIn = true;
			} else {
				JOptionPane.showMessageDialog(null, "Login fehlgeschlagen");
			}
			break;
		case "error":
			JOptionPane.showMessageDialog(null, params.get("error"));
			break;
		case "joingame":
			switch (params.get("game")) {
			case "Catan":
				gui.startGame(params.get("setup"));
				break;
			}
			break;
		case "userjoin":
			Data.writeToLog(toExecute);
			break;
		}
		gui.setStatus();
	}

	/**
	 * Stops the client by sending a request to the server
	 */
	public void terminate() {
		try {
			listener.terminate();
			GUI.setClient(null);
			write = false;
			sendDataToServer("disconnect");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void register(String name, String pw) {
		os.println("register?name=" + name + "&password=" + pw);
		os.flush();
	}

	public void login(String name, String pw) {
		os.println("login?name=" + name + "&password=" + pw);
		os.flush();
	}
	
	public void setStatus(){
		gui.setStatus();
	}
	
	public void popup(String message){
		gui.popup(message);
	}
	
	public void blockGame(){
		gui.blockGame();
	}

	public void sendDataToServer(String data) {
		try {
			os.println(data);
			os.flush();
		} catch (NullPointerException e) {
			System.err.println("No connection");
		}
	}
}