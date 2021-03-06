package games.catan.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import games.catan.gameLogic.Catan;

import dataManagement.Data;

public class Renderer {

	private static final Color orange = new Color(255, 127, 0);
	private static final Color green = new Color(34, 139, 34);
	private static final Color red = new Color(205, 0, 0);

	private double height;
	private double radius;
	private double startX;
	private double startY;

	private ShapePoint[] tileCoords;
	private ShapePoint[] roadCoords;
	private ShapePoint[] crossroadCoords;

	private Shape[] tiles;
	private Shape[] roads;
	private Shape[] crossroads;
	private int t_index;
	private int r_index;
	private int c_index;
	private Image[] images;
	private Image[] simages;
	private JComponent component;
	private Catan game;
	private Image background;

	public Renderer(JComponent component) {
		this.component = component;
		this.game = new Catan(this);
	}

	public Catan getGame() {
		return game;
	}

	public void setHeight(Dimension size) {

		if (size.getHeight() < (size.getWidth() - (size.getHeight()/5.5)))
			height = size.getHeight();
		else{
			height = size.getWidth();
			height -= (height/5.5) + 5;
		}		

		radius = (height / 11);
		startX = radius + 5;
		startY = (height / 2);

		ShapePoint.setParameters(radius, startX, startY);
	}

	public void setFactor(Double factor) {
		ShapePoint.setFactor(factor);
		calc();
		component.repaint();
	}

	public void mouseClicked(MouseEvent e) {
		Point2D p = e.getPoint();

		for (int i = 0; i < c_index; i++) {
			if (crossroads[i].contains(p)) {
				game.execute("mouseclick?object=crossroad&id=" + i);
				return;
			}
		}
		for (int i = 0; i < r_index; i++) {
			if (roads[i].contains(p)) {
				game.execute("mouseclick?object=road&id=" + i);
				return;
			}
		}
		for (int i = 0; i < t_index; i++) {
			if (tiles[i].contains(p)) {
				game.execute("mouseclick?object=tile?id=" + i);
				return;
			}
		}
	}

	public void loadImages() {
		background = null;
		images = new Image[7];
		simages = new Image[7];
		try {
			background = ImageIO.read(new File("Catan/images/background.jpg"));
			images[0] = ImageIO.read(new File("Catan/images/desert.png"));
			images[1] = ImageIO.read(new File("Catan/images/sheep.png"));
			images[2] = ImageIO.read(new File("Catan/images/clay.png"));
			images[3] = ImageIO.read(new File("Catan/images/ore.png"));
			images[4] = ImageIO.read(new File("Catan/images/wheat.png"));
			images[5] = ImageIO.read(new File("Catan/images/wood.png"));
			images[6] = ImageIO.read(new File("Catan/images/water.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadMap(String loadout) {
		try {
			switch (loadout) {
			case "standard":
				t_index = 37;
				r_index = 72;
				c_index = 54;
				break;
			}

			tileCoords = new ShapePoint[t_index];
			roadCoords = new ShapePoint[r_index];
			crossroadCoords = new ShapePoint[c_index];
			tiles = new Shape[t_index];
			roads = new Shape[r_index];
			crossroads = new Shape[c_index];

			FileReader fin = null;
			BufferedReader in = null;
			String line;
			HashMap<String, String> params;

			File tileCoord = new File("Catan/data/tile_" + loadout + ".txt");
			File roadCoord = new File("Catan/data/road_" + loadout + ".txt");
			File crossroadCoord = new File("Catan/data/crossroad_" + loadout + ".txt");

			fin = new FileReader(tileCoord);
			in = new BufferedReader(fin);

			while ((line = in.readLine()) != null) {
				params = Data.parseParameters(line, ",");
				int id = Integer.parseInt(params.get("id"));
				tileCoords[id] = new ShapePoint(parseDouble(params.get("x")), parseDouble(params.get("y")));
			}
			fin.close();
			fin = new FileReader(roadCoord);
			in = new BufferedReader(fin);

			while ((line = in.readLine()) != null) {
				params = Data.parseParameters(line, ",");
				int id = Integer.parseInt(params.get("id"));
				roadCoords[id] = new ShapePoint(parseDouble(params.get("x")), parseDouble(params.get("y")));
				game.getRoads()[id].setOrientation(Integer.parseInt(params.get("o")));
			}
			fin.close();
			fin = new FileReader(crossroadCoord);
			in = new BufferedReader(fin);

			while ((line = in.readLine()) != null) {
				params = Data.parseParameters(line, ",");
				int id = Integer.parseInt(params.get("id"));
				crossroadCoords[id] = new ShapePoint(parseDouble(params.get("x")), parseDouble(params.get("y")));
			}
			fin.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		ShapePoint.loadShapes();
	}

	public void scale() {
		for (int i = 0; i < images.length; i++) {
			simages[i] = images[i].getScaledInstance((int) (2 * radius), (int) (2 * radius), ImageObserver.ALLBITS);
		}
	}

	public void calc() {
		for (int i = 0; i < t_index; i++) {
			tiles[i] = tileCoords[i].getTileShape();
		}
		for (int i = 0; i < r_index; i++) {
			roads[i] = roadCoords[i].getRoadShape(game.getRoads()[i].getOrientation());
		}
		for (int i = 0; i < c_index; i++) {
			crossroads[i] = crossroadCoords[i].getCrossroadShape(game.getCrossroads()[i].getBuild());
		}

		// TODO roads, crossroads
	}

	public void render(Graphics2D g) {
		// draw background
		g.drawImage(background, 0, 0, component);
		// draw tiles
		for (int i = 0; i < 19; i++) {
			Shape s = tiles[i];
			ShapePoint sp = tileCoords[i];
			int resource = game.getResource(i);
			int dice = game.getDice(i);
			int x = (int) (sp.getX() - 0.25 * radius);
			int y = (int) (sp.getY() - 0.25 * radius);
			try {
				g.setClip(s);
				g.drawImage(simages[resource], (int) s.getBounds2D().getX(), (int) s.getBounds2D().getY(), component);
				g.setClip(null);
				g.setColor(Color.ORANGE);
				g.setStroke(new BasicStroke((float) (radius / 20)));
				g.draw(s);
				if (resource != 0 && resource != 6) {
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke((int) radius / 20));
					g.drawOval(x, y, (int) (0.5 * radius), (int) (0.5 * radius));
					g.setColor(Color.orange);
					g.fillOval(x, y, (int) (0.5 * radius), (int) (0.5 * radius));

					g.setFont(new Font("Arial", Font.PLAIN, (int) (0.2 * radius)));
					x = (int) ((x + 0.25 * radius) - 0.5 * g.getFontMetrics().stringWidth("" + dice));
					y = (int) ((y + 0.25 * radius) + 0.25 * g.getFontMetrics().getHeight());
					g.setColor(Color.BLACK);
					g.drawString("" + dice, x, y);
				}
			} catch (NullPointerException e) {
			}
		}
		
		for (int i = 19; i < t_index; i++){
			Shape s = tiles[i];
			ShapePoint sp = tileCoords[i];
			int resource = game.getResource(i);
			int orientation = game.getDice(i);
			try{
				g.setClip(s);
				g.drawImage(simages[6], (int) s.getBounds2D().getX(), (int) s.getBounds2D().getY(), component);
				g.setClip(null);
				g.setColor(Color.ORANGE);
				g.setStroke(new BasicStroke((float) (radius / 20)));
				g.draw(s);
				//TODO draw harbor
			}catch(NullPointerException e){
				
			}
		}

		// draw roads
		for (int i = 0; i < r_index; i++) {
			Shape s = roads[i];
			try {
				switch (game.getRoads()[i].getOwner()) {
				case 0:
					g.setColor(Color.BLUE);
					g.fill(s);
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(1.0f));
					g.draw(s);
					break;
				case 1:
					g.setColor(red);
					g.fill(s);
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(1.0f));
					g.draw(s);
					break;
				case 2:
					g.setColor(green);
					g.fill(s);
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(1.0f));
					g.draw(s);
					break;
				case 3:
					g.setColor(orange);
					g.fill(s);
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(1.0f));
					g.draw(s);
					break;
				}
			} catch (Exception e) {

			}
		}

		// draw crossroads
		for (int i = 0; i < c_index; i++) {
			Shape s = crossroads[i];
			try {
				switch (game.getCrossroads()[i].getOwner()) {
				case 0:
					g.setColor(Color.BLUE);
					g.fill(s);
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(1.0f));
					g.draw(s);
					break;
				case 1:
					g.setColor(red);
					g.fill(s);
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(1.0f));
					g.draw(s);
					break;
				case 2:
					g.setColor(green);
					g.fill(s);
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(1.0f));
					g.draw(s);
					break;
				case 3:
					g.setColor(orange);
					g.fill(s);
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(1.0f));
					g.draw(s);
					break;
				}
			} catch (Exception e) {

			}
		}
	}

	public void repaint() {
		component.repaint();
	}

	private double parseDouble(String arg) {
		if (arg.contains("/")) {
			String[] rat = arg.split("/");
			return (Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]));
		} else {
			return Double.parseDouble(arg);
		}
	}
}
