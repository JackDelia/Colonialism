package com.jackdelia.colonialism.location;

import java.awt.*;

/**
 * <p>Information about an Explorer's Location</p>
 * <p>
 * Change Log:
 * </p>
 * <ul>
 *     <li>v0.5 - Initial File Creation. Extracted logic from {@link com.jackdelia.colonialism.explorer.Explorer}.</li>
 * </ul>
 *
 * @author <a href="mailto:andrewparise1994@gmail.com">Andrew Parise</a>
 * @since 0.5
 * @version 0.5
 */
public class Location {

    private Point point;

    /**
     * Default Constructor
     */
    public Location(){
        this.point = new Point();
    }

    /**
     * Constructor With Specified Point
     */
    public Location(Point point) {
        this.point = point;
    }

    /**
     * Fetch the Point
     *
     * @return Point the coordinate pair representing a location
     */
    public Point getPoint(){
        return this.point;
    }

    /**
     * Setter for the Point
     *
     * @param newPoint for the location to represent
     */
    public void setPoint(Point newPoint){
        this.point = newPoint;
    }

    /**
     * Setter for the Point's X Value
     *
     * @param xValue for the location to represent
     */
    public void setXValue(int xValue){
        this.point.x = xValue;
    }

    /**
     * Setter for the Point's Y Value
     *
     * @param yValue for the location to represent
     */
    public void setYValue(int yValue){
        this.point.y = yValue;
    }

    /**
     * Fetch this Location's X Value
     *
     * @return the x coordinate
     */
    public int getXValue(){
        return this.point.x;
    }

    /**
     * Fetch this Location's Y Value
     *
     * @return the y coordinate
     */
    public int getYValue(){
        return this.point.y;
    }

    /**
     * Compares this Location to Another
     *
     * @param otherLocation the location to compare this one to
     * @return LocationEquality enum representing the result of the comparison
     */
    public LocationEquality compareTo(Location otherLocation) {
        if ((this.point.x != otherLocation.getPoint().x) && (this.point.y != otherLocation.getPoint().y)) {
            return LocationEquality.DIFFERENT_VALUE_XY;
        } else if (this.point.x != otherLocation.getPoint().x) {
            return LocationEquality.DIFFERENT_VALUE_X;
        } else if (this.point.y != otherLocation.getPoint().y) {
            return LocationEquality.DIFFERENT_VALUE_Y;
        } else {
            return LocationEquality.EQUAL;
        }
    }

    /**
     * Translates this Location in the Direction of param targetLocation
     *
     * @param targetLocation the location to move towards
     */
    public void translate(Location targetLocation){
        this.point.translate(targetLocation.getPoint().x, targetLocation.getPoint().y);
    }

    /**
     * Checks if this Location is equal to param otherObject
     *
     * @param otherObject either a Point or a Location to compare against
     * @return true if equal, false if not
     */
    public boolean equals(Object otherObject){

        if(otherObject instanceof Location) {
            // if the x and y values are the same, then they are equal
            return (this.point.x == ((Location) otherObject).getPoint().getX())
                    && (this.point.y == ((Location) otherObject).getPoint().getY());
        }

        if(otherObject instanceof Point) {
            // if it is a Point, then compare based on the underlying point object
            return this.point.equals(otherObject);
        }

        // object type mismatch, so return false
        return false;

    }

}
