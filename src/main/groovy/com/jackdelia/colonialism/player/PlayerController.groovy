package com.jackdelia.colonialism.player

import javax.swing.*

class PlayerController {

    PlayerModel playerModel
    PlayerLabelView playerLabelView

    PlayerController() {
        playerModel = new PlayerModel()
        playerLabelView = new PlayerLabelView(observable: playerModel)
        playerLabelView.subscribe()
    }

    void setName(String nameValue) {
        playerModel.name = nameValue
    }

    void addViewToPanel(JPanel panel) {
        panel?.add(playerLabelView)
    }

}
