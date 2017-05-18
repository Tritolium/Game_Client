package userInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.Client;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public RegisterDialog register;
	public LoginDialog login;
	protected Window gameWindow;
	protected ConnectDialog connect;
	private JLabel lblStatus;
	protected static GUI frame;
	private static Client client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new GUI();
					frame.setSize(500, 400);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static Client getClient() {
		return client;
	}

	public ConnectDialog getConnectDialog() {
		return connect;
	}

	public static void setClient(Client c) {
		GUI.client = c;
	}

	public static void newClient() {
		client = new Client(frame);
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnConnection = new JMenu("Verbindung");
		menuBar.add(mnConnection);

		JMenuItem mntmConnect = new JMenuItem("Verbinden");
		mnConnection.add(mntmConnect);

		JMenuItem mntmDisconnect = new JMenuItem("Trennen");
		mnConnection.add(mntmDisconnect);

		JMenu mnUser = new JMenu("Benutzer");
		menuBar.add(mnUser);

		JMenuItem mntmRegister = new JMenuItem("Registrieren");
		mnUser.add(mntmRegister);

		JMenuItem mntmLogin = new JMenuItem("Login");
		mnUser.add(mntmLogin);

		JMenu mnGames = new JMenu("Spiele");
		menuBar.add(mnGames);

		JMenuItem mntmCatan = new JMenuItem("Catan");
		mnGames.add(mntmCatan);

		JMenu mnZoom = new JMenu("Zoom");
		mnGames.add(mnZoom);

		JMenuItem menuItem = new JMenuItem("0.75");
		mnZoom.add(menuItem);

		JMenuItem menuItem_1 = new JMenuItem("1.0");
		mnZoom.add(menuItem_1);

		JMenuItem menuItem_2 = new JMenuItem("1.25");
		mnZoom.add(menuItem_2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		mntmConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect = new ConnectDialog(frame);
				connect.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				connect.setVisible(true);
				setStatus();
			}
		});
		mntmDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client != null)
					client.terminate();
				else
					JOptionPane.showMessageDialog(contentPane, "Nicht verbunden");
			}
		});
		mntmRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client != null && client.isConnected()) {
					register = new RegisterDialog();
					register.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					register.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(contentPane, "Nicht verbunden");
				}
			}
		});
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client != null && client.isConnected()) {
					login = new LoginDialog();
					login.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					login.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(contentPane, "Nicht verbunden");
				}
			}
		});
		mntmCatan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client != null && client.isConnected()) {
					if (client.isLoggedIn()) {
						// TODO order CreateGame from Server
						client.sendDataToServer("joingame?game=Catan");
						/*
						 * setup: 0-18: tileres 19-37: tiledice 38-55: portres
						 * 56-73: portori
						 */
						// gameWindow = new Window();
						// gameWindow.init("53411505415322241344031670528157342698060510063002100101000151515242524333");
						// contentPane.add(gameWindow, BorderLayout.CENTER);
						// contentPane.validate();
					} else {
						popup("Nicht eingeloggt");
					}
				} else {
					popup("Nicht verbunden");
				}
			}
		});
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameWindow.getRenderer().setFactor(0.75);
			}
		});
		menuItem_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameWindow.getRenderer().setFactor(1.0);
			}
		});
		menuItem_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameWindow.getRenderer().setFactor(1.25);
			}
		});
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.EAST);

		JLabel lblIchBinEin = new JLabel("Ich bin ein Men\u00FC");
		panel.add(lblIchBinEin);

		lblStatus = new JLabel("");
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblStatus, BorderLayout.SOUTH);

	}

	public void startGame(String setup) {
		gameWindow = new Window();
		gameWindow.init(setup);
		contentPane.add(gameWindow, BorderLayout.CENTER);
		contentPane.repaint();
		contentPane.validate();
	}

	public void popup(String message) {
		JOptionPane.showMessageDialog(contentPane, message);
	}
	
	public void blockGame(){
		contentPane.setEnabled(false);
		contentPane.setVisible(false);
		repaint();
	}

	public void setStatus() {
		if (client != null && client.isConnected()) {
			lblStatus.setText("Verbunden");
			if (client.isLoggedIn()) {
				lblStatus.setText("Verbunden, " + client.getUsername());
			}
		}else{
			lblStatus.setText("Nicht verbunden");
		}
	}
}
