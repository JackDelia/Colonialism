package com.jackdelia.colonialism.city;

import com.jackdelia.colonialism.map.Map;
import com.jackdelia.colonialism.player.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class CityTest {

    @Test
    public void constructorShouldInitCityWithParamName(){
        // Setup Test Data
        Map gameMap = new Map();

        String playerName = "Merlin";
        Player player = new Player(playerName, gameMap);

        String cityName = "Avalon";
        int xPosition = 10;
        int yPosition = 10;

        // Perform Method Call
        City testCity = new City(cityName, xPosition, yPosition, player, gameMap);

        // Verify
        assertEquals(cityName, testCity.getName());
    }

    @Test
    public void constructorShouldInitCityWithParamLocation(){
        // Setup Test Data
        Map gameMap = new Map();

        String playerName = "Arthur";
        Player player = new Player(playerName, gameMap);

        String cityName = "Camelot";
        int xPosition = 10;
        int yPosition = 20;

        // Perform Method Call
        City testCity = new City(cityName, xPosition, yPosition, player, gameMap);

        // Verify
        assertEquals(xPosition, testCity.getPosition().x);
        assertEquals(yPosition, testCity.getPosition().y);
    }



}