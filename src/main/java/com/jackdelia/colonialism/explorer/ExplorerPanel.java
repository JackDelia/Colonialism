package com.jackdelia.colonialism.explorer;

import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ExplorerPanel extends JPanel {

	private Explorer explorer;
	private JLabel label;
	
	private ExplorerPanel() {}

    /**
     * Factory Method for Creating new Instances of ExplorerPanels
     * @param explorer the Explorer to be displayed
     * @return a constructed instance of an ExplorerPanel
     */
	public static ExplorerPanel create(final Explorer explorer) {
	    ExplorerPanel constructedExplorerPanel = new ExplorerPanel();

        constructedExplorerPanel.setExplorer(explorer);

        constructedExplorerPanel.setLayout(new BoxLayout(constructedExplorerPanel, BoxLayout.LINE_AXIS));

        JLabel explorerLabel = new JLabel(explorer.toString());
        constructedExplorerPanel.setLabel(explorerLabel);
        constructedExplorerPanel.add(explorerLabel);

        JButton increaseFunding = new JButton("^");
        increaseFunding.addActionListener((ActionEvent e) -> explorer.incrementFunding(1));

        JButton decreaseFunding = new JButton("v");
        decreaseFunding.addActionListener((ActionEvent e) -> explorer.incrementFunding(-1));

        constructedExplorerPanel.add(increaseFunding);
        constructedExplorerPanel.add(decreaseFunding);

	    return constructedExplorerPanel;
    }
	
	public void paintComponent(Graphics g) {
		label.setText(explorer.toString());
	}


    public Explorer getExplorer() {
        return explorer;
    }

    public void setExplorer(Explorer explorer) {
        this.explorer = explorer;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }
}