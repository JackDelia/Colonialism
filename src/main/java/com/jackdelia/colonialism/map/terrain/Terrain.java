package com.jackdelia.colonialism.map.terrain;

import java.awt.*;

/**
 * <p>Information about a Map Location's Terrain</p>
 * <p>
 * Change Log:
 * </p>
 * <ul>
 *     <li>v0.1 - Initial File Creation.</li>
 *     <li>v0.5 - Added behavior extracted from {@link com.jackdelia.colonialism.map.Map}.</li>
 * </ul>
 *
 * @author <a href="mailto:JackDelia542@gmail.com">Jack Delia</a>
 * @author <a href="mailto:AndrewParise1994@gmail.com">Andrew Parise</a>
 * @since 0.1
 * @version 0.5
 */
public enum Terrain {

    PLAINS,	MOUNTAINS, OCEAN, HILLS, FORREST, DESERT, INVALID;

    /**
     * Fetch the Color to Display on the Map
     *
     * @return the associated color
     */
    public Color getColor() {
        switch(this){
            case PLAINS:
                return new Color(255, 225, 79);
            case MOUNTAINS:
                return Color.BLACK;
            case OCEAN:
                return Color.BLUE;
            case HILLS:
                return Color.ORANGE;
            case FORREST:
                return Color.GREEN;
            case DESERT:
                return Color.YELLOW;
            default:
                // fail gracefully
                return Color.MAGENTA;
        }
    }
}


