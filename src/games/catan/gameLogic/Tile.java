package games.catan.gameLogic;

public class Tile {
	private int id;
	private int resource;
	private int dice;
	private static Tile[] tiles;

	public Tile(int id, int res, int dice) {
		this.id = id;
		this.resource = res;
		if (dice < 5)
			this.dice = dice + 2;
		else
			this.dice = dice + 3;
	}

	public int getId() {
		return id;
	}

	public int getResource() {
		return resource;
	}

	public int getDice() {
		return dice;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setRessource(int res) {
		this.resource = res;
	}

	public static Tile[] generateTiles(String s) {
		tiles = new Tile[37];

		for (int i = 0; i < 19; i++) {
			tiles[i] = new Tile(i, Character.getNumericValue(s.charAt(i)), Character.getNumericValue(s.charAt(i + 19)));
		}
		for (int i = 19; i < 37; i++) {
			tiles[i] = new Tile(i, 6, -1);
		}
		// TODO Handling for water-tiles
		return tiles;
	}
}
