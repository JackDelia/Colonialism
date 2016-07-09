import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ExplorerPanel extends JPanel {

	private Explorer explorer;
	private JLabel label;
	
	public ExplorerPanel(Explorer ex) {
		explorer = ex;
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		label = new JLabel(explorer.toString());
		add(label);
		JButton increaseFunding = new JButton("^");
		increaseFunding.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				explorer.incrementFunding(1);
			}
		});
		
		JButton decreaseFunding = new JButton("v");
		decreaseFunding.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ex.incrementFunding(-1);
			}
		});
		
		add(decreaseFunding);
		add(increaseFunding);
	}
	
	public void paintComponent(Graphics g){
		label.setText(explorer.toString());
	}

}