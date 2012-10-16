package com.golddigger.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.golddigger.astar.CoordPathFinder;
import com.golddigger.model.Coordinate;
import com.golddigger.model.Direction;
import com.golddigger.model.Path;
import com.golddigger.service.ArrayTiledMap;
import com.golddigger.service.ServerService;
import com.golddigger.service.TiledMap;

public class GoldCollector {
	
	private class Goal implements Comparable<Goal> {

		Coordinate coordinate;
		Path path;
		int cost;
		int gold;

		public Goal(Coordinate coordinate, List<Direction> steps, int gold) {
			this.coordinate = coordinate;
			this.path = new Path(steps);
			this.cost = steps.size();
			this.gold = gold;
		}
		
		@Override
		public String toString(){
			return coordinate + " " + path.toString() + " " + cost + " " + gold;
		}

		@Override
		public int compareTo(Goal o) {
			if(this.cost < o.cost)
				return -1;
			if(this.cost > o.cost)
				return 1;
			else
				return 0;
		}

	}

	private TiledMap map;
	private ServerService client;
	private Coordinate baseCoordinate;
	private List<Goal> goals =  new ArrayList<Goal>();
	Coordinate startCoord;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String field = 	"wwwww\n" +
						"wbw1w\n" + 
						"w.w1w\n" + 
						"w9..w\n" + 
						"wwwww\n";

		ArrayTiledMap map = new ArrayTiledMap(64, 64, new Coordinate(32, 32));
		map.add(new Coordinate(0, 0), field);

		GoldCollector gc = new GoldCollector(map, null);

		gc.setGoals();
		gc.getGold();

	}

	public GoldCollector(TiledMap map, ServerService client) {
		ArrayTiledMap tmp = (ArrayTiledMap) map;
		//Normalise the coords back to 0,0
//		int normalisedClientLat = client.getCurrentPosition().lat - tmp.getSlat();
//		int normalisedClientLng = client.getCurrentPosition().lng - tmp.getSlng();
//		startCoord = new Coordinate(normalisedClientLat, normalisedClientLng);
//		System.out.println(startCoord.toString());
		System.out.println(client.getCurrentPosition().toString());
		this.map = new ArrayTiledMap(map.toArray());
		this.client = client;
		getBase();
	}
	
	public void getBase() {
		char[][] mapArray = map.toArray();

		for (int lat = 0; lat < mapArray.length; lat++) {
			for (int lng = 0; lng < mapArray[0].length; lng++) {
				char currentTile = map.get(new Coordinate(lat, lng));
				if (currentTile == 'b') {
					System.out.println("Base at ("+lat+", "+lng+")");
					this.baseCoordinate = new Coordinate(lat, lng);
				}
			}
		}
	}

	public void setGoals() {
		System.out.println("Settings the goals");
		char[][] mapArray = map.toArray();
		
		

		for (int lat = 0; lat < mapArray.length; lat++) {
			for (int lng = 0; lng < mapArray[0].length; lng++) {
				Coordinate coord = new Coordinate(lat, lng);
				char tile = map.get(coord);
//				System.out.println(tile);
				switch (tile) {
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					System.out.println(tile+" gold at ("+lat+", "+lng+")");
					List<Direction> steps = buildPathSteps(coord, baseCoordinate);
						goals.add(new Goal(coord, steps, Integer.parseInt(tile + "")));
					break;
				default:
					break;
				}
			}
		}
		
		Collections.sort(goals);
		
		for(Goal g : goals){
			System.out.println(g.toString());
		}
		
		System.out.println("Done setting goals");
	}
	
	private List<Direction> buildPathSteps(Coordinate goal, Coordinate start) {
		CoordPathFinder finder = new CoordPathFinder(map, goal);
		List<Coordinate> nodes = finder.compute(start);
		if(nodes == null || nodes.size() < 1)
			return null;
		List<Direction> steps = new ArrayList<Direction>();
		for (int i = 0; i < nodes.size()-1; i++){
			Direction step = Direction.parse(nodes.get(i+1).sub(nodes.get(i)));
			if (step == null) continue;
			else steps.add(step);
		}
		if(steps.size() < 1)
			return null;
		else
			return steps;
	}

	public void getGold() {
		System.out.println("Getting the gold now");
		
		List<Direction> steps = buildPathSteps(baseCoordinate, baseCoordinate);
		
		if (steps != null && steps.size() >= 1){
			getAndBank(new Goal(baseCoordinate, steps, -1));
		}
		for(Goal goal : goals){
			getAndBank(goal);
		}
	}

	private void getAndBank(Goal goal) {
		if(goal.gold == -1){
			while (goal.path.hasNext()) {
				client.move(goal.path.next());
			}
			return;
		}
		while (goal.gold > 0) {
			System.out.println("Now going for gold at " +  goal.coordinate.toString());
			while (goal.path.hasNext()) {
				client.move(goal.path.next());
			}
			client.grab();
			while (goal.path.hasPrevious()) {
				client.move(goal.path.previous().reverse());
			}
			String amount = client.drop().trim();
			goal.gold =- Integer.parseInt(amount);
			client.next();
		}
		System.out.println("Finished getting gold at " + goal.coordinate.toString());
	}
}
