package com.jackdelia.colonialism.player

import javax.swing.*

/**
 * Controller Class for a Player's Name
 */
class PlayerNameController {

    PlayerNameModel playerModel
    PlayerNameLabelView playerLabelView

    /**
     * Default Constructor
     * @param playerNameModel the PlayerNameModel to be handled
     */
    PlayerNameController(PlayerNameModel playerNameModel) {
        playerModel = playerNameModel
        playerLabelView = new PlayerNameLabelView(playerModel)
    }

    /**
     * Sets the name of the Player to the param nameValue
     * @param nameValue
     */
    void setName(String nameValue) {
        playerModel.name = nameValue
    }

    /**
     * Adds the associated PlayerNameView to the param panel
     * @param panel the JPanel to have this PlayerNameView attached to
     */
    void addViewToPanel(JPanel panel) {
        panel?.add(playerLabelView)
    }
\
}
