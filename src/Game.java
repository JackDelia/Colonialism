import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;


public class Game extends JFrame{
	private boolean exploreClicked;
	private boolean foundClicked;
	private boolean paused;
	private Player pc;
	private Map gameMap;
	private JPanel buttonPanel;
	private JEditorPane messages;
	private String currentMessage = "";
	private int day;
	private long lastUpdate;
	
	public Game(){
		super("Colonialism!");
		this.getContentPane().setBackground(new Color(255,0,255));
		this.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });    
		
		lastUpdate = System.currentTimeMillis();
		exploreClicked = false;
		foundClicked = false;
		gameMap = new Map();
		pc = new Player("Jack", gameMap);
		gameMap.player = pc;
		
		buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(255,0,255));
		buttonPanel.setPreferredSize(new Dimension(-450,-450));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		JButton exploreButton = new JButton("Explore");
		exploreButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(exploreClicked){
						exploreClicked = false;
						currentMessage = "";
						return;
					}
					exploreClicked = true;
					foundClicked = false;
					currentMessage = "Explore Where?";
				}
		});
		
		
		
		buttonPanel.add(exploreButton);
		
		JButton foundButton = new JButton("Found City");
		foundButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				foundClicked = true;
				exploreClicked = false;
				currentMessage = "Pick a Location";
			}
			
		});
		
		JButton pause = new JButton("pause");
		pause.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				paused = !paused;
			}
			
		});
		
		buttonPanel.add(foundButton);
		buttonPanel.add(pause);
		messages = new JEditorPane();
		messages.setEditable(false);
		messages.setBounds(0,0,250, 450);
		JPanel messagesHolder = new JPanel();
		messagesHolder.setLayout(null);
		messagesHolder.setPreferredSize(new Dimension(1,1));
		messagesHolder.add(messages);
		messagesHolder.setBounds(0,0,50,50);
		messagesHolder.setBackground(new Color(255,0,255));
		buttonPanel.add(messagesHolder);
		
		gameMap.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				if(exploreClicked){
					if(!pc.inRange(e.getX()/Map.PIXELSTEP, e.getY()/Map.PIXELSTEP)){
						currentMessage += "\nToo far away";
						return;
					}
					if(pc.money < 20){
						currentMessage += "\nNot Enough Money.";
						return;
					}
					pc.money -= 20;
					explore(e.getX()/Map.PIXELSTEP, e.getY()/Map.PIXELSTEP);
				}
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
		setSize(1000, 600);
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		container.setSize(700,700);
		container.add(gameMap);
		container.add(buttonPanel);
		container.setBackground(new Color(255,0,255));
		add(container);
		setVisible(true);
		pc.ships.add(new Ship(pc.yloc,pc.xloc,pc,1));
	}

	private void foundCity(int x, int y) {
		boolean wasPaused = paused;
		paused = true;
		String cityName = JOptionPane.showInputDialog("Choose A City Name");
		paused = wasPaused;
		if(cityName == null)
			return;
		City c = pc.foundCity(cityName, x, y);
		if (c == null)
			return;
		
		foundClicked = false;
		JButton cityButton = new JButton(cityName);
		cityButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				currentMessage = c.toString();	
			}
			
		});
		buttonPanel.add(cityButton);
		currentMessage = cityName + " founded.";
		repaint();
	}
	

	private void explore(int i, int j) {
		for(int x = -6; x <= 6; x++)
			for(int y = -6; y <= 6; y++)
				if(x+i >= 0 && x+i < Map.MAPSIZE && y+j >= 0 && y+j < Map.MAPSIZE && Math.sqrt(Math.pow(x,2)+Math.pow(y,2)) <= 6)
					pc.visible[i+x][j+y] = true;
		
		repaint();
	}

	public void run() {	
		while (true){
			repaint();
			gameMap.repaint();
			if(paused)
				lastUpdate = System.currentTimeMillis();
			if(System.currentTimeMillis() - lastUpdate > 500){
				day++;
				lastUpdate = System.currentTimeMillis();
				messages.setText("Day " + day + "\n" + "Money: " + (int)pc.money + "G\n" + currentMessage);
				pc.Update(1);
			}
		}
	}

}
