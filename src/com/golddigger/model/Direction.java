package com.golddigger.model;

public enum Direction {
	NORTH, SOUTH, EAST, WEST;

	public static Direction[] all(){
		return new Direction[]{NORTH, SOUTH, EAST, WEST};
	}
	
	public Coordinate offset(Coordinate coord) {
		Coordinate offset;
		switch(this){
		case NORTH: offset = Coordinate.NORTH; break;
		case SOUTH: offset = Coordinate.SOUTH; break;
		case EAST: offset = Coordinate.EAST; break;
		case WEST: offset = Coordinate.WEST; break;
		default: return coord;
		}
		return coord.add(offset);
	}
	
	public Direction reverse(){
		switch(this){
		case NORTH: return SOUTH;
		case SOUTH: return NORTH;
		case EAST: return WEST;
		case WEST: return EAST;
		default: return null;
		}
	}

	public static Direction parse(Coordinate coord) {
		if (coord.equals(Coordinate.EAST)) return Direction.EAST;
		if (coord.equals(Coordinate.WEST)) return Direction.WEST;
		if (coord.equals(Coordinate.SOUTH)) return Direction.SOUTH;
		if (coord.equals(Coordinate.NORTH)) return Direction.NORTH;
		return null;
	}
}
