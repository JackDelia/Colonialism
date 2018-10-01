package com.jackdelia.colonialism.map;

import com.jackdelia.colonialism.city.City;
import com.jackdelia.colonialism.empire.Empire;
import com.jackdelia.colonialism.location.Location;
import com.jackdelia.colonialism.map.resource.Resource;
import com.jackdelia.colonialism.map.resource.ResourceCollection;
import com.jackdelia.colonialism.map.resource.ResourceFactory;
import com.jackdelia.colonialism.map.resource.stocking.ResourceStockingStrategyFactory;
import com.jackdelia.colonialism.map.terrain.Terrain;
import com.jackdelia.colonialism.math.RandomBooleanGenerator;
import com.jackdelia.colonialism.math.RandomNumberGenerator;
import com.jackdelia.colonialism.player.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 *
 */
public class Map extends JPanel{

    private Terrain[][] mapTerrain;
    private Resource[][][] mapResources;

    public static final int MAP_SIZE = 100;
    public static final int PIXEL_STEP = 6;
    private ResourceCollection naturalResources;
    public Player player;

    private MapAge mapAge;
    private Empire empire;

    private static final int DEFAULT_MAP_WIDTH = 500;
    private static final int DEFAULT_MAP_HEIGHT = 500;

    /**
     * Default Constructor
     */
    public Map() {
        // initialize class variables
        this.setSize(DEFAULT_MAP_WIDTH,DEFAULT_MAP_HEIGHT);
        this.mapTerrain = new Terrain[MAP_SIZE][MAP_SIZE];
        this.mapResources = new Resource[MAP_SIZE][MAP_SIZE][];

        this.empire = new Empire();
        this.mapAge = new MapAge();
        this.naturalResources = ResourceFactory.getInstance().getNaturalResources();

        //procedural generation possible later
        //or maybe just a set map
        formContinents();
        formTerrain();
        stockResources();
    }

    /**
     * Populates the map with Resources
     */
    private void stockResources(){
        final ResourceStockingStrategyFactory resourceStockingStrategyFactory = ResourceStockingStrategyFactory.init();

        for(int i = 0; i < this.mapResources.length; i++) {

            for(int j = 0; j < this.mapResources[i].length; j++) {

                Location location = new Location(new Point(i, j));

                if((getRandomDouble() > (7/8.0)) && (this.mapTerrain[i][j] != Terrain.MOUNTAINS)) {
                    resourceStockingStrategyFactory.executeResourceStockingStrategy("THREE", this.mapResources, this.naturalResources, location);

                } else if(getRandomDouble() > (1 / 2.0)) {
                    resourceStockingStrategyFactory.executeResourceStockingStrategy("TWO", this.mapResources, this.naturalResources, location);

                } else if(getRandomDouble() > (1 / 10.0)) {
                    resourceStockingStrategyFactory.executeResourceStockingStrategy("ONE", this.mapResources, this.naturalResources, location);

                } else {
                    if(this.mapTerrain[i][j] == Terrain.MOUNTAINS) {
                        resourceStockingStrategyFactory.executeResourceStockingStrategy("MOUNTAINS", this.mapResources, this.naturalResources, location);

                    } else {
                        resourceStockingStrategyFactory.executeResourceStockingStrategy("NULL", this.mapResources, this.naturalResources, location);

                    }
                }
            }
        }

    }

    public void paintComponent(Graphics graphics){
        boolean isInNormalAge = this.mapAge.isInNormalAge();

        for(int i = 0; i < MAP_SIZE; i++){
            for(int j = 0; j< MAP_SIZE; j++){
                graphics.setColor(this.mapTerrain[i][j].getColor());
                if(this.player != null && !(this.player.canSee(i, j))) {
                    graphics.setColor(Color.WHITE);
                }
                if((this.player != null) && (this.player.getLocation() == null) && (this.player.getPosition().getX() == i) && (this.player.getPosition().getY() == j) && !(isInNormalAge))
                    graphics.setColor(Color.RED);
                graphics.fillRect(i* PIXEL_STEP, j* PIXEL_STEP, PIXEL_STEP, PIXEL_STEP);
            }

            for(City curCity : this.empire.getCities()){
                if(!(this.player.getLocation() == curCity && isInNormalAge) && curCity.getPlayer() == this.player || this.player.canSee(curCity.getPosition())){
                    graphics.setColor(new Color(181,80,137));
                    if(this.player.getLocation() == curCity)
                        graphics.setColor(Color.red);
                    int ovalSize = 7 + curCity.getSize()/2000;
                    graphics.fillOval(curCity.getPosition().x*6 - ovalSize/2, curCity.getPosition().y * 6-ovalSize/2, ovalSize , ovalSize);
                    graphics.drawString(curCity.getName(), curCity.getPosition().x * 6, curCity.getPosition().y * 6-10);
                }
            }
            this.mapAge.incrementAge();
        }
    }

    private void formMountains(){
        int count = getRandomInt((int) (Math.pow(MAP_SIZE, 2)/1000))+18;
        for(int i = 0; i < count; i++){
            int rangeLength = getRandomInt(MAP_SIZE /3);
            int rangeWidth = getRandomInt(MAP_SIZE /17) + 2;
            boolean northSouth = getRandomBoolean();
            boolean eastWest = getRandomBoolean();

            if(northSouth && eastWest) {
                eastWest = false;
            }

            Point startPoint = new Point(getRandomInt(MAP_SIZE), getRandomInt(MAP_SIZE));
            while(getTerrain(startPoint.x, startPoint.y) != Terrain.PLAINS){
                startPoint = new Point(getRandomInt(MAP_SIZE), getRandomInt(MAP_SIZE));
            }

            formMountainRange(rangeLength, rangeWidth, northSouth, eastWest, startPoint);
        }
    }

    private void setTerrain(int xValue, int yValue, Terrain terrain){
        if((xValue > 0) && (xValue < MAP_SIZE) && (yValue > 0) && (yValue < MAP_SIZE) && (this.mapTerrain[xValue][yValue] != Terrain.OCEAN)) {
            this.mapTerrain[xValue][yValue] = terrain;
        }
    }

    private void formMountainRange(int rangeLength, int rangeWidth,
                                   boolean northSouth, boolean eastWest, Point startPoint) {

        int dx = 0;
        int dy = 0;
        for(int i = 0; i <= rangeLength; i ++) {

            if(northSouth) {
                dy = i;
            } else {
                dy += getRandomInt(3) - 1;
            }

            if(eastWest) {
                dx = i;
            } else {
                dx += getRandomInt(3) - 1;
            }

            int xPosition = startPoint.x + dx;
            int yPosition = startPoint.y + dy;
            setTerrain(xPosition, yPosition, Terrain.MOUNTAINS);
            int effectiveWidth = (rangeWidth / 2)- getRandomInt(3);
            for(int j = 1; j <= effectiveWidth; j++) {
                if(northSouth) {
                    setTerrain(xPosition - j,yPosition, Terrain.MOUNTAINS);
                    setTerrain(xPosition + j,yPosition, Terrain.MOUNTAINS);
                } else {
                    setTerrain(xPosition,yPosition + j, Terrain.MOUNTAINS);
                    setTerrain(xPosition,yPosition - j, Terrain.MOUNTAINS);
                }
            }
        }
    }

    /**
     * Encapsulation of the method call to fetch random doubles
     *
     * @return a randomly generated double
     */
    private double getRandomDouble() {
        return RandomNumberGenerator.generate();
    }

    /**
     * Encapsulation of the method call to fetch random ints
     *
     * @param maxValue the maximum value of the generated int
     * @return a randomly generated int
     */
    private int getRandomInt(int maxValue) {
        return RandomNumberGenerator.generate(maxValue);
    }

    /**
     * Encapsulation of the method call to fetch random booleans
     *
     * @return a randomly generated boolean
     */
    private boolean getRandomBoolean() {
        return RandomBooleanGenerator.generate();
    }


    private void formTerrain(){
        formMountains();
        formDeserts();
        formForrests();
        for(int i = 3; i <= 4; i++){
            Terrain t = Terrain.PLAINS;
            switch(i) {
                case 2: t = Terrain.MOUNTAINS;
                    // this is an unreachable case: intended behavior?
                    break;
                case 3: t = Terrain.DESERT;
                    break;
                case 4: t = Terrain.FORREST;

            }

            int count = getRandomInt((int) (Math.pow(MAP_SIZE, 2)/1000)) + 18;
            while (count > 0) {
                int x = getRandomInt(MAP_SIZE - 2) + 1;
                int y = getRandomInt(MAP_SIZE - 2) + 1;

                if(mapTerrain[x][y] == Terrain.PLAINS){
                    mapTerrain[x][y] = t;
                    int dirCount = 1;

                    while(getRandomDouble() < .75 && dirCount < x){
                        if(mapTerrain[x - dirCount][y] == Terrain.PLAINS) {
                            mapTerrain[x - dirCount][y] = t;
                            if(mapTerrain[x - dirCount][y + 1] == Terrain.PLAINS && getRandomDouble() >= .5) {
                                mapTerrain[x - dirCount][y + 1] = t;
                            }
                            if(mapTerrain[x - dirCount][y + 1] == Terrain.PLAINS && getRandomDouble() >= .5) {
                                mapTerrain[x - dirCount][y - 1] = t;
                            }
                        }
                        dirCount++;
                    }

                    dirCount = 1;
                    while(getRandomDouble() < .75 && dirCount + x< MAP_SIZE){
                        if(mapTerrain[x + dirCount][y] == Terrain.PLAINS){
                            mapTerrain[x + dirCount][y] = t;
                            if(mapTerrain[x + dirCount][y + 1] == Terrain.PLAINS && getRandomDouble() >= .5) {
                                mapTerrain[x + dirCount][y + 1] = t;
                            }
                            if(mapTerrain[x + dirCount][y + 1] == Terrain.PLAINS && getRandomDouble() >= .5) {
                                mapTerrain[x + dirCount][y - 1] = t;
                            }
                        }
                        dirCount++;
                    }

                    dirCount = 1;
                    while(getRandomDouble() < .75 && dirCount < y) {
                        if(mapTerrain[x][y - dirCount] == Terrain.PLAINS) {
                            mapTerrain[x][y - dirCount] = t;
                            if(mapTerrain[x + 1][y - dirCount] == Terrain.PLAINS && getRandomDouble() >= .5) {
                                mapTerrain[x + 1][y - dirCount] = t;
                            }
                            if(mapTerrain[x - 1][y - dirCount] == Terrain.PLAINS && getRandomDouble() >= .5) {
                                mapTerrain[x - 1][y - dirCount] = t;
                            }
                        }
                        dirCount++;
                    }

                    dirCount = 1;
                    while(getRandomDouble() < .75 && dirCount+y < MAP_SIZE) {
                        if(mapTerrain[x][y+dirCount] == Terrain.PLAINS) {
                            mapTerrain[x][y+dirCount] = t;
                            if(mapTerrain[x+1][y+dirCount] == Terrain.PLAINS && getRandomDouble() >= .5) {
                                mapTerrain[x+1][y+dirCount] = t;
                            }
                            if(mapTerrain[x-1][y+dirCount] == Terrain.PLAINS && getRandomDouble() >= .5) {
                                mapTerrain[x-1][y+dirCount] = t;
                            }
                        }
                        dirCount++;
                    }
                    count--;
                }
            }
        }
        for(int w = 0; w< MAP_SIZE; w++) {
            for(int z = 0; z< MAP_SIZE; z++) {
                if(mapTerrain[w][z] == Terrain.PLAINS && getRandomDouble() > .75) {
                    mapTerrain[w][z] = Terrain.HILLS;
                }
            }
        }


    }

    private void formForrests() {


    }

    private void formDeserts() {
        // TODO Auto-generated method stub

    }

    private void formContinents(){
        int rt = getRandomInt(MAP_SIZE - 7);
        int lt = getRandomInt(MAP_SIZE);
        int mass = 0;
        if(rt < lt){
            int temp = rt;
            rt = lt;
            lt = temp;
        }
        for(int i = 0; i< MAP_SIZE; i++){
            for(int j = 0; j< MAP_SIZE; j++){
                if(j<=lt || j>=rt)
                    mapTerrain[j][i] = Terrain.OCEAN;
                else{
                    mapTerrain[j][i] = Terrain.PLAINS;
                    mass++;
                }
            }

            if(rt == lt) {
                if(getRandomDouble() > .85){
                    rt += getRandomInt(7);
                    lt -= getRandomInt(7);
                }
            }
            else {
                rt += getRandomInt(15) - 7;
                lt += getRandomInt(15) - 7;
                if(rt < lt) {
                    rt = lt;
                }
            }

            if(rt >= MAP_SIZE) {
                rt = MAP_SIZE - 1;
            }

            if(lt < 0) {
                lt = 0;
            }

        }
        if(mass < (MAP_SIZE * MAP_SIZE)/2) {
            formContinents();
            return;
        }
    }

    /**
     *

     * @param latitude latitude portion of the coordinate
     * @param longitude longitude portion of the coordinate
     * @return Enum corresponding to the terrain at the position
     */
    public Terrain getTerrain(int latitude, int longitude) {
        if(latitude >= MAP_SIZE || longitude >= MAP_SIZE || latitude < 0 || longitude < 0) {
            return Terrain.INVALID;
        }
        return mapTerrain[latitude][longitude];
    }


    public ArrayList<Resource> getNearbyResources(int latitude, int longitude) {

        ArrayList<Resource> ret = new ArrayList<>();
        if(getRandomDouble() > .5) {
            ret.add(Resource.MEAT);
        }

        for(Resource s: mapResources[latitude][longitude]){
            if(!ret.contains(s)) {
                ret.add(s);
            }
        }

        for(int i = -2; i<=2; i++){
            for(int j = -2; j<=2; j++){
                if(latitude+i > 0 && longitude+j > 0 && latitude+i < Map.MAP_SIZE && longitude+j < Map.MAP_SIZE){

                    if(mapTerrain[latitude+i][longitude+j] == Terrain.FORREST && !ret.contains(Resource.WOOD))
                        ret.add(Resource.WOOD);
                    if(mapTerrain[latitude+1][longitude+1] == Terrain.DESERT && !ret.contains(Resource.COTTON))
                        ret.add(Resource.COTTON);
                }
            }
        }

        if(getRandomDouble() > .5 || ret.size() == 0) {
            ret.add(Resource.GRAIN);
        }

        return ret;
    }

    /**
     * @param xPosition x coordinate value
     * @param yPosition y coordinate value
     * @return boolean representing if the param coordinates were a valid location
     */
    public boolean valid(int xPosition, int yPosition) {
        if(this.mapTerrain[xPosition][yPosition] == Terrain.OCEAN) {
            return false;
        }

        for(City curCity : this.empire.getCities()) {
            if(curCity.getPosition().distance(new Point(xPosition, yPosition)) < 10) {
                return false;
            }
        }

        return true;
    }

    /**
     * Fetch the Player's Empire
     *
     * @return the Player's Empire
     */
    public Empire getEmpire() {
        return this.empire;
    }

}
