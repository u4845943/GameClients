package com.golddigger.tools;

import java.util.ArrayList;

import com.golddigger.model.Coordinate;
import com.golddigger.model.Direction;
import com.golddigger.service.ServerService;
import com.golddigger.service.TiledMap;

public class Explorer {
	private ArrayList<Coordinate> visited;
	private TiledMap map;
	private ServerService client;
	
	public static void explore(TiledMap map, ServerService client){
		new Explorer(map, client).explore(client.getCurrentPosition());
	}
	
	private Explorer(TiledMap map, ServerService client){
		this.visited = new ArrayList<Coordinate>();
		this.map = map;
		this.client = client;
	}
	
	public void explore(Coordinate coord){
		map.set(coord, client.view());
		
		Coordinate next;
		for (Direction direction : Direction.all()){
			next = direction.offset(coord);
			if (visited.contains(next)) continue;
			if (client.move(direction).contains("FAILED")) continue;
			visited.add(next);
			System.out.println(map.toString() + "\n\n\n\n\n");
			explore(next);
			client.move(direction.reverse());
		}
		
	}
}
