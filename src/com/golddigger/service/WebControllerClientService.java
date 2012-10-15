package com.golddigger.service;

import java.io.IOException;

import com.golddigger.model.Coordinate;
import com.golddigger.model.Direction;
import com.golddigger.util.WebController;
import com.meterware.httpunit.WebResponse;

public class WebControllerClientService implements ServerService {
	private WebController wc;
	private Coordinate pos = new Coordinate(0,0);

	public WebControllerClientService(WebController controller){
		this.wc = controller;
	}

	@Override
	public Coordinate getCurrentPosition() {
		return pos;
	}

	@Override
	public String move(Direction direction) {
		WebResponse resp = null;
		try {
			switch(direction){
			case NORTH: resp = wc.moveNorth(); break;
			case SOUTH: resp = wc.moveSouth(); break;
			case EAST: resp = wc.moveEast(); break;
			case WEST: resp = wc.moveWest(); break;
			}
			String result = resp.getText();
			if (result.contains("OK")) {
				pos = direction.offset(pos);
			}
			return result;
		} catch (IOException e){
			return null;
		}
	}

	@Override
	public String view(){
		try {
			return wc.view().getText();
		} catch (IOException e){
			return null;
		}
	}
}
