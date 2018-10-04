package com.jackdelia.colonialism.player;

import com.jackdelia.colonialism.city.City;
import com.jackdelia.colonialism.map.Map;
import com.jackdelia.colonialism.math.RandomNumberGenerator;

import java.awt.Point;
import java.util.ArrayList;

public class ComputerPlayer extends BasePlayer {
	
	private static final ArrayList<String> cityNames = new ArrayList<>();

	static{
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

	private long lastCityTime;

	public ComputerPlayer(String name, Map map) {
		super(name, map);
		setPosition(new Point(Map.MAP_SIZE -1, 20));
		lastCityTime = 0;
	}
	
	private ArrayList<Point> possibleLocations(){
		ArrayList<Point> possible = new ArrayList<Point>();

		for(int i = 0; i < Map.MAP_SIZE; i++){
			for(int j = 0; j < Map.MAP_SIZE; j++){
				if(canSee(i,j) && this.map.valid(i, j))
					possible.add(new Point(i,j));
			}
		}
		
		return possible;
	}

	@Override
	public void update(int days) {
		super.update(days);
		if(canExplore() && RandomNumberGenerator.generate() > .6){
			explore(new Point((int)(RandomNumberGenerator.generate() * Map.MAP_SIZE),(int)(RandomNumberGenerator.generate() * Map.MAP_SIZE)));
		}

        getCities().forEach(c -> {
            if (c.getSize() / 90 > c.getFunding() && getMoney() > getTotalExpenses() * 1.2) {
                c.incrementFunding(1);
            }
            if (getMoney() > getTotalExpenses() * 4) {
                c.incrementFunding(1);
            }
        });
		
		ArrayList<Point> possible = possibleLocations();
		if(possible.size() > 0 && System.currentTimeMillis() - this.lastCityTime > 50000) {
			this.lastCityTime = System.currentTimeMillis();
			Point loc = new Point(possibleLocations().get((int)(RandomNumberGenerator.generate() * possible.size())));
			foundCity(randomCityName(), loc.x, loc.y);
		}
	}
	
	private double getTotalExpenses(){
        return getCities().stream()
                .mapToDouble(City::getFunding)
                .sum();
	}
	
	private String randomCityName(){
		return cityNames.get((int)(RandomNumberGenerator.generate() * cityNames.size()));
	}

}
