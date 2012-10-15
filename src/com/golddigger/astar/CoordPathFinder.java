package com.golddigger.astar;

import java.util.LinkedList;
import java.util.List;

import com.golddigger.model.Coordinate;
import com.golddigger.model.Direction;
import com.golddigger.service.ArrayTiledMap;
import com.golddigger.service.TiledMap;

public class CoordPathFinder extends AStar<Coordinate> {
	private TiledMap map;
	private Coordinate goal;

	public CoordPathFinder(TiledMap map, Coordinate goal){
		this.map = map;
		this.goal = goal;
	}

	@Override
	protected boolean isGoal(Coordinate node) {
		return node.equals(goal);
	}

	@Override
	protected Double g(Coordinate from, Coordinate to) {
		if (from.equals(to)) return 0.0;
		if (map.get(to) != 'w' || map.get(to) != 0) return 1.0;
		return Double.MAX_VALUE;
	}

	@Override
	protected Double h(Coordinate from, Coordinate to) {
		Double x = (double) Math.abs(from.lat - to.lat);
		Double y = (double) Math.abs(from.lng - to.lng);
		return new Double((x + y) * 1.01);
	}

	@Override
	protected List<Coordinate> generateSuccessors(Coordinate node) {
		List<Coordinate> ret = new LinkedList<Coordinate>();
		for (Direction d : Direction.all()){
			Coordinate c = d.offset(node);
			if (isTreadable(map.get(c))){
				ret.add(c);
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		String field =  "wwwwwww\n"+
						"wb....w\n"+
						"wwwww.w\n"+
						"w.....w\n"+
						"wwwwwww";
		
		ArrayTiledMap map = new ArrayTiledMap(64, 64, new Coordinate(32,32));
		map.add(new Coordinate(0,0), field);
		System.out.println(map);

		Coordinate start = new Coordinate(1, 1);
		Coordinate goal = new Coordinate(3, 1);

		AStar<Coordinate> pf = new CoordPathFinder(map, goal);
		List<Coordinate> nodes = pf.compute(start);
		if (nodes == null){
			System.out.println("No Path Found");
		} else {
			System.out.print("Path :");
			for (Coordinate coord : nodes){
				System.out.print(coord+" -> ");
			}
			System.out.print("Next");
		}
	}

	
	private boolean isTreadable(char tile){
		if (tile == 0) return false;
		if (tile == 'w') return false;
		return true;
	}
}
