package com.jackdelia.colonialism.basics

import com.jackdelia.colonialism.Game
import com.jackdelia.colonialism.day.DayController
import com.jackdelia.colonialism.explorer.ExplorationPanel
import com.jackdelia.colonialism.money.MoneyController
import com.jackdelia.colonialism.player.Player
import com.jackdelia.colonialism.player.PlayerNameController

import javax.swing.BoxLayout
import javax.swing.JPanel

class BasicsPanelView extends JPanel {


    BasicsPanelView(Player player, Game game) {
        def rootPanel = new JPanel()
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS))

        def playerNameController = new PlayerNameController(player.name)
        playerNameController.addViewToPanel(rootPanel)

        def moneyController = new MoneyController(observable: player.money)
        moneyController.subscribe()
        moneyController.addViewToPanel(rootPanel)

        def dayController = new DayController(game.day)
        dayController.addViewToPanel(rootPanel)

        def explorerPanel = ExplorationPanel.create(player)
        rootPanel.add(explorerPanel)

        add(rootPanel)
    }


}
