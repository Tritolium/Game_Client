package userInterface;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.TextArea;

public class GamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4967810612382973750L;
	private TextArea infoText;


	/**
	 * Create the panel.
	 */
	public GamePanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));
		
		infoText = new TextArea();
		panel.add(infoText, BorderLayout.SOUTH);
		infoText.setColumns(10);
		infoText.setRows(5);
	}

	
	public void addComponent(JComponent comp, Object constraints){
		this.add(comp, constraints);
	}
	
	public void writeInfo(String info){
		infoText.setText(info);
	}
}
