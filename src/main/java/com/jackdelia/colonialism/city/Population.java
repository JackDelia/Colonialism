package com.jackdelia.colonialism.city;

import java.util.Observable;

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
public class Population extends Observable {

    private static final int DEFAULT_STARTING_POPULATION = 50;

    private int numberOfPeople;

    /**
     * Default Constructor
     */
    Population(){
        setPopulation(DEFAULT_STARTING_POPULATION);
    }

    /**
     * Constructor With Specified Population Size
     */
    public Population(int numberOfPeople) {

        // validate input
        if(numberOfPeople < 0){
            numberOfPeople = DEFAULT_STARTING_POPULATION;
        }

        setPopulation(numberOfPeople);
    }

    /**
     * Fetches the number of people
     *
     * @return int the population count
     */
    int getNumberOfPeople(){
        return this.numberOfPeople;
    }

    /**
     * Increases the Population by the Specified Amount
     *
     * @param howManyPeople the number to increase the population by
     */
    void increasePopulation(int howManyPeople){

        if(howManyPeople == 0) {
            return;
        }

        if(howManyPeople < 0) {
            decreasePopulation(-1 * howManyPeople);
        }

        final int initialPopulation = this.numberOfPeople;
        int newPopulationAmount = initialPopulation + howManyPeople;

        // prevent the explorer from going completely broke
        if(newPopulationAmount < 0) {
            newPopulationAmount = 0;
        }

        setPopulation(newPopulationAmount);
    }

    /**
     * Decreases the Population by the Specified Amount
     *
     * @param howManyPeople the number to decrease the population by
     */
    void decreasePopulation(int howManyPeople){

        if(howManyPeople == 0) {
            return;
        }

        if(howManyPeople < 0) {
            increasePopulation(-1 * howManyPeople);
        }


        final int initialPopulation = this.numberOfPeople;
        int newPopulationAmount = initialPopulation - howManyPeople;

        // prevent the explorer from going completely broke
        if(newPopulationAmount < 0) {
            newPopulationAmount = 0;
        }

        setPopulation(newPopulationAmount);
    }

    private void setPopulation(int newPopulationValue) {
        if(this.numberOfPeople != newPopulationValue) {
            this.numberOfPeople = newPopulationValue;
            setChanged();
            notifyObservers(newPopulationValue);
        }
    }

}
