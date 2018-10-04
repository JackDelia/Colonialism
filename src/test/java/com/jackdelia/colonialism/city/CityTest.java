package com.jackdelia.colonialism.city;

import com.jackdelia.colonialism.map.Map;
import com.jackdelia.colonialism.player.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class CityTest {

    @Test
    public void createShouldInitCityWithParamName(){
        // Setup Test Data
        Map gameMap = Map.create();

        String playerName = "Merlin";
        Player player = Player.create(playerName, gameMap);

        String cityName = "Avalon";
        int xPosition = 10;
        int yPosition = 10;

        // Perform Method Call
        City testCity = City.create(cityName, xPosition, yPosition, player, gameMap);

        // Verify
        assertEquals(cityName, testCity.getName());
    }

    @Test
    public void createShouldInitCityWithParamLocation(){
        // Setup Test Data
        Map gameMap = Map.create();

        String playerName = "Arthur";
        Player player = Player.create(playerName, gameMap);

        String cityName = "Camelot";
        int xPosition = 10;
        int yPosition = 20;

        // Perform Method Call
        City testCity = City.create(cityName, xPosition, yPosition, player, gameMap);

        // Verify
        assertEquals(xPosition, testCity.getPosition().x);
        assertEquals(yPosition, testCity.getPosition().y);
    }



}