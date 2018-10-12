package com.jackdelia.colonialism.currency;

/**
 * <p>Information about an Explorer or Player's Funding</p>
 * <p>
 * Change Log:
 * </p>
 * <ul>
 *     <li>v0.5 - Initial File Creation. Extracted logic from {@link com.jackdelia.colonialism.explorer.Explorer}.</li>
 * </ul>
 *
 * @author <a href="mailto:andrewparise1994@gmail.com">Andrew Parise</a>
 * @since 0.5
 * @version 0.5
 */
public class Funding {

    private static final int DEFAULT_STARTING_CASH = 1;

    private int cash;

    /**
     * Default Constructor
     */
    public Funding(){
        this.cash = DEFAULT_STARTING_CASH;
    }

    /**
     * Constructor With Specified Cash
     */
    public Funding(int cash) {

        // validate input
        if(cash < 0) {
            cash = DEFAULT_STARTING_CASH;
        }

        this.cash = cash;
    }

    /**
     * Fetch the Cash
     *
     * @return int representing the cash left in the account
     */
    public int getCash(){
        return this.cash;
    }

    /**
     * Adds money to the Funding
     *
     * @param howMuch cash to add into the funding
     */
    public void addCash(int howMuch){
        this.cash += howMuch;
    }

    /**
     * Removes money from the Explorer's Funding
     *
     * @param howMuch cash to remove from the funding
     */
    public void removeCash(int howMuch){
        this.cash -= howMuch;

        // prevent the explorer from going completely broke
        if(this.cash <= 0) {
            this.cash = 1;
        }
    }

}
