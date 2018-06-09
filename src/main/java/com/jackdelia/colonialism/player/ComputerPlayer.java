package com.jackdelia.colonialism.player;

import com.jackdelia.colonialism.city.City;
import com.jackdelia.colonialism.map.Map;
import java.awt.Point;
import java.util.ArrayList;

public class ComputerPlayer extends Player {
	
	private static final ArrayList<String> cityNames;
	private long lastCityTime;

	static {
		cityNames = new ArrayList<>();
		cityNames.add("New York City");
		cityNames.add("Washington");
		cityNames.add("Philadelphia");
		cityNames.add("Toronto");
		cityNames.add("Trenton");
		cityNames.add("San Francisco");
		cityNames.add("New Amsterdam");
		cityNames.add("West India");
		cityNames.add("Plymouth Rock");
		cityNames.add("Cape May");
		cityNames.add("Columbia");
		cityNames.add("Virginia");
	}

	public ComputerPlayer(String name, Map map) {
		super(name, map);
		lastCityTime = 0;
		setPosition(new Point(Map.MAP_SIZE -1, 20));
	}
	
	private ArrayList<Point> possibleLocations() {
		ArrayList<Point> possible = new ArrayList<>();

		for(int i = 0; i < Map.MAP_SIZE; i++) {
			for(int j = 0; j < Map.MAP_SIZE; j++) {
				if(canSee(i,j) && map.valid(i, j)) {
					possible.add(new Point(i,j));
				}
			}
		}
		
		return possible;
	}
	
	public void update(int days) {
		super.update(days);

		if(canExplore() && Math.random() > .6) {
			explore(new Point((int)(Math.random()*Map.MAP_SIZE),(int)(Math.random()*Map.MAP_SIZE)));
		}
		
		for(City curCity: getCities()) {
			if(((curCity.getSize() / 90) > curCity.getFunding()) && (getMoney() > (getTotalExpenses() * 1.2))) {
				curCity.incrementFunding(1);
			}
			
			if(getMoney() > (getTotalExpenses() * 4)) {
				curCity.incrementFunding(1);
			}
		}
		
		ArrayList<Point> possible = possibleLocations();
		if((possible.size() > 0) && ((System.currentTimeMillis() - lastCityTime) > 50000)) {
			lastCityTime = System.currentTimeMillis();
			Point loc = new Point(possibleLocations().get((int)(Math.random()*possible.size())));
			foundCity(randomCityName(), loc.x, loc.y);
		}
	}
	
	private double getTotalExpenses() {
		double total = 0;
		for(City curCity: getCities()){
            total = total + curCity.getFunding();
		}
		return total;
	}
	
	private String randomCityName(){
		return cityNames.get((int)(Math.random()* cityNames.size()));
	}

}
