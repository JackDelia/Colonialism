package com.jackdelia.colonialism.map;

/**
 * <p>Information about the age of a Game Map</p>
 * <p>
 * Change Log:
 * </p>
 * <ul>
 *     <li>v0.5 - Initial File Creation. Extracted logic from {@link com.jackdelia.colonialism.map.Map}.</li>
 * </ul>
 *
 * @author <a href="mailto:andrewparise1994@gmail.com">Andrew Parise</a>
 * @since 0.5
 * @version 0.5
 */
public class MapAge {

    private int age;

    /**
     * Default Constructor
     */
    MapAge() {
        this.age = 0;
    }

    /**
     * Checks to see if the Map has been is in it's Normal Age
     *
     * @return boolean true if the map is in the Normal Age, false if not
     */
    public boolean isInNormalAge(){
        return (this.age % 7000) < 3500;
    }

    /**
     * Increments the map's age - to be done after each game turn
     */
    public void incrementAge(){
        this.age++;
    }

    /**
     * Fetch the age of the Map
     *
     * @return int the age of the Map
     */
    public int getAge(){
        return this.age;
    }

}
