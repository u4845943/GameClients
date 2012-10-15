package com.golddigger.model;

import java.util.ArrayList;
import java.util.List;

public class Path {
	private List<Direction> steps;
	private int i = 0;
	
	public Path(List<Direction> steps){
		this.steps = steps;
	}
	
	public Direction next() throws IndexOutOfBoundsException {
		Direction next = steps.get(i);
		if (next != null) i++;
		return next;
	}
	
	public Direction previous() throws IndexOutOfBoundsException {
		Direction next = steps.get(i-1);
		if (next != null) i--;
		return next;
	}
	
	public boolean hasNext(){
		return i < steps.size();
	}
	
	public boolean hasPrevious(){
		return i > 0;
	}
	
	public Direction[] forward(){
		return steps.toArray(new Direction[]{});
	}
	public Direction[] reverse(){
		int size = steps.size();
		Direction[] array = new Direction[size];
		for (int i = 0; i < size; i++) array[i] = steps.get(size-i-1);
		return array;
	}
	
	@Override
	public String toString(){
		String res = "";
		for (Direction d : forward()) res += d;
		return res;
	}
	
	public static void main(String[] args){
		ArrayList<Direction> steps = new ArrayList<Direction>();
		steps.add(Direction.EAST);
		steps.add(Direction.WEST);
		steps.add(Direction.NORTH);
		Path path = new Path(steps);
		
		while (path.hasNext()) System.out.println(path.next());
		while (path.hasPrevious()) System.out.println(path.previous());
	}
}
