package com.jackdelia.colonialism.player;

import com.jackdelia.colonialism.city.City;
import com.jackdelia.colonialism.currency.Funding;
import com.jackdelia.colonialism.empire.Empire;
import com.jackdelia.colonialism.explorer.Explorer;
import com.jackdelia.colonialism.explorer.Fleet;
import com.jackdelia.colonialism.map.Map;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.IntStream;

public abstract class BasePlayer {

    private static final int STARTING_CASH = 1000;

    private String name;
    protected Map map;

    private Influence influence;

    private Vassal vassal;
    private City capitol;
    private boolean[][] visible;
    private Point position;
    private City location;

    private Fleet fleet;

    private Empire empire;
    private Funding money;




    public BasePlayer(String name, Map map) {

        setName(name);
        setMap(map);
        setMoney(new Funding(STARTING_CASH));
        setEmpire(new Empire());
        setVassal(new Vassal());
        setInfluence(new Influence());

        // construct the player's location
        Point playerPoint = new Point(Map.MAP_SIZE - 1, 0);
        setPosition(playerPoint);

        // construct the player's fleet
        Fleet playerFleet = new Fleet();
        playerFleet.setIdleExplorersLocation(playerPoint);

        setFleet(playerFleet);
        playerFleet.addExplorer(Explorer.create(playerPoint));

        // construct player visibility
        boolean[][] playerVisibility = new boolean[Map.MAP_SIZE][Map.MAP_SIZE];
        IntStream.range(0, 8).forEach((int i) -> {
            if (i < 8) {
                playerVisibility[Map.MAP_SIZE - 1 - i][0] = true;
            }
            if (i + 1 < 8) {
                playerVisibility[Map.MAP_SIZE - 1 - i][1] = true;
            }
            if (i + 2 < 8) {
                playerVisibility[Map.MAP_SIZE - 1 - i][2] = true;
            }
            if (i + 3 < 8) {
                playerVisibility[Map.MAP_SIZE - 1 - i][3] = true;
            }
            if (i + 4 < 8) {
                playerVisibility[Map.MAP_SIZE - 1 - i][4] = true;
            }
            if (i + 5 < 8) {
                playerVisibility[Map.MAP_SIZE - 1 - i][5] = true;
            }
            if (i + 6 < 8) {
                playerVisibility[Map.MAP_SIZE - 1 - i][6] = true;
            }
            if (i + 7 < 8) {
                playerVisibility[Map.MAP_SIZE - 1 - i][7] = true;
            }
        });
        setVisible(playerVisibility);

    }

    private int numExplored(){
        int count = 0;
        for(boolean[] ba: this.visible) {
            for(boolean b: ba) {
                if(b) {
                    count++;
                }
            }
        }
        return count;
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

    public void update(int days) {

        this.empire.updateDays(days);

        int fleetExpenditure = this.fleet.getExpenditure();

        this.money.removeCash(fleetExpenditure);

        this.fleet.getExplorers().stream()
                .filter(Explorer::update)
                .map(Explorer::getKnowledge)
                .forEach(this::gainExploreKnowledge);

        if(this.fleet.size() > this.empire.size() && this.fleet.size() > 1) {
            this.money.removeCash(this.fleet.size());
        }

    }

    public City findCityByName(String name) {
        return this.empire.findCityByName(name);
    }

    public double getMoney() {
        return this.money.getCash();
    }

    public void incrementMoney(double money) {
        this.money.addCash((int) money);
    }

    public void decrementMoney(double money) {
        this.money.removeCash((int) money);
    }

    public void addInfluence(int i){
        this.influence.addInfluence(i);
    }


    /**
     * @param name the name of the new City
     * @param latitude the latitude portion of the coordinate
     * @param longitude the longitude portion of the coordinate
     * @return the new City object
     */
    public City foundCity(String name, int latitude, int longitude) {
        if(this.map.valid(latitude, longitude) && this.visible[latitude][longitude]) {
            City newCity = City.create(name, latitude, longitude, this, this.map);
            this.empire.addCity(newCity);
            System.out.println("City " + name + " founded.");

            if(this.empire.size() == 1) {
                this.position = new Point(latitude, longitude);
                this.capitol = newCity;
                this.location = newCity;
                this.fleet.setHomeCity(this.position);
            }

            return newCity;
        }
        else {
            System.out.println("Not a Valid location.");
        }
        return null;
    }

    public ArrayList<City> getCities() {
        return this.empire.getCities();
    }


    public boolean canExplore() {
        return this.fleet.hasIdleExplorer();
    }

    public void explore(Point target) {
        this.fleet.explore(target);
    }

    private void gainExploreKnowledge(HashSet<Point> knowledge) {
        knowledge.forEach(p -> this.visible[p.x][p.y] = true);
    }

    public boolean canSee(int i, int j) {
        return this.visible[i][j];
    }

    public boolean canSee(Point p){
        return this.visible[p.x][p.y];
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

    public Influence getInfluence() {
        return influence;
    }

    public void setInfluence(Influence influence) {
        this.influence = influence;
    }

    public Vassal getVassal() {
        return vassal;
    }

    public void setVassal(Vassal vassal) {
        this.vassal = vassal;
    }

    public City getCapitol() {
        return capitol;
    }

    public void setCapitol(City capitol) {
        this.capitol = capitol;
    }

    public boolean[][] getVisible() {
        return visible;
    }

    public void setVisible(boolean[][] visible) {
        this.visible = visible;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void addExplorer(Explorer newExplorer){
        this.fleet.addExplorer(newExplorer);
    }

    public ArrayList<Explorer> getExplorers() {
        return this.fleet.getExplorers();
    }


    public City getLocation() {
        return location;
    }

    public void setLocation(City location) {
        this.location = location;
    }

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }

    public Empire getEmpire() {
        return empire;
    }

    public void setEmpire(Empire empire) {
        this.empire = empire;
    }

    public void setMoney(Funding money) {
        this.money = money;
    }
}
