package com.jackdelia.colonialism.explorer;

import com.jackdelia.colonialism.player.Player;


import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 */
public class ExplorationPanel extends JPanel {

	private Player player;
	
	private ExplorationPanel() {}

    /**
     * Factory Method to handle creating a new Instance of an Exploration Panel
     *
     * @param player the Player to be associated with this Panel
     * @return a constructed instance of an ExplorationPanel
     */
	public static ExplorationPanel create(Player player) {
	    ExplorationPanel constructedExplorationPanel = new ExplorationPanel();

        constructedExplorationPanel.setPlayer(player);
        constructedExplorationPanel.setLayout(new BoxLayout(constructedExplorationPanel, BoxLayout.PAGE_AXIS));
        constructedExplorationPanel.add(new JLabel("Explorers"));

        final JPanel explorersPanel = new JPanel();
        explorersPanel.setLayout(new BoxLayout(explorersPanel, BoxLayout.PAGE_AXIS));

        player.getExplorers().stream()
                .map(ExplorerPanel::create)
                .forEach(explorersPanel::add);

        constructedExplorationPanel.add(explorersPanel);

        JButton addExplorer = new JButton("+");
        addExplorer.addActionListener(e -> {
            Explorer added = Explorer.create(player.getPosition());
            player.addExplorer(added);
            explorersPanel.add(ExplorerPanel.create(added));
        });
        constructedExplorationPanel.add(addExplorer);

	    return constructedExplorationPanel;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
