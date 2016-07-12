import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


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
	
	public static final HashMap<Resource,Resource> ADVANCED = new HashMap<Resource,Resource>();
	static{
		ADVANCED.put(Resource.WEAPONS, Resource.IRON);
		ADVANCED.put(Resource.SOLDIERS, Resource.WEAPONS);
		ADVANCED.put(Resource.TOOLS, Resource.STONE);
		ADVANCED.put(Resource.CLOTHING, Resource.COTTON);
		ADVANCED.put(Resource.JEWELRY, Resource.GOLD);
	}
	
	public static final HashMap<Resource, Double> prices = new HashMap<Resource, Double>();
	static {
		prices.put(Resource.COTTON, .09);
		prices.put(Resource.IRON, .1);
		prices.put(Resource.STONE, .08);
		prices.put(Resource.GOLD, 1.0);
		prices.put(Resource.JEWELRY, 1.2);
		prices.put(Resource.MEAT, .1);
		prices.put(Resource.FISH, .08);
		prices.put(Resource.GRAIN, .07);
		prices.put(Resource.WOOD, .1);
		prices.put(Resource.WEAPONS, .2);
		prices.put(Resource.CLOTHING, .15);
		prices.put(Resource.TOOLS, .18);
		prices.put(Resource.SOLDIERS, .4);
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
	
	public static double trim(double d){
		if(Math.abs(d-((int) d)) < .01)
			d = (int) d;
		return ((int)(d*10))/10.0;
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
				if(exploreClicked)
					setCursor(Cursor.CROSSHAIR_CURSOR);
				else if(foundClicked)
					setCursor(Cursor.HAND_CURSOR);
			}

			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.DEFAULT_CURSOR);
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
		JPanel basicsPanel = new BasicsPanel(pc);
		uiPanel.add(basicsPanel);
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
				messages.setText("Colonialism!\n\nExplore the map and found cities to \nmake money!\n\n"
						+ "TIPS:\n\n You lose if you run out of money, so be careful!\n\n"
						+ "Your explorers can be a bit lazy. You may have to move yourself a bit to get them to explore what you want\n\n"
						+ "You've gotta spend money to make money!");
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
		if(name == null || name.equals(""))
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
		while (pc.getMoney() >= 1){
			validate();
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
//				messages.setText(pc.getName() + "\nDay " + day + "\n" + "Money: " + 
//				(int)pc.getMoney() + "G\n" + "Explorers:\n" +  
//						explorerStati + currentMessage);
				for(Player p: players){
					p.update(1);
				}
			}
		}
		
		JOptionPane.showMessageDialog(null, "You ran out of money. Game Over.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dispose();
		new Game().run();
	}

}
