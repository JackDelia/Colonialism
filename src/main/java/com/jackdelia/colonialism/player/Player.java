package com.jackdelia.colonialism.player;


import com.jackdelia.colonialism.map.Map;


/**
 *
 */
public class Player extends BasePlayer {


    private Player(String name, Map map) {
        super(name, map);
    }

    /**
     * Factory Method to handle creation of new Players
     *
     * @param name the name of the Player
     * @param map the game map instance
     * @return a constructed Player instance
     */
    public static Player create(String name, Map map) {
        return new Player(name, map);
    }




}
