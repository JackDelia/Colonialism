package com.jackdelia.colonialism.city;

/**
 * <p>Information about a City's Population</p>
 * <p>
 * Change Log:
 * </p>
 * <ul>
 *     <li>v0.5 - Initial File Creation. Extracted logic from {@link com.jackdelia.colonialism.city.City}.</li>
 * </ul>
 *
 * @author <a href="mailto:andrewparise1994@gmail.com">Andrew Parise</a>
 * @since 0.5
 * @version 0.5
 */
public class Population {

    private static final int DEFAULT_STARTING_POPULATION = 50;

    private int numberOfPeople;

    /**
     * Default Constructor
     */
    public Population(){
        this.numberOfPeople = DEFAULT_STARTING_POPULATION;
    }

    /**
     * Constructor With Specified Population Size
     */
    public Population(int numberOfPeople) {

        // validate input
        if(numberOfPeople < 0){
            numberOfPeople = DEFAULT_STARTING_POPULATION;
        }

        this.numberOfPeople = numberOfPeople;
    }

    /**
     * Fetches the number of people
     *
     * @return int the population count
     */
    public int getNumberOfPeople(){
        return this.numberOfPeople;
    }

    /**
     * Increases the Population by the Specified Amount
     *
     * @param howManyPeople the number to increase the population by
     */
    public void increasePopulation(int howManyPeople){
        this.numberOfPeople += howManyPeople;
    }

    /**
     * Decreases the Population by the Specified Amount
     *
     * @param howManyPeople the number to decrease the population by
     */
    public void decreasePopulation(int howManyPeople){
        this.numberOfPeople -= howManyPeople;

        // if the decrease drops into the negatives, then set to zero
        if(this.numberOfPeople < 0){
            this.numberOfPeople = 0;
        }

    }

}
