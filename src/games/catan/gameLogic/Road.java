package games.catan.gameLogic;

public class Road {
	private int id;
	private int orientation;
	private int owner;

	private static Road[] roads;

	public Road(int own) {
		this.owner = own;
	}

	public int getId() {
		return id;
	}

	public int getOrientation() {
		return orientation;
	}

	public int getOwner() {
		return owner;
	}

	public void setOrientation(int ori) {
		this.orientation = ori;
	}

	public void setOwner(int own) {
		this.owner = own;
	}

	public static Road[] generateRoads() {
		roads = new Road[72];

		for (int i = 0; i < 72; i++) {
			roads[i] = new Road(1);
		}

		return roads;
	}
}
