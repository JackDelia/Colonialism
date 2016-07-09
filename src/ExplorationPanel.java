import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ExplorationPanel extends JPanel {

	private Player player;
	
	public ExplorationPanel(Player p) {
		this.player = p;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(new JLabel("Explorers"));
		JPanel explorersPanel = new JPanel();
		explorersPanel.setLayout(new BoxLayout(explorersPanel, BoxLayout.PAGE_AXIS));
		ArrayList<Explorer> explorers = player.getExplorers();
		for(Explorer e: explorers){
			ExplorerPanel newPan = new ExplorerPanel(e);
			explorersPanel.add(newPan);
		}
		add(explorersPanel);
		JButton addExplorer = new JButton("+");
		addExplorer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Explorer added = new Explorer(player.getPosition());
				player.addExplorer(added);
				explorersPanel.add(new ExplorerPanel(added));
			}
		});
		add(addExplorer);
	}

}
