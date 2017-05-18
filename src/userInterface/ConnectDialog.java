package userInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.Client;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class ConnectDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField hostField;
	private JTextField portField;
	private JButton okButton;
	private JButton cancelButton;

	/**
	 * Create the dialog.
	 */
	public ConnectDialog(JFrame frame) {
		setBounds(100, 100, 244, 129);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblHost = new JLabel("Host:");
			GridBagConstraints gbc_lblHost = new GridBagConstraints();
			gbc_lblHost.anchor = GridBagConstraints.EAST;
			gbc_lblHost.insets = new Insets(0, 0, 5, 5);
			gbc_lblHost.gridx = 0;
			gbc_lblHost.gridy = 0;
			contentPanel.add(lblHost, gbc_lblHost);
		}
		{
			hostField = new JTextField();
			GridBagConstraints gbc_hostField = new GridBagConstraints();
			gbc_hostField.insets = new Insets(0, 0, 5, 0);
			gbc_hostField.fill = GridBagConstraints.HORIZONTAL;
			gbc_hostField.gridx = 1;
			gbc_hostField.gridy = 0;
			contentPanel.add(hostField, gbc_hostField);
			hostField.setColumns(10);
		}
		{
			JLabel lblPort = new JLabel("Port");
			GridBagConstraints gbc_lblPort = new GridBagConstraints();
			gbc_lblPort.anchor = GridBagConstraints.EAST;
			gbc_lblPort.insets = new Insets(0, 0, 0, 5);
			gbc_lblPort.gridx = 0;
			gbc_lblPort.gridy = 1;
			contentPanel.add(lblPort, gbc_lblPort);
		}
		{
			portField = new JTextField();
			GridBagConstraints gbc_portField = new GridBagConstraints();
			gbc_portField.fill = GridBagConstraints.HORIZONTAL;
			gbc_portField.gridx = 1;
			gbc_portField.gridy = 1;
			contentPanel.add(portField, gbc_portField);
			portField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Verbinden");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Abbrechen");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					portField.setBackground(Color.WHITE);
					Client.setConnection(hostField.getText(), Integer.parseInt(portField.getText()));
				} catch (NumberFormatException e1) {
					portField.setBackground(Color.RED);
				}
				GUI.newClient();
				GUI.getClient().start();
			}
		});
		this.setModal(true);
	}

}
