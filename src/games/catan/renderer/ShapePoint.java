package games.catan.renderer;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

import dataManagement.Data;

public class ShapePoint {

	private static double radius;
	private static double xStart;
	private static double yStart;
	private static double factor = 1.0;

	private double xPos;
	private double yPos;
	
	private static HashMap<Integer, HashMap<String, String>> roadShape;
	private static HashMap<Integer, HashMap<String, String>> settlementShape;
	private static HashMap<Integer, HashMap<String, String>> townShape;

	public ShapePoint(double x, double y) {
		this.xPos = x;
		this.yPos = y;
	}

	public double getX() {
		return (xStart + xPos * radius / 2 * Math.sqrt(3.0));
	}

	public double getY() {
		return (yStart + yPos * 1.5 * radius);
	}

	public static void setFactor(double factor) {
		ShapePoint.factor = factor;
	}

	public static void setParameters(double radius, double xStart, double yStart) {
		ShapePoint.radius = radius;
		ShapePoint.xStart = xStart;
		ShapePoint.yStart = yStart;
	}
	
	public static void loadShapes(){
		roadShape = Data.readData("Catan/data/road_shape.txt");
		settlementShape = Data.readData("Catan/data/settlement_shape.txt");
		townShape = Data.readData("Catan/data/town_shape.txt");
	}

	public Shape getTileShape() {

		int[] xPoints = new int[6];
		int[] yPoints = new int[6];

		double x = xStart + xPos * radius / 2 * Math.sqrt(3.0);
		double y = yStart + yPos * 1.5 * radius;

		for (int i = 0; i < 6; i++) {
			xPoints[i] = (int) (x + radius * Math.cos((i + 0.5f) * 2 * Math.PI / 6));
			yPoints[i] = (int) (y + radius * Math.sin((i + 0.5f) * 2 * Math.PI / 6));
		}

		return (new Polygon(xPoints, yPoints, 6));
	}

	public Shape getRoadShape(int orientation) {
		
		HashMap<String, String> params = roadShape.get(0);

		double width = Data.parseDouble(params.get("width"));
		double height = Data.parseDouble(params.get("height"));
		width *= radius * factor;
		height *= radius * factor;

		double x = xStart + xPos * radius / 2 * Math.sqrt(3.0) - 0.5 * width;
		double y = yStart + yPos * 1.5 * radius - 0.5 * height;

		Rectangle rect = new Rectangle((int) x, (int) y, (int) (width), (int) (height));

		AffineTransform at = new AffineTransform();
		switch (orientation) {
		case 0:
			at.setToRotation(0.5 * Math.PI, x + 0.5 * width, y + 0.5 * height);
			break;
		case 1:
			at.setToRotation(-0.5, x + 0.5 * width, y + 0.5 * height);
			break;
		case 2:
			at.setToRotation(0.5, x + 0.5 * width, y + 0.5 * height);
			break;
		}
		return at.createTransformedShape(rect);
	}

	public Shape getCrossroadShape(int build) {
		HashMap<String, String> params;
		int size;

		double x = xStart + xPos * radius / 2 * Math.sqrt(3.0);
		double y = yStart + yPos * 1.5 * radius - 0.1 * radius;

		switch (build) {
		default:
			int[] xPoints = new int[4];
			int[] yPoints = new int[4];

			xPoints[0] = xPoints[1] = (int) (x + 0.15 * radius);
			xPoints[2] = xPoints[3] = (int) (x - 0.15 * radius);
			yPoints[0] = yPoints[3] = (int) (y - 0.05 * radius);
			yPoints[1] = yPoints[2] = (int) (y + 0.25 * radius);
			return new Polygon(xPoints, yPoints, 4);
		case 1:

			size = settlementShape.size();

			xPoints = new int[size];
			yPoints = new int[size];

			for (int i = 0; i < size; i++) {
				params = settlementShape.get(i);
				xPoints[i] = (int) (x + Data.parseDouble(params.get("x")) * radius * factor);
				yPoints[i] = (int) (y + Data.parseDouble(params.get("y")) * radius * factor);
			}

			return new Polygon(xPoints, yPoints, size);
		case 2:

			size = townShape.size();

			xPoints = new int[size];
			yPoints = new int[size];

			for (int i = 0; i < size; i++) {
				params = townShape.get(i);
				xPoints[i] = (int) (x + Data.parseDouble(params.get("x")) * radius * factor);
				yPoints[i] = (int) (y + Data.parseDouble(params.get("y")) * radius * factor);
			}
			// xPoints = new int[7];
			// yPoints = new int[7];
			//
			// xPoints[0] = (int) (x + 0.15 * radius);
			// xPoints[1] = xPoints[2] = (int) (x + 0.3 * radius);
			// xPoints[3] = xPoints[4] = (int) (x - 0.33 * radius);
			// xPoints[5] = xPoints[6] = (int) x;
			//
			// yPoints[0] = (int) (y - 0.415 * radius);
			// yPoints[1] = yPoints[6] = (int) (y - 0.265 * radius);
			// yPoints[2] = yPoints[3] = (int) (y + 0.2 * radius);
			// yPoints[4] = yPoints[5] = (int) (y - 0.1 * radius);

			return new Polygon(xPoints, yPoints, size);
		}
	}
}
