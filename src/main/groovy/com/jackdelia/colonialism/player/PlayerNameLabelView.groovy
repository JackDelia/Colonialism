package com.jackdelia.colonialism.player

import javax.swing.*

class PlayerNameLabelView extends JLabel {

    String text

    PlayerNameLabelView(PlayerNameModel playerNameModel) {
        text = { playerNameModel.name }()
    }

}
