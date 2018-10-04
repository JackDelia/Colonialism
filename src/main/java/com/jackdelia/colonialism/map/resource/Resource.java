package com.jackdelia.colonialism.map.resource;

/**
 * <p>Information about a Game Resource</p>
 * <p>
 * Change Log:
 * </p>
 * <ul>
 *     <li>v0.1 - Initial File Creation.</li>
 *     <li>v0.5 - Added behavior extracted from {@link com.jackdelia.colonialism.Game}.</li>
 * </ul>
 *
 * @author <a href="mailto:JackDelia542@gmail.com">Jack Delia</a>
 * @author <a href="mailto:AndrewParise1994@gmail.com">Andrew Parise</a>
 * @since 0.1
 * @version 0.5
 */
public enum Resource {
	COTTON, IRON, STONE, GOLD, JEWELRY, MEAT, FISH, GRAIN, WOOD, WEAPONS, CLOTHING, TOOLS, SOLDIERS;

    /**
     * Fetch the Price of the Resource
     *
     * @return the associated price
     */
    public Double getPrice() {
        switch(this){
            case COTTON:
                return 0.09;
            case IRON:
                return 0.10;
            case STONE:
                return 0.08;
            case GOLD:
                return 1.00;
            case JEWELRY:
                return 1.20;
            case MEAT:
                return 0.10;
            case FISH:
                return 0.08;
            case GRAIN:
                return 0.07;
            case WOOD:
                return 0.10;
            case WEAPONS:
                return 0.20;
            case CLOTHING:
                return 0.15;
            case TOOLS:
                return 0.18;
            case SOLDIERS:
                return 0.40;
            default:
                // fail gracefully
                return 0.0;
        }
    }

}
