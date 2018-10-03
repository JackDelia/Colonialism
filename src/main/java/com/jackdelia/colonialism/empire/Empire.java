package com.jackdelia.colonialism.empire;

import com.jackdelia.colonialism.city.City;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * <p>Information about a Collection of Cities</p>
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
public class Empire {

    private ArrayList<City> cities;

    /**
     * Default Constructor
     */
    public Empire() {
        this.cities = new ArrayList<>();
    }

    /**
     * Fetch the Cities this Empire is Composed of
     *
     * @return ArrayList<City> the cities in this Empire
     */
    public ArrayList<City> getCities() {
        return this.cities;
    }

    /**
     * Fetch the Number of Cities in this Empire
     *
     * @return int the number of cities in this Empire
     */
    public int size() {
        return this.cities.size();
    }

    /**
     * Adds a new City to the Empire
     *
     * @param newCity the newly founded City
     */
    public void addCity(City newCity) {
        this.cities.add(newCity);
    }


    /**
     * Finds the City based on the param Name
     *
     * @param name the name of the city to search for
     * @return City the city corresponding to the param name
     */
    public City findCityByName(String name) {

        return this.cities.stream()
                .filter(city -> name.equals(city.getName()))
                .findAny()
                .orElse(null);
    }

    /**
     * Iterates over each of the Cities in the Empire and updates the days
     *
     * @param howMany days to update by
     */
    public void updateDays(int howMany){
        this.cities.forEach(city -> city.update(howMany));
    }


    /**
     * @return Message describing the Cities in the Empire
     */
    public String toString(){
        return this.cities.stream()
                .map(curCity -> curCity.toString() + "\n")
                .collect(Collectors.joining("", "\nCities:\n", ""));
    }

}
