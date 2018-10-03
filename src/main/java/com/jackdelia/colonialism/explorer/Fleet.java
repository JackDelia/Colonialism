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
        return this.explorers.stream()
                .anyMatch(curExplorer -> !curExplorer.isExploring());
    }

    /**
     * Sets a new Target for the Fleet to Explore
     *
     * @param target Point where the Fleet will explore
     */
    public void explore(Point target) {
        this.explorers.stream()
                .filter(e -> !e.isExploring()).findFirst()
                .ifPresent(e -> e.setTarget(target));
    }

    /**
     * Updates the Fleet's Home City to the param location
     *
     * @param location Point where the Fleet will be based out of
     */
    public void setHomeCity(Point location) {
        this.explorers.forEach(curExplorer ->
                curExplorer.setOrigin(location)
        );
    }

    /**
     * Moves all idle explorers in the fleet to the target location
     *
     * @param location where the idle explorers will be moved to
     */
    public void setIdleExplorersLocation(Point location){
        setHomeCity(location);
        this.explorers.stream()
                .filter(curExplorer -> !curExplorer.isExploring())
                .forEach(curExplorer -> curExplorer.setLocation(location));
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
        return this.explorers.stream()
                .filter(Explorer::isExploring)
                .mapToInt(Explorer::getFunding)
                .sum();
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
