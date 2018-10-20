package com.jackdelia.colonialism.currency;

import java.util.Observable;

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
public class Funding extends Observable {

    public static final int DEFAULT_STARTING_CASH = 1;

    private int cash;

    /**
     * Default Constructor
     */
    public Funding() {
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

        setCash(cash);
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
        if(howMuch == 0) {
            return;
        }

        if(howMuch < 0) {
            removeCash(-1 * howMuch);
        }

        final int initialCash = this.cash;
        int newCashAmount = initialCash + howMuch;

        setCash(newCashAmount);
    }

    /**
     * Removes money from the Explorer's Funding
     *
     * @param howMuch cash to remove from the funding
     */
    public void removeCash(int howMuch){
        if(howMuch == 0) {
            return;
        }

        if(howMuch < 0) {
            addCash(-1 * howMuch);
        }

        final int initialCash = this.cash;
        int newCashAmount = initialCash - howMuch;

        // prevent the explorer from going completely broke
        if(newCashAmount <= 0) {
            newCashAmount = 1;
        }

        setCash(newCashAmount);
    }

    private void setCash(int newCashValue) {
        if(this.cash != newCashValue) {
            this.cash = newCashValue;
            setChanged();
            notifyObservers(newCashValue);
        }
    }

}
