package games.catan.gameLogic;

import java.util.HashMap;

import dataManagement.Data;
import games.catan.renderer.Renderer;

public class Catan {

	private Renderer renderer;

	public Catan(Renderer renderer) {
		this.renderer = renderer;
	}

	private Tile[] tiles;
	private Road[] roads;
	private Crossroad[] crossroads;

	public void setup(String setup) {
		tiles = Tile.generateTiles(setup);
		roads = Road.generateRoads();
		crossroads = Crossroad.generateCrossroads();
	}

	public Road[] getRoads() {
		return roads;
	}

	public Crossroad[] getCrossroads() {
		return crossroads;
	}

	public int getResource(int id) {
		return tiles[id].getResource();
	}

	public int getDice(int id) {
		return tiles[id].getDice();
	}

	public void execute(String toExecute) {
		System.out.println(toExecute);
		String[] split = toExecute.split("\\?");
		String command = split[0];
		HashMap<String, String> params = Data.parseParameters(split[1], "&");
		switch(command){
		case "mouseclick":
			switch(params.get("object")){
			case "crossroad":
				int id = Integer.parseInt(params.get("id"));
				int own = (crossroads[id].getOwner() + 1) % 4;
				crossroads[id].setOwner(own);
				break;
			case "road":
				roads[Integer.parseInt(params.get("id"))].setOwner(2);
			}
			break;
		}
		
		renderer.repaint();
	}
}
