package com.jackdelia.colonialism.map;

import org.junit.Test;

import static org.junit.Assert.*;

public class MapTest {

    @Test
    public void getTerrainShouldReturnInvalidForBadInput(){
        // Test Data Setup
        Map testMap = new Map();

        // Perform Method Call
        Terrain resultWhenAboveByOne = testMap.getTerrain(Map.MAP_SIZE + 1, Map.MAP_SIZE + 1);
        Terrain resultWhenAboveByALot = testMap.getTerrain(Map.MAP_SIZE + 5000, Map.MAP_SIZE + 123456);
        Terrain resultWithNegatives = testMap.getTerrain(-5000, -12345);

        // Validate Results
        assertEquals(Terrain.INVALID, resultWhenAboveByALot);
        assertEquals(Terrain.INVALID, resultWhenAboveByOne);
        assertEquals(Terrain.INVALID, resultWithNegatives);
    }

}