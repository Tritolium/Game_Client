package games.catan.gameLogic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public class Crossroad {

	private int id;
	private float posX;
	private float posY;
	private int owner;
	private int build;
	private Polygon polygon;

	private static Crossroad[] crossroads;

	public Crossroad(int owner, int build) {
		this.owner = owner;
		this.build = build;
	}

	public int getId() {
		return id;
	}
	
	public int getOwner(){
		return owner;
	}
	
	public int getBuild(){
		return build;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setOwner(int own) {
		this.owner = own;
	}

	public void setBuild(int build) {
		this.build = build;
	}

	public static Crossroad[] generateCrossroads() {
		crossroads = new Crossroad[54];

		
		for (int i = 0; i < 54; i++) {
			crossroads[i] = new Crossroad(1,2);
		}

		return crossroads;
	}

	public static Crossroad check(Point p) {
		for (Crossroad c : crossroads) {
			if (c.getPolygon().contains(p)) {
				return c;
			}
		}
		return null;
	}

	public void drawCrossroad(Graphics2D g, int xStart, int yStart, int radius) {
		int x = (int) (xStart + posX * radius / 2 * Math.sqrt(3.0));
		int y = (int) (yStart + posY * 1.5 * radius - 0.1 * radius);

		switch (build) {
		case 0:
			int[] xPoints = new int[4];
			int[] yPoints = new int[4];
			xPoints[0] = xPoints[1] = (int) (x + 0.15 * radius);
			xPoints[2] = xPoints[3] = (int) (x - 0.15 * radius);
			yPoints[0] = yPoints[3] = (int) (y - 0.05 * radius);
			yPoints[1] = yPoints[2] = (int) (y + 0.25 * radius);
			polygon = new Polygon(xPoints, yPoints, 4);
			break;
		case 1:
			xPoints = new int[5];
			yPoints = new int[5];
			xPoints[0] = x;
			xPoints[1] = (int) (x + 0.2 * radius);
			xPoints[2] = (int) (x + 0.2 * radius);
			xPoints[3] = (int) (x - 0.2 * radius);
			xPoints[4] = (int) (x - 0.2 * radius);
			yPoints[0] = (int) (y - 0.2 * radius);
			yPoints[1] = y;
			yPoints[2] = (int) (y + 0.3 * radius);
			yPoints[3] = (int) (y + 0.3 * radius);
			yPoints[4] = y;
			polygon = new Polygon(xPoints, yPoints, 5);
			break;
		}

		switch (owner) {
		case 0:
			g.setColor(Color.BLUE);
			g.fill(polygon);
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(1.0f));
			g.draw(polygon);
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		}
	}
}
