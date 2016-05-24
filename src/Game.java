import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Game extends JFrame{
	private boolean exploreClicked;
	private boolean foundClicked;
	private Player pc;
	private Map gameMap;
	private JPanel buttonPanel;
	
	public Game(){
		super("Colonialism!");
		this.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });    
		
		exploreClicked = false;
		foundClicked = false;
		gameMap = new Map();
		pc = new Player("Jack", gameMap);
		gameMap.player = pc;
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		JButton exploreButton = new JButton("Explore");
		exploreButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					exploreClicked = true;
				}
		});
		
		
		
		buttonPanel.add(exploreButton);
		
		JButton foundButton = new JButton("Found City");
		foundButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				foundClicked = true;
			}
			
		});
		
		buttonPanel.add(foundButton);
		buttonPanel.add(new JButton("Wait"));
		
		gameMap.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				if(exploreClicked)
					explore(e.getX()/Map.PIXELSTEP, e.getY()/Map.PIXELSTEP);
				else if(foundClicked)
					foundCity(e.getX()/Map.PIXELSTEP, e.getY()/Map.PIXELSTEP);
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
			
		});
		
		JPanel container = new JPanel();
		setSize(800, 600);
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		container.setSize(700,700);
		container.add(gameMap);
		container.add(buttonPanel);
		add(container);
		setVisible(true);
		pc.ships.add(new Ship(pc.yloc,pc.xloc,pc,1));
	}

	private void foundCity(int x, int y) {
		String cityName = JOptionPane.showInputDialog("Choose A City Name");
		if(cityName == null)
			return;
		City c = pc.foundCity(cityName, x, y);
		
		foundClicked = false;
		JButton cityButton = new JButton(cityName);
		cityButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				System.out.println(c.toString());	
			}
			
		});
		buttonPanel.add(cityButton);
		repaint();
	}

	private void explore(int i, int j) {
		for(int x = -6; x <= 6; x++)
			for(int y = -6; y <= 6; y++)
				if(x+i >= 0 && x+i < Map.MAPSIZE && y+j >= 0 && y+j < Map.MAPSIZE && Math.sqrt(Math.pow(x,2)+Math.pow(y,2)) <= 6)
					pc.visible[i+x][j+y] = true;
		
		repaint();
		exploreClicked = false;
	}

	public void run() {	
		while (true){
			repaint();
			gameMap.repaint();
		}
	}

}
