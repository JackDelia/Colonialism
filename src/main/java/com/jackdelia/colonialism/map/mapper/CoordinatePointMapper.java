package com.jackdelia.colonialism.map.mapper;

import com.jackdelia.colonialism.map.model.Coordinate;

import java.awt.*;

/**
 * Handler for Mapping Coordinate POJO's to Java.Awt Point Objects
 *
 * @author  Andrew Parise
 * @version June 8th 2018
 * @since   June 8th 2018
 *
 */
public class CoordinatePointMapper {

    /**
     * Default Constructor
     */
    public CoordinatePointMapper(){}

    /**
     * Maps a Coordinate to a Java.Awt.Point object
     *
     * @param coordinate    a coordinate object
     * @return  a fully instantiated Point object
     */
    public Point convertCoordinate(Coordinate coordinate){
        int coordXValue = coordinate.getxPosition();
        int coordYValue = coordinate.getyPosition();

        return new Point(coordXValue, coordYValue);

    }

}
