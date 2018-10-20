package com.jackdelia.colonialism.basics;

import com.jackdelia.colonialism.day.DayController;
import com.jackdelia.colonialism.explorer.ExplorationPanel;
import com.jackdelia.colonialism.Game;
import com.jackdelia.colonialism.money.MoneyController;
import com.jackdelia.colonialism.player.Player;
import com.jackdelia.colonialism.player.PlayerNameController;
import com.jackdelia.colonialism.player.PlayerNameModel;

import java.awt.Graphics;

import javax.swing.BoxLayout;

import javax.swing.JPanel;

/**
 *
 *
 */
public class BasicsPanel extends JPanel {

    private BasicsModel basicsModel;
    private MoneyController moneyController;
    private DayController dayController;
    private PlayerNameController playerNameController;

	public BasicsPanel(Player player) {

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));

	    this.basicsModel = new BasicsModel();

	    this.moneyController = new MoneyController();
	    this.moneyController.setObservable(player.getMoney());
	    this.moneyController.subscribe();
	    this.moneyController.addViewToPanel(rootPanel);

	    this.dayController = new DayController();
	    this.dayController.addViewToPanel(rootPanel);

	    this.playerNameController = new PlayerNameController(player.getName());
	    this.playerNameController.addViewToPanel(rootPanel);

        JPanel explorerPanel = ExplorationPanel.create(player);
        rootPanel.add(explorerPanel);

        this.add(rootPanel);
    }

    /**
     * Factory Method to encapsulate Creation of BasicsPanels
     * @param player the Player to be displayed on the Panel
     * @param game the Game data to be displayed
     * @return a constructed BasicsPanel
     */
	public static BasicsPanel create(Player player, Game game) {
	    BasicsPanel constructedBasicsPanel = new BasicsPanel(player);

        constructedBasicsPanel.basicsModel.setGameDay(game.getDay());

	    return constructedBasicsPanel;
    }

	public void paintComponent(Graphics g) {

	    this.dayController.setDay(this.basicsModel.getGameDay());

	}

}
