package com.jackdelia.colonialism.map.factory;

import com.jackdelia.colonialism.map.model.Coordinate;

/**
 * Abstraction for the Coordinate Factory
 *
 * @author  Andrew Parise
 * @since   June 8th 2018
 * @version June 8th 2018
 */
public interface ICoordinateFactory {

    ICoordinateFactory getInstance();

    Coordinate constructCoordinate(int xPosition, int yPosition);

}
