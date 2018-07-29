package com.jackdelia.colonialism.player;

/**
 * <p>Information about a Player's Influence</p>
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
public class Influence {

    private static final int DEFAULT_STARTING_INFLUENCE = 50;

    private int influencePoints;

    /**
     * Default Constructor
     */
    public Influence(){
        this.influencePoints = DEFAULT_STARTING_INFLUENCE;
    }

    /**
     * Fetch the Influence Points
     *
     * @return int representing the influencePoints a Player has
     */
    public int getInfluencePoints(){
        return this.influencePoints;
    }

    /**
     * Adds Influence Points to the Influence
     *
     * @param howMuch influencePoints to add
     */
    public void addInfluence(int howMuch){
        this.influencePoints += howMuch;
    }

    /**
     * Removes influence points from the Influence
     *
     * @param howMuch influencePoints to remove
     */
    public void removeInfluence(int howMuch){
        this.influencePoints -= howMuch;

        // prevent the explorer from going completely broke
        if(this.influencePoints <= 0) {
            this.influencePoints = 1;
        }
    }

}
