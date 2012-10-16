package com.golddigger.service;

import com.golddigger.model.Coordinate;
import com.golddigger.model.Direction;

public interface ServerService {
	public Coordinate getCurrentPosition();
	public String move(Direction direction);
	public String drop();
	public String grab();
	public String view();
	public String next();
}
