package com.jackdelia.colonialism.basics;

import com.jackdelia.colonialism.day.DayController;
import com.jackdelia.colonialism.explorer.ExplorationPanel;
import com.jackdelia.colonialism.Game;
import com.jackdelia.colonialism.money.MoneyController;
import com.jackdelia.colonialism.player.Player;
import com.jackdelia.colonialism.player.PlayerNameController;
import com.jackdelia.colonialism.player.PlayerNameModel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.Graphics;

import javax.swing.BoxLayout;

import javax.swing.JPanel;

/**
 *
 *
 */
public class BasicsPanel extends JPanel {

    private MoneyController moneyController;
    private DayController dayController;
    private PlayerNameController playerNameController;

	public BasicsPanel(Player player, Game game) {

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));

	    this.moneyController = new MoneyController();
	    this.moneyController.setObservable(player.getMoney());
	    this.moneyController.subscribe();
	    this.moneyController.addViewToPanel(rootPanel);

	    this.dayController = new DayController(game.getDay());
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
	@NotNull
    @Contract("_, _ -> new")
    public static BasicsPanel create(Player player, Game game) {
        return new BasicsPanel(player, game);
    }

	public void paintComponent(Graphics g) {

	}

}
