package com.jackdelia.colonialism.explorer;

import java.awt.*;
import java.util.ArrayList;

/**
 * <p>Information about a Collection of Explorers</p>
 * <p>
 * Change Log:
 * </p>
 * <ul>
 *     <li>v0.5 - Initial File Creation. Extracted logic from {@link com.jackdelia.colonialism.player.Player}.</li>
 * </ul>
 *
 * @author <a href="mailto:andrewparise1994@gmail.com">Andrew Parise</a>
 * @since 0.5
 * @version 0.5
 */
public class Fleet {

    private ArrayList<Explorer> explorers;

    /**
     * Default Constructor
     */
    public Fleet(){
        this.explorers = new ArrayList<>();
    }

    /**
     * Adds a new Explorer to the Fleet
     *
     * @param newExplorer the new founded Explorer
     */
    public void addExplorer(Explorer newExplorer) {
        this.explorers.add(newExplorer);
    }

    /**
     * Checks to See if there are any Idle Explorers
     *
     * @return true if there is a free explorer, false if not
     */
    public boolean hasIdleExplorer(){
        for(Explorer curExplorer : this.explorers) {
            if(!curExplorer.isExploring()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets a new Target for the Fleet to Explore
     *
     * @param target Point where the Fleet will explore
     */
    public void explore(Point target) {
        for(Explorer e: this.explorers) {
            if(!e.isExploring()) {
                e.setTarget(target);
                return;
            }
        }
    }

    /**
     * Updates the Fleet's Home City to the param location
     *
     * @param location Point where the Fleet will be based out of
     */
    public void setHomeCity(Point location) {
        for(Explorer curExplorer : this.explorers) {
            curExplorer.setOrigin(location);
        }
    }

    /**
     * Moves all idle explorers in the fleet to the target location
     *
     * @param location where the idle explorers will be moved to
     */
    public void setIdleExplorersLocation(Point location){
        setHomeCity(location);
        for(Explorer curExplorer: this.explorers){
            if(!curExplorer.isExploring()){
                curExplorer.setLocation(location);
            }
        }
    }

    /**
     * Fetches the Explorers in the Fleet
     *
     * @return the explorers in the Fleet
     */
    public ArrayList<Explorer> getExplorers(){
        return this.explorers;
    }

    /**
     * Gets the Total Expenditure of the Fleet in one Day
     *
     * @return int the Total Expenditure for the Fleet
     */
    public int getExpenditure(){
        int expenditure = 0;

        for(Explorer curExplorer : this.explorers) {
            if(curExplorer.isExploring()) {
                expenditure += curExplorer.getFunding();
            }
        }

        return expenditure;
    }

    /**
     * Fetch the Number of Explorers in this Fleet
     *
     * @return int the number of explorers in this Fleet
     */
    public int size() {
        return this.explorers.size();
    }

}
