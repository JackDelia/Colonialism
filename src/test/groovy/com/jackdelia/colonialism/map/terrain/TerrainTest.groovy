package com.jackdelia.colonialism.map.terrain

import spock.lang.Specification

import java.awt.Color

class TerrainTest extends Specification {

    def "getColor: Should Return the Correct Price"(Terrain testTerrain, Color expectedColor) {

        expect: "The Method's Return Value to Match the Expected Color"
            testTerrain.getColor() == expectedColor

        where: "the Enum and Expected Color are provided"
            testTerrain       | expectedColor
            Terrain.PLAINS    | new Color(255, 225, 79)
            Terrain.MOUNTAINS | Color.BLACK
            Terrain.OCEAN     | Color.BLUE
            Terrain.HILLS     | Color.ORANGE
            Terrain.FORREST   | Color.GREEN
            Terrain.DESERT    | Color.YELLOW
            Terrain.INVALID   | Color.MAGENTA

    }


}
