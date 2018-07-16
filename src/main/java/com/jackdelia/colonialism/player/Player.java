package com.jackdelia.colonialism.player;

import com.jackdelia.colonialism.explorer.Explorer;
import com.jackdelia.colonialism.city.City;
import com.jackdelia.colonialism.map.Map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 */
public class Player {

    private String name;
    protected Map map;
    private double money = 1000;
    private int influence = 50;
    private ArrayList<Player> vassals = new ArrayList<Player>();
    private ArrayList<City> cities = new ArrayList<City>();
    private ArrayList<Explorer> explorers = new ArrayList<Explorer>();
    private City capitol;
    private boolean[][] visible = new boolean[Map.MAP_SIZE][Map.MAP_SIZE];
    private Point position = new Point(Map.MAP_SIZE -1, 0);
    private City location = null;

    public Player(String name, Map map) {
        this.name = name;
        this.map = map;
        explorers.add(new Explorer(position));

        if(name.equals("�_�l")) {
            money = 9999999;
            for(int i = 0; i < visible.length; i++)
                for(int j = 0; j < visible.length; j++)
                    visible[i][j] = true;
        }

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(i + j < 8) {
                    visible[Map.MAP_SIZE -1-i][j] = true;
                }
            }
        }
    }

    public boolean canExplore() {
        for(Explorer curExplorer : explorers) {
            if(!curExplorer.isExploring()) {
                return true;
            }
        }
        return false;
    }

    public void explore(Point target) {
        for(Explorer e: explorers) {
            if(!e.isExploring()) {
                e.setTarget(target);
                return;
            }
        }
    }

    private void gainExploreKnowledge(HashSet<Point> knowledge) {
        for(Point p: knowledge) {
            visible[p.x][p.y] = true;
        }
    }

    /**
     * @param name the name of the new City
     * @param latitude the latitude portion of the coordinate
     * @param longitude the longitude portion of the coordinate
     * @return the new City object
     */
    public City foundCity(String name, int latitude, int longitude) {
        if(this.map.valid(latitude, longitude) && this.visible[latitude][longitude]) {
            City newCity = new City(name, latitude, longitude, this, this.map);
            this.cities.add(newCity);
            System.out.println("City " + name + " founded.");

            if(this.cities.size() == 1) {
                this.position = new Point(latitude, longitude);
                this.capitol = newCity;
                this.location = newCity;
                for(Explorer curExplorer : this.explorers) {
                    curExplorer .setOrigin(this.position);
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

        for(City c : cities){
            c.update(days);
        }

        for(Explorer curExplorer : this.explorers) {

            if(curExplorer.isExploring()) {
                this.money -= curExplorer.getFunding();
            }

            if(curExplorer.update()) {
                gainExploreKnowledge(curExplorer.getKnowledge());
            }

        }

        if(this.explorers.size() > this.cities.size() && this.explorers.size() > 1) {
            this.money -= explorers.size();
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
        influence += i;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
        for(Explorer e: explorers){
            e.setOrigin(position);
            if(!e.isExploring()){
                e.setLocation(position);
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

    public String toString(){
        StringBuilder constructedMessage = new StringBuilder("name: " + name + "\nMoney: " + money + "\nlocation: ");

        if(location != null) {
            constructedMessage.append(location.getName());
        } else {
            constructedMessage.append(position);
        }

        constructedMessage.append("\nCities:\n");

        for(City curCity : cities) {
            constructedMessage.append(curCity.toString()).append("\n");
        }

        return constructedMessage + "\n";
    }

    public boolean canSee(int i, int j) {
        return visible[i][j];
    }

    public boolean canSee(Point p){
        return visible[p.x][p.y];
    }

    public City findCityByName(String name) {
        for(City curCity : cities) {
            if(curCity.getName().equals(name)) {
                return curCity;
            }
        }

        return null;
    }

    private int numExplored(){
        int count = 0;
        for(boolean[] ba: visible) {
            for(boolean b: ba) {
                if(b)
                    count++;
            }
        }
        return count;

    }
}
