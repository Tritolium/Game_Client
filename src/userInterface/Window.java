package userInterface;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import games.catan.renderer.Renderer;

public class Window extends JComponent {

	private static final long serialVersionUID = 1L;
	private Renderer renderer;

	public Window(){
		this.renderer = new Renderer(this);
		renderer.loadImages();
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				renderer.mouseClicked(e);
				repaint();
			}
		});
		this.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e){
				renderer.calc();
				renderer.scale();
				repaint();
			}
		});
		this.setVisible(true);
	}
	
	public void init(String setup){
		renderer.getGame().setup(setup);
		renderer.loadMap("standard");
	}
	
	public Renderer getRenderer(){
		return renderer;
	}
	
	@Override
	public void paintComponent(Graphics g){
		renderer.setHeight(this.getSize());
		renderer.render((Graphics2D) g);
	}
}
