package com.jackdelia.colonialism.basics;

import com.jackdelia.colonialism.day.DayController;
import com.jackdelia.colonialism.explorer.ExplorationPanel;
import com.jackdelia.colonialism.Game;
import com.jackdelia.colonialism.money.MoneyController;
import com.jackdelia.colonialism.player.Player;
import com.jackdelia.colonialism.player.PlayerNameController;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * JPanel that Displays basic information about the Player
 */
public class BasicsPanel extends JPanel {

    private BasicsPanel(@NotNull Player player, @NotNull Game game) {

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));

        MoneyController moneyController = new MoneyController();
	    moneyController.setObservable(player.getMoney());
	    moneyController.subscribe();
	    moneyController.addViewToPanel(rootPanel);

        DayController dayController = new DayController(game.getDay());
	    dayController.addViewToPanel(rootPanel);

        PlayerNameController playerNameController = new PlayerNameController(player.getName());
	    playerNameController.addViewToPanel(rootPanel);

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



}
