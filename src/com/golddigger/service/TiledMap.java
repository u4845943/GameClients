package com.golddigger.service;

import com.golddigger.model.Coordinate;

public interface TiledMap {
	public void set(Coordinate location, String view);
	public void set(Coordinate location, char tile);
	public char get(Coordinate location);
	public boolean inBounds(Coordinate location);
	public char[][] toArray();
}
