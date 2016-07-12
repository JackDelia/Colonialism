import java.awt.Component;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class BasicsPanel extends JPanel {

	private Player player;
	private JLabel moneyLabel;
	private JPanel aPan;
	
	public BasicsPanel(Player player) {
		this.player = player;
		aPan = new JPanel();
		aPan.setLayout(new BoxLayout(aPan, BoxLayout.PAGE_AXIS));
		aPan.add(new JLabel(player.getName()));
		moneyLabel = new JLabel("Money: \t" + player.getMoney());
		aPan.add(moneyLabel);
		aPan.add(new ExplorationPanel(this.player));
		add(aPan);
	}
	
	public void paintComponent(Graphics g){
		moneyLabel.setText("Money: \t" + Game.trim(player.getMoney()));
	}
	
}
