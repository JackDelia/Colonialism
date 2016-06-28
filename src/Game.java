import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;


public class Game extends JFrame{
	private boolean exploreClicked;
	private boolean foundClicked;
	private boolean moving;
	private boolean paused;
	private int menu;
	private Player pc;
	private ArrayList<Player> players = new ArrayList<Player>();
	private Map gameMap;
	private JPanel buttonPanel;
	private JPanel moveButtonPanel;
	public JPanel uiPanel;
	private JEditorPane messages;
	private String currentMessage = "";
	private int day;
	private long lastUpdate;
	
	public static final HashMap<String,String> ADVANCED = new HashMap<String,String>();
	static{
		ADVANCED.put("weapons", "iron");
		ADVANCED.put("soldiers", "weapons");
		ADVANCED.put("tools", "stone");
		ADVANCED.put("clothing", "cotton");
		ADVANCED.put("jewelry", "gold");
	}
	
	public static final HashMap<String, Double> prices = new HashMap<String, Double>();
	static {
		prices.put("cotton", .09);
		prices.put("iron", .1);
		prices.put("stone", .08);
		prices.put("gold", 1.0);
		prices.put("jewelry", 1.2);
		prices.put("meat", .1);
		prices.put("fish", .08);
		prices.put("grain", .07);
		prices.put("wood", .1);
		prices.put("weapons", .2);
		prices.put("clothing", .15);
		prices.put("tools", .18);
		prices.put("soldiers", .4);
    }
	
	public Game(){
		super("Colonialism!");
		setSize(1200, 600);
		this.getContentPane().setBackground(Color.YELLOW);
		this.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });    
		
		
		exploreClicked = false;
		foundClicked = false;
		
		uiPanel = new JPanel();
		uiPanel.setBackground(Color.YELLOW);
		uiPanel.setPreferredSize(new Dimension(-250,-250));
		uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.X_AXIS));
		
		setVisible(true);
	}
	
	private void setupMouseListener(){
		gameMap.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				if(exploreClicked){
					explore(e.getX()/Map.PIXELSTEP, e.getY()/Map.PIXELSTEP);
				}
				else if(foundClicked)
					foundCity(e.getX()/Map.PIXELSTEP, e.getY()/Map.PIXELSTEP);
				else if(moving){
					pc.setLocation(null);
					pc.setPosition(new Point(e.getX()/Map.PIXELSTEP, e.getY()/Map.PIXELSTEP));
				}
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
	}
	
	private void setupButtonPanel(){
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.setAutoscrolls(true);
		
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
					moving = false;
					currentMessage = "Explore Where?";
				}
		});
		
		
		
		buttonPanel.add(exploreButton);
		
		JButton foundButton = new JButton("Found City");
		foundButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				foundClicked = true;
				exploreClicked = false;
				moving = false;
				currentMessage = "Pick a Location";
			}
			
		});
		
		JButton pause = new JButton("pause");
		pause.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				paused = !paused;
				if(paused)
					pause.setText("unpause");
				else
					pause.setText("pause");
			}
			
		});
		
		JButton moveButton = new JButton("Move");
		moveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showMoveMenu();
			}
		});
		
		
		buttonPanel.add(foundButton);
		buttonPanel.add(pause);
		buttonPanel.add(moveButton);
	}
	
	private void setupMoveButtonPanel(){
		moveButtonPanel = new JPanel();
		JButton positionMoveButton = new JButton("Move to Position");
		positionMoveButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e){
				moving = true;
				exploreClicked = false;
				foundClicked = false;
				currentMessage = "Click where you want to move on the map.";
			}
			
		});
		
		JButton cancelMoveButton = new JButton("Cancel");
		cancelMoveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				currentMessage = "";
				uiPanel.remove(moveButtonPanel);
				uiPanel.add(buttonPanel);
				moving = false;
			}
		});
		
		moveButtonPanel.add(positionMoveButton);
		moveButtonPanel.add(cancelMoveButton);

		moveButtonPanel.setLayout(new BoxLayout(moveButtonPanel, BoxLayout.PAGE_AXIS));
	}
	
	private void setup(String name){
		setSize(1200, 600);
		
		lastUpdate = System.currentTimeMillis();
		gameMap = new Map();
		pc = new Player(name, gameMap);
		players.add(pc);
//		players.add(new ComputerPlayer("ROBOT", gameMap));
		gameMap.player = pc;

		setupMoveButtonPanel();
		setupButtonPanel();
		uiPanel.add(createMessageBox());
		uiPanel.add(buttonPanel);
		setupMouseListener();
		
		JPanel container = new JPanel();
		container.setSize(700,700);
		container.add(gameMap);
		container.add(uiPanel);
		BoxLayout b = new BoxLayout(container, BoxLayout.LINE_AXIS);
		container.setLayout(b);
		container.setBackground(Color.YELLOW);
		add(container);
	}
	
	private JPanel createMessageBox(){
		messages = new JEditorPane();
		messages.setEditable(false);
		messages.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
		messages.setMargin(new Insets(50,50,50,50));
		messages.setForeground(Color.BLUE);
		messages.setBounds(0,50,250, 450);
		messages.setBackground(Color.WHITE);
		messages.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		JPanel messagesHolder = new JPanel();
		messagesHolder.setLayout(null);
		messagesHolder.setPreferredSize(new Dimension(1,1));
		messagesHolder.add(messages);
		messagesHolder.setBounds(0,0,50,50);
		messagesHolder.setBackground(Color.YELLOW);
		return messagesHolder;
	}
	
	private void showMoveMenu(){
		currentMessage = "Move to Where?";
		uiPanel.remove(buttonPanel);
		uiPanel.add(moveButtonPanel);
		
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
//				currentMessage = c.toString();	
				showCity(c);
			}
			
		});
		
		JButton moveToCityButton = new JButton(cityName);
		moveToCityButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				pc.setLocation(c);
			}
			
		});
		
		buttonPanel.add(cityButton);
		moveButtonPanel.add(moveToCityButton);
		currentMessage = cityName + " founded.";
		repaint();
	}
	
	private void showCity(City c) {
		CityPanel cpan = new CityPanel(c);
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				uiPanel.remove(cpan);
				uiPanel.add(buttonPanel);
			}
			
		});
		cpan.add(back);
		uiPanel.remove(buttonPanel);
		uiPanel.add(cpan);
	}
	

	private void explore(int i, int j) {
		if(pc.canExplore()){
			pc.explore(new Point(i,j));
			currentMessage = "Explorer Sent.";
			exploreClicked = false;
		} else{
			currentMessage = "No Explorers Left";
		}
	}
	
	private String showMenu(){
		menu = 0;
		JPanel container = setupMenu();
		add(container);
		setSize(500, 500);
		while(menu != 1){
			repaint();
			container.repaint();
			if(menu == 2){
				container.removeAll();
				JButton backButton = new JButton("Back");
				backButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						menu = 3;
					}
				});
				JPanel instructions = createMessageBox();
				messages.setText("Colonialism!\n\nExplore the map and found cities to \nmake money!");
				container.add(backButton);
				container.add(instructions);
				container.validate();
				menu = 0;
			}
			if(menu == 3){
				remove(container);
				container = setupMenu();
				add(container);
				validate();
				menu = 0;
			}
		}
		
		String name = JOptionPane.showInputDialog("Enter Name");
		if(name == null)
			name = "Jack Delia";
		
		remove(container);
		return name;
	}
	
	private JPanel setupMenu(){
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		JButton startButton = new JButton("Start!");
		startButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				menu = 1;
			}
			
		});
		
		JButton instructionsButton = new JButton("Instructions");
		instructionsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				menu = 2;
			}
		});
		
		JButton exitButton = new JButton("Quit");
		exitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
		container.add(startButton);
		container.add(instructionsButton);
		container.add(exitButton);
		container.setBackground(Color.YELLOW);
		return container;
	}

	public void run() {	
		setup(showMenu());
		while (true){
			repaint();
			gameMap.repaint();
			if(paused)
				lastUpdate = System.currentTimeMillis();
			if(System.currentTimeMillis() - lastUpdate > 500){
				day++;
				lastUpdate = System.currentTimeMillis();
				String explorerStati = "";
				for(Explorer e: pc.getExplorers()){
					explorerStati += e.getName();
					if(e.isExploring())
						explorerStati += ": Exploring\n";
					else
						explorerStati += ": Free\n";
				}
				messages.setText(pc.getName() + "\nDay " + day + "\n" + "Money: " + 
				(int)pc.getMoney() + "G\n" + "Explorers:\n" +  
						explorerStati + currentMessage);
				for(Player p: players){
					p.update(1);
				}
			}
		}
	}

}
