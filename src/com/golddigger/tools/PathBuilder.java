package com.golddigger.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.golddigger.astar.AStar;
import com.golddigger.astar.CoordPathFinder;
import com.golddigger.model.Coordinate;
import com.golddigger.model.Direction;
import com.golddigger.model.Path;
import com.golddigger.service.ArrayTiledMap;
import com.golddigger.service.TiledMap;

public class PathBuilder extends Thread {
	public static final Coordinate ORIGIN = new Coordinate(0, 0);
	private Queue<Path> pathQueue;
	private Coordinate start;
	private CoordPathFinder finder;

	public static void fromOrigin(Coordinate target, Queue<Path> paths, TiledMap map){
		CoordPathFinder finder = new CoordPathFinder(map, target);
		PathBuilder builder = new PathBuilder(ORIGIN, finder, paths);
		builder.run();
	}

	private PathBuilder(Coordinate start, CoordPathFinder finder, Queue<Path> pathsQueue) {
		this.start = start;
		this.finder = finder;
		this.pathQueue = pathsQueue;
	}
	
	public void run(){
		List<Coordinate> nodes = finder.compute(start);
		List<Direction> steps = new ArrayList<Direction>();
		for (int i = 0; i < nodes.size()-1; i++){
			Direction step = Direction.parse(nodes.get(i+1).sub(nodes.get(i)));
			if (step == null) return;
			else steps.add(step);
		}
		if (steps.size() < 1) return;
		else pathQueue.add(new Path(steps));
		
	}
	
	public static void main(String[] args){
		Coordinate start = new Coordinate(1,1);
		Coordinate goal = new Coordinate(3,1);
		String field =  "wwwww\n"+
						"wb..w\n"+
						"www.w\n"+
						"w9..w\n"+
						"wwwww\n";
		
//		TiledMap map = new ArrayTiledMap(64, 64, new Coordinate(32,32));
//		map.set(new Coordinate(-2,-2), field);
		
		ArrayTiledMap map = new ArrayTiledMap(64, 64, new Coordinate(32,32));
		map.add(new Coordinate(0,0), field);
		
		
		CoordPathFinder finder = new CoordPathFinder(map, goal);

		List<Coordinate> nodes = finder.compute(start);
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
}
