package com.golddigger.util;

import java.util.ArrayList;
import java.util.Arrays;
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

		public Goal(Coordinate coordinate, Path path, int cost) {
			this.coordinate = coordinate;
			this.path = path;
			this.cost = cost;
		}
		
		@Override
		public String toString(){
			return coordinate + " " + path.toString() + " " + cost;
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
		this.map = map;
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
				System.out.println(tile);
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
					CoordPathFinder finder = new CoordPathFinder(map, coord);
					List<Coordinate> nodes = finder.compute(baseCoordinate);
					List<Direction> steps = new ArrayList<Direction>();
					for (int i = 0; i < nodes.size()-1; i++){
						Direction step = Direction.parse(nodes.get(i+1).sub(nodes.get(i)));
						if (step == null) continue;
						else steps.add(step);
					}
					if (steps.size() < 1) continue;
					else
						goals.add(new Goal(coord, new Path(steps), steps.size()));
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
	
	public void getGold() {
		System.out.println("Getting the gold now");
		
		CoordPathFinder finder = new CoordPathFinder(map, baseCoordinate);
		List<Coordinate> backToBase = finder.compute(client.getCurrentPosition());
		
		List<Direction> steps = new ArrayList<Direction>();
		
		for(Direction s : steps){
			System.out.print(s);
		}
		
		for (int i = 0; i < backToBase.size()-1; i++){
			Direction step = Direction.parse(backToBase.get(i+1).sub(backToBase.get(i)));
			if (step == null) continue;
			else steps.add(step);
		}
		if (steps.size() >= 1){
			followPath(new Path(steps));
		}
		for(Goal goal : goals){
			followPath(goal.path);
		}
	}

	private void followPath(Path path) {
		while(path.hasNext()){
			client.move(path.next());
		}
		while(path.hasPrevious()){
			client.move(path.previous());
		}
	}
}
