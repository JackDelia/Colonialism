package com.jackdelia.colonialism.basics;

import com.jackdelia.colonialism.explorer.ExplorationPanel;
import com.jackdelia.colonialism.Game;
import com.jackdelia.colonialism.player.Player;

import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 *
 */
public class BasicsPanel extends JPanel {

	private Player player;
	private Game game;
	private JLabel dayLabel;
	private JLabel moneyLabel;

	private BasicsPanel() {}

    /**
     * Factory Method to encapsulate Creation of BasicsPanels
     * @param player the Player to be displayed on the Panel
     * @param game the Game data to be displayed
     * @return a constructed BasicsPanel
     */
	public static BasicsPanel create(Player player, Game game) {
	    BasicsPanel constructedBasicsPanel = new BasicsPanel();

        constructedBasicsPanel.setPlayer(player);
        constructedBasicsPanel.setGame(game);
        JPanel aPan = new JPanel();
        aPan.setLayout(new BoxLayout(aPan, BoxLayout.PAGE_AXIS));
        aPan.add(new JLabel(player.getName()));
        constructedBasicsPanel.setDayLabel(new JLabel(String.format("Day \t%d", game.getDay())));
        constructedBasicsPanel.setMoneyLabel(new JLabel(String.format("Money: \t%s", player.getMoney())));
        aPan.add(constructedBasicsPanel.getMoneyLabel());
        aPan.add(constructedBasicsPanel.getDayLabel());
        aPan.add(ExplorationPanel.create(constructedBasicsPanel.getPlayer()));
        constructedBasicsPanel.add(aPan);

	    return constructedBasicsPanel;
    }
	
	public void paintComponent(Graphics g){
		this.getMoneyLabel().setText(String.format("Money: \t%s", Game.trim(this.getPlayer().getMoney())));
		this.getDayLabel().setText(String.format("Day \t%d", this.getGame().getDay()));
	}

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public JLabel getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(JLabel dayLabel) {
        this.dayLabel = dayLabel;
    }

    public JLabel getMoneyLabel() {
        return moneyLabel;
    }

    public void setMoneyLabel(JLabel moneyLabel) {
        this.moneyLabel = moneyLabel;
    }
}
