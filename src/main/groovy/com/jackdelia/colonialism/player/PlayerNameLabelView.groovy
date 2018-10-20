package com.jackdelia.colonialism.player

import javax.swing.*

/**
 * JLabel View for a Player's Name
 */
class PlayerNameLabelView extends JLabel {

    String text

    PlayerNameLabelView(PlayerNameModel playerNameModel) {
        text = { playerNameModel.name }()
    }

}
