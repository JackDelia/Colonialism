package com.jackdelia.colonialism.knowledge;

import java.awt.*;
import java.util.HashSet;

public class Knowledge {

    private HashSet<Point> knowledge;

    /**
     * Default Constructor
     */
    public Knowledge() {
        this.knowledge = new HashSet<>();
    }

    /**
     * Marks the location as seen
     * @param mapLocation the location to mark down as seen
     */
    public void markSeen(Point mapLocation) {
        this.knowledge.add(mapLocation);
    }

    /**
     * Clears the knowledge
     */
    public void resetKnowledge() {
        this.knowledge.clear();
    }

    public HashSet<Point> getKnowledge() {
        return this.knowledge;
    }
}
