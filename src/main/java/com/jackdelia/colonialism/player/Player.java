package com.jackdelia.colonialism.player;

import com.jackdelia.colonialism.explorer.Explorer;
import com.jackdelia.colonialism.city.City;
import com.jackdelia.colonialism.map.Map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

public class Player {

    private String name;
    protected Map map;
    private double money = 1000;
    private ArrayList<City> cities;
    private ArrayList<Explorer> explorers;
    private boolean[][] visible;
    private Point position;
    private City location;

    public Player(String name, Map gameMap) {
        this.name = name;
        this.map = gameMap;
        this.explorers = new ArrayList<>();
        this.cities = new ArrayList<>();
        this.location = null;
        this.position = new Point(Map.MAP_SIZE - 1, 0);
        this.explorers.add(new Explorer(position));
        this.visible = new boolean[Map.MAP_SIZE][Map.MAP_SIZE];

        if(name.equals("�_�l")) {
            this.money = 9999999;
            for(int i = 0; i < this.visible.length; i++) {
                for(int j = 0; j < this.visible.length; j++) {
                    this.visible[i][j] = true;
                }
            }
        }

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j< 8; j++) {
                if(i+j < 8) {
                    this.visible[Map.MAP_SIZE - 1 - i][j] = true;
                }
            }
        }
    }

    public boolean canExplore() {
        for(Explorer curExplorer: explorers) {
            if(!curExplorer.isExploring()) {
                return true;
            }
        }
        return false;
    }

    public void explore(Point target) {
        for(Explorer curExplorer: explorers){
            if(!curExplorer.isExploring()){
                curExplorer.setTarget(target);
                return;
            }
        }
    }

    private void gainExploreKnowledge(HashSet<Point> knowledge) {
        for(Point curPoint: knowledge) {
            visible[curPoint.x][curPoint.y] = true;
        }
    }

    public City foundCity(String name, int latitude, int longitude) {
        if(this.map.valid(latitude, longitude) && visible[latitude][longitude]) {
            City newCity = new City(name, latitude, longitude, this, this.map);
            this.cities.add(newCity);
            System.out.println("City " + name + " founded.");
            if(this.cities.size() == 1) {
                this.position = new Point(latitude, longitude);
                this.location = newCity;
                for(Explorer curExplorer: explorers) {
                    curExplorer.setOrigin(this.position);
                }
            }
            return newCity;
        }
        else {
            System.out.println("Not a Valid location.");
        }
        return null;
    }

    public void update(int days) {
        for(City curCity : this.cities) {
            curCity.update(days);
        }

        for(Explorer curExplorer : this.explorers) {
            if(curExplorer.isExploring()) {
                this.money = this.money - curExplorer.getFunding();
            }
            if(curExplorer.update()) {
                gainExploreKnowledge(curExplorer.getKnowledge());
            }
        }

        if(this.explorers.size() > this.cities.size() && this.explorers.size() > 1) {
            this.money = this.money - this.explorers.size();
        }

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public double getMoney() {
        return money;
    }

    public void incrementMoney(double money) {
        this.money += money;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void addCity(City c){
        cities.add(c);
    }

    public ArrayList<Explorer> getExplorers() {
        return explorers;
    }

    public void fireExplorer(Explorer e){
        explorers.remove(e);
    }

    public void addExplorer(Explorer e){
        explorers.add(e);
    }

    public void addInfluence(int i){
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
        for(Explorer curExplorer: this.explorers) {
            curExplorer.setOrigin(position);
            if(!curExplorer.isExploring()){
                curExplorer.setLocation(position);
            }
        }
    }

    public City getLocation() {
        return location;
    }

    public void setLocation(City location) {
        this.location = location;
        if(location != null)
            setPosition(location.getPosition().getLocation());
    }

    public String toString() {
        StringBuilder ret = new StringBuilder("name: " + name + "\nMoney: " + money + "\nlocation: ");
        if(location != null)
            ret.append(location.getName());
        else
            ret.append(position);
        ret.append("\nCities:\n");
        for(City c : cities){
            ret.append(c.toString()).append("\n");
        }
        return ret + "\n";
    }

    public boolean canSee(int i, int j) {
        return visible[i][j];
    }

    public boolean canSee(Point p){
        return visible[p.x][p.y];
    }

    public City findCityByName(String name) {
        for(City c : cities) {
            if(c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    private int numExplored() {
        int count = 0;
        for(boolean[] ba: visible) {
            for(boolean b: ba) {
                if(b) {
                    count++;
                }
            }
        }
        return count;
    }

}
