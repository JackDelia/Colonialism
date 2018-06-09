package com.jackdelia.colonialism.map.factory;

import com.jackdelia.colonialism.map.model.Coordinate;

/**
 * Factory to handle the creation of Coordinate objects
 *
 * @author  Andrew Parise
 * @since   June 8th 2018
 * @version June 8th 2018
 *
 */
public class CoordinateFactory implements ICoordinateFactory {

    private CoordinateFactory instance;

    /**
     * Default Constructor
     *  Access is private as per Singleton pattern
     */
    private CoordinateFactory() {}

    /**
     * Accessor for an instance of this Factory as per the Singleton pattern
     *
     * @return  an instance of this factory
     */
    public ICoordinateFactory getInstance() {
        if(this.instance == null){
            this.instance = new CoordinateFactory();
        }
        return this.instance;
    }

    /**
     * Handles the construction of Coordinate objects
     *
     * @param xPosition the xPosition of the coordinate
     * @param yPosition the yPosition of the coordinate
     * @return  a constructed Coordinate populated with the param values
     */
    public Coordinate constructCoordinate(int xPosition, int yPosition) {
        Coordinate constructedCoordinate = new Coordinate();
        constructedCoordinate.setxPosition(xPosition);
        constructedCoordinate.setyPosition(yPosition);
        return constructedCoordinate;
    }


}
