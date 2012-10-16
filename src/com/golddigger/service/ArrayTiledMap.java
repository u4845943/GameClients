package com.golddigger.service;

import com.golddigger.model.Coordinate;

public class ArrayTiledMap implements TiledMap {
	private int slat, slng, flat, flng;

	public int getSlng() {
		return slng;
	}

	public int getSlat() {
		return slat;
	}

	private char[][] tiles;
	private final Coordinate offset;
	
	public ArrayTiledMap(char[][] tiles){
		this.tiles = tiles;
		offset = new Coordinate(0,0);
		
		slat = 0;
		flat = tiles.length;
		slng = 0;
		if (tiles.length != 0) flng = tiles[0].length;
		else flng = 0;
	}
	
	public ArrayTiledMap(int width, int height, Coordinate origin){
		tiles = new char[width][height];
		offset = origin;
		
		slat = origin.lat;
		flat = origin.lat;
		slng = origin.lng;
		flng = origin.lng;
	}
	
	
	public void add(Coordinate location, String view){
		String[] lines = view.trim().split("\n");
		
		for (int i = 0; i < lines.length; i++){
			String line = lines[i].trim();
			for (int j = 0; j < line.length(); j++){
				Coordinate offset = new Coordinate(i,j);
				set(location.add(offset), line.charAt(j));
			}
		}
	}
	
	@Override
	public void set(Coordinate location, String view) {
		String[] lines = view.trim().split("\n");
		int radius = (lines.length-1)/2;
		Coordinate r = new Coordinate(-radius,-radius);
		
		for (int i = 0; i < lines.length; i++){
			String line = lines[i].trim();
			for (int j = 0; j < line.length(); j++){
				Coordinate offset = new Coordinate(i,j).add(r);
				set(location.add(offset), line.charAt(j));
			}
		}
	}

	@Override
	public void set(Coordinate location, char tile) {
		Coordinate local = location.add(offset);
		char c = get(location);
		if (c != tile) {
			if (c == 0 || c == '?') set(local.lat, local.lng, tile);
			else if (tile == '?') return;
			else throw new RuntimeException("your position is stuffed("+c+" != "+tile+")");
		}
	}

	@Override
	public char get(Coordinate location) {
		Coordinate local = location.add(offset);
		return get(local.lat, local.lng);
	}
	
	public char get(int lat, int lng){
		try {
			return tiles[lat][lng];
		} catch (ArrayIndexOutOfBoundsException e){
			return 0;
		}
	}
	
	private void set(int lat, int lng, char tile){
		if (lat < slat) slat = lat;
		if (lat+1 > flat) flat = lat+1;
		if (lng < slng) slng = lng;
		if (lng+1 > flng) flng = lng+1;
		
		tiles[lat][lng] = tile;
	}
	
	@Override
	public String toString(){
		String res = "";
		for (char[] row : this.toArray()){
			for (char tile : row){
				if (tile == 0) res += "-";
				else res += tile;
			}
			res+= "\n";
		}
		return res.trim();
	}

	@Override
	public char[][] toArray() {
		final int height = flat-slat;
		final int width  = flng-slng;
		char[][] array = new char[height][width];
		for (int lat = 0; lat < height; lat++){
			for (int lng = 0; lng < width; lng++){
				array[lat][lng] = get(lat+slat, lng+slng);
			}
		}
		return array;
	}

	@Override
	public boolean inBounds(Coordinate location) {
		Coordinate c = location.add(offset);
		return (c.lat > slat && c.lng > slng && c.lat < flat && c.lng < flng);
	}
}
