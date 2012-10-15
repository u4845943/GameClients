package com.golddigger.model;

public class Coordinate {
	public static final Coordinate EAST = new Coordinate(0,1);
	public static final Coordinate WEST = new Coordinate(0,-1);
	public static final Coordinate NORTH = new Coordinate(-1,0);
	public static final Coordinate SOUTH = new Coordinate(1,0);
	
	public final int lat, lng;
	
	public Coordinate(int lat, int lng){
		this.lat = lat;
		this.lng = lng;
	}
	
	public Coordinate add(Coordinate other){
		return new Coordinate(lat+other.lat, lng+other.lng);
	}
	
	public Coordinate sub(Coordinate other){
		return new Coordinate(lat-other.lat, lng-other.lng);
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Coordinate){
			return equals((Coordinate) o);
		} else {
			return false;
		}
	}
	
	public boolean equals(Coordinate c){
		return lat == c.lat && lng == c.lng;
	}
	
	@Override
	public String toString(){
		return "("+lat+","+lng+")";
	}
}
