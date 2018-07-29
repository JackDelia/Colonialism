package com.jackdelia.colonialism.player;


import java.util.ArrayList;

/**
 * <p>Information about a Collection of Players</p>
 * <p>
 * Change Log:
 * </p>
 * <ul>
 *     <li>v0.5 - Initial File Creation. Extracted logic from {@link com.jackdelia.colonialism.player.Player}.</li>
 * </ul>
 *
 * @author <a href="mailto:andrewparise1994@gmail.com">Andrew Parise</a>
 * @since 0.5
 * @version 0.5
 */
public class Vassal {

    private ArrayList<Player> players;

    /**
     * Default Constructor
     */
    public Vassal(){
        this.players = new ArrayList<>();
    }

    /**
     * Adds a new Player to the Vassal
     *
     * @param newPlayer the Player to add to the Vassal
     */
    public void addPlayer(Player newPlayer) {
        this.players.add(newPlayer);
    }

    /**
     * Fetches the Players in the Vassal
     *
     * @return the players in the Vassal
     */
    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    /**
     * Fetch the Number of Players in this Vassal
     *
     * @return int the number of players in this Vassal
     */
    public int size() {
        return this.players.size();
    }

}
