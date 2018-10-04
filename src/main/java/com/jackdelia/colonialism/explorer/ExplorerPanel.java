package com.jackdelia.colonialism.explorer;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
     * @return a constructed isntance of an ExplorerPanel
     */
	public static ExplorerPanel create(final Explorer explorer) {
	    ExplorerPanel constructedExplorerPanel = new ExplorerPanel();

        constructedExplorerPanel.setExplorer(explorer);

        constructedExplorerPanel.setLayout(new BoxLayout(constructedExplorerPanel, BoxLayout.LINE_AXIS));
        constructedExplorerPanel.setLabel(new JLabel(explorer.toString()));
        constructedExplorerPanel.add(constructedExplorerPanel.getLabel());

        JButton increaseFunding = new JButton("^");
        increaseFunding.addActionListener(e -> explorer.incrementFunding(1));

        JButton decreaseFunding = new JButton("v");
        decreaseFunding.addActionListener(e -> explorer.incrementFunding(-1));

        constructedExplorerPanel.add(decreaseFunding);
        constructedExplorerPanel.add(increaseFunding);

	    return constructedExplorerPanel;
    }
	
	public void paintComponent(Graphics g){
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