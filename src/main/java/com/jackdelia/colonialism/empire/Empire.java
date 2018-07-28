package com.jackdelia.colonialism.empire;

import com.jackdelia.colonialism.city.City;

import java.util.ArrayList;

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
     * Adds a new City to the Empire
     *
     * @param newCity the newly founded City
     */
    public void addCity(City newCity) {
        this.cities.add(newCity);
    }

}
