package com.jackdelia.colonialism.basics;

import com.jackdelia.colonialism.explorer.ExplorationPanel;
import com.jackdelia.colonialism.Game;
import com.jackdelia.colonialism.player.Player;

import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class BasicsPanel extends JPanel {

	private Player player;
	private Game game;
	private JLabel dayLabel;
	private JLabel moneyLabel;
	private JPanel aPan;
	
	public BasicsPanel(Player player, Game game) {
		this.player = player;
		this.game = game;
		aPan = new JPanel();
		aPan.setLayout(new BoxLayout(aPan, BoxLayout.PAGE_AXIS));
		aPan.add(new JLabel(player.getName()));
		dayLabel = new JLabel("Day \t"+game.getDay());
		moneyLabel = new JLabel("Money: \t" + player.getMoney());
		aPan.add(moneyLabel);
		aPan.add(dayLabel);
		aPan.add(new ExplorationPanel(this.player));
		add(aPan);
	}
	
	public void paintComponent(Graphics g){
		moneyLabel.setText("Money: \t" + Game.trim(player.getMoney()));
		dayLabel.setText("Day \t"+game.getDay());
	}
	
}
