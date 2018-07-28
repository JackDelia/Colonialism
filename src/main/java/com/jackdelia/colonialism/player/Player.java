package com.jackdelia.colonialism.player;

import com.jackdelia.colonialism.currency.Funding;
import com.jackdelia.colonialism.empire.Empire;
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

    private static final int STARTING_CASH = 1000;

    private String name;
    protected Map map;
    private int influence = 50;
    private ArrayList<Player> vassals = new ArrayList<>();
    private ArrayList<Explorer> explorers = new ArrayList<>();
    private City capitol;
    private boolean[][] visible = new boolean[Map.MAP_SIZE][Map.MAP_SIZE];
    private Point position = new Point(Map.MAP_SIZE - 1, 0);
    private City location = null;

    private Empire empire;
    private Funding money;

    public Player(String name, Map map) {
        this.name = name;
        this.map = map;
        this.money = new Funding(STARTING_CASH);
        this.empire = new Empire();

        this.explorers.add(new Explorer(this.position));

        if(name.equals("�_�l")) {
            this.money = new Funding(9999999);

            for(int i = 0; i < this.visible.length; i++) {
                for(int j = 0; j < this.visible.length; j++) {
                    this.visible[i][j] = true;
                }
            }
        }

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(i + j < 8) {
                    this.visible[Map.MAP_SIZE -1-i][j] = true;
                }
            }
        }
    }

    public boolean canExplore() {
        for(Explorer curExplorer : this.explorers) {
            if(!curExplorer.isExploring()) {
                return true;
            }
        }
        return false;
    }

    public void explore(Point target) {
        for(Explorer e: this.explorers) {
            if(!e.isExploring()) {
                e.setTarget(target);
                return;
            }
        }
    }

    private void gainExploreKnowledge(HashSet<Point> knowledge) {
        for(Point p: knowledge) {
            this.visible[p.x][p.y] = true;
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
            this.empire.addCity(newCity);
            System.out.println("City " + name + " founded.");

            if(this.empire.size() == 1) {
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

        this.empire.updateDays(days);

        for(Explorer curExplorer : this.explorers) {

            if(curExplorer.isExploring()) {
                this.money.removeCash(curExplorer.getFunding());
            }

            if(curExplorer.update()) {
                gainExploreKnowledge(curExplorer.getKnowledge());
            }

        }

        if(this.explorers.size() > this.empire.size() && this.explorers.size() > 1) {
            this.money.removeCash(this.explorers.size());
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
        return this.money.getCash();
    }

    public void incrementMoney(double money) {
        this.money.addCash((int) money);
    }

    public ArrayList<City> getCities() {
        return this.empire.getCities();
    }

    public ArrayList<Explorer> getExplorers() {
        return this.explorers;
    }

    public void fireExplorer(Explorer e){
        this.explorers.remove(e);
    }

    public void addExplorer(Explorer e){
        this.explorers.add(e);
    }

    public void addInfluence(int i){
        this.influence += i;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
        for(Explorer e: this.explorers){
            e.setOrigin(position);
            if(!e.isExploring()){
                e.setLocation(position);
            }
        }
    }

    public City getLocation() {
        return this.location;
    }

    public void setLocation(City location) {
        this.location = location;
        if(location != null)
            setPosition(location.getPosition().getLocation());
    }

    public String toString(){
        StringBuilder constructedMessage = new StringBuilder("name: " + this.name + "\nMoney: " + this.money + "\nlocation: ");

        if(this.location != null) {
            constructedMessage.append(this.location.getName());
        } else {
            constructedMessage.append(this.position);
        }

        constructedMessage.append(this.empire.toString());

        return constructedMessage + "\n";
    }

    public boolean canSee(int i, int j) {
        return this.visible[i][j];
    }

    public boolean canSee(Point p){
        return this.visible[p.x][p.y];
    }

    public City findCityByName(String name) {
        return this.empire.findCityByName(name);
    }

    private int numExplored(){
        int count = 0;
        for(boolean[] ba: this.visible) {
            for(boolean b: ba) {
                if(b)
                    count++;
            }
        }
        return count;

    }
}
