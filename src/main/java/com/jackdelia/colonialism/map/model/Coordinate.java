package com.jackdelia.colonialism.map.model;

/**
 * POJO to contain a single map coordinate
 *
 * @author  Andrew Parise
 * @since   June 8th 2018
 * @version June 8th 2018
 */
public class Coordinate {

    private int xPosition;
    private int yPosition;

    /**
     * Default Constructor
     */
    public Coordinate(){}

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }
}
