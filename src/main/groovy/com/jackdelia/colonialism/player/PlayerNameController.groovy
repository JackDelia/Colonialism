package com.jackdelia.colonialism.player

import javax.swing.*

class PlayerNameController {

    PlayerNameModel playerModel
    PlayerNameLabelView playerLabelView

    PlayerNameController() {
        this(new PlayerNameModel())
    }

    PlayerNameController(PlayerNameModel playerNameModel) {
        playerModel = playerNameModel
        playerLabelView = new PlayerNameLabelView(playerModel)
    }

    void setName(String nameValue) {
        playerModel.name = nameValue
    }

    void addViewToPanel(JPanel panel) {
        panel?.add(playerLabelView)
    }
\
}
