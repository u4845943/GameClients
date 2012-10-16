package com.golddigger;

import com.golddigger.model.Coordinate;
import com.golddigger.service.ArrayTiledMap;
import com.golddigger.service.ServerService;
import com.golddigger.service.TiledMap;
import com.golddigger.service.WebControllerClientService;
import com.golddigger.tools.Explorer;
import com.golddigger.tools.GoldCollector;
import com.golddigger.util.WebController;

public class Robot extends Thread {
	private TiledMap map;
	private ServerService server;
	
	String host, name;

	public Robot(String host, String name){
		WebController wc = new WebController(name, host, 0);
		server = new WebControllerClientService(wc);
		this.map = new ArrayTiledMap(64,64,new Coordinate(32,32));
		
		this.host = host;
		this.name = name;
	}

	public void run(){
		for(;;){
			WebController wc = new WebController(name, host, 0);
			server = new WebControllerClientService(wc);
			this.map = new ArrayTiledMap(64,64,new Coordinate(32,32));
		Explorer.explore(map, server);
		System.out.println(map.toString());
		GoldCollector gc = new GoldCollector(map, server);
		gc.setGoals();
		gc.getGold();
		server.next();
		System.out.println("Moving onto next field");
		}
	}

	public static void main(String[] args) {
		Robot robot = new Robot("localhost:8066", "test1");
		robot.start();
	}
}
