package com.jackdelia.colonialism;

import com.jackdelia.colonialism.city.City;
import com.jackdelia.colonialism.input.PromptUser;
import com.jackdelia.colonialism.map.resource.Resource;
import com.jackdelia.colonialism.basics.BasicsPanel;
import com.jackdelia.colonialism.city.CityPanel;
import com.jackdelia.colonialism.map.Map;
import com.jackdelia.colonialism.player.Player;
import org.apache.commons.lang3.StringUtils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
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


public class Game extends JFrame{
	private boolean exploreClicked;
	private boolean foundClicked;
	private boolean moving;
	private boolean paused;
	private int menu;
	private Player pc;
	private ArrayList<Player> players;
	private Map gameMap;
	private JPanel buttonPanel;
	private JPanel moveButtonPanel;
	private JPanel uiPanel;
	private JEditorPane messages;
	private String currentMessage = "";
	private int day;
	private long lastUpdate;
	
	public static final HashMap<Resource, Resource> ADVANCED;

	static {
		ADVANCED = new HashMap<>();
		ADVANCED.put(Resource.WEAPONS, Resource.IRON);
		ADVANCED.put(Resource.SOLDIERS, Resource.WEAPONS);
		ADVANCED.put(Resource.TOOLS, Resource.STONE);
		ADVANCED.put(Resource.CLOTHING, Resource.COTTON);
		ADVANCED.put(Resource.JEWELRY, Resource.GOLD);
	}
	
	private Game(){
		super("Colonialism!");

        this.exploreClicked = false;
        this.foundClicked = false;
        this.players = new ArrayList<>();
    }

	public static Game create() {
	    Game constructedGame = new Game();

        constructedGame.setSize(1200, 600);
        constructedGame.setVisible(true);

        constructedGame.getContentPane().setBackground(Color.YELLOW);

        // add exit event
        constructedGame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        // construct ui panel
        JPanel uiPanel = new JPanel();
        uiPanel.setBackground(Color.YELLOW);
        uiPanel.setPreferredSize(new Dimension(-250,-250));
        uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.X_AXIS));
        constructedGame.setUiPanel(uiPanel);

	    return constructedGame;
    }
	
	public static double trim(double d){
		if(Math.abs(d - ((int) d)) < .01) {
			d = (int) d;
		}
		return ((int)(d*10))/10.0;
	}
	
	
	private void setupMouseListener(){
		this.gameMap.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				if(exploreClicked){
					System.out.println(e.getX()/Map.PIXEL_STEP);
					explore(e.getX()/Map.PIXEL_STEP, e.getY()/Map.PIXEL_STEP);
				}
				else if(foundClicked)
					foundCity(e.getX()/Map.PIXEL_STEP, e.getY()/Map.PIXEL_STEP);
				else if(moving){
					pc.setLocation(null);
					pc.setPosition(new Point(e.getX()/Map.PIXEL_STEP, e.getY()/Map.PIXEL_STEP));
				}
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				if(exploreClicked) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                } else if(foundClicked) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
			}

			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			
		});
	}
	
	private void setupButtonPanel(){
		this.buttonPanel = new JPanel();
		this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, BoxLayout.PAGE_AXIS));
		this.buttonPanel.setAutoscrolls(true);
		
		JButton exploreButton = new JButton("Explore");
		exploreButton.addActionListener(e -> {
            if(exploreClicked){
                exploreClicked = false;
                currentMessage = "";
                return;
            }
            exploreClicked = true;
            foundClicked = false;
            moving = false;
            currentMessage = "Explore Where?";
        });

		this.buttonPanel.add(exploreButton);
		
		JButton foundButton = new JButton("Found City");
		foundButton.addActionListener(e -> {
            foundClicked = true;
            exploreClicked = false;
            moving = false;
            currentMessage = "Pick a Location";
        });
		
		final JButton pause = new JButton("pause");
		pause.addActionListener(e -> {
            paused = !paused;
            if(paused) {
                pause.setText("unpause");
            } else {
                pause.setText("pause");
            }
        });
		
		JButton moveButton = new JButton("Move");
		moveButton.addActionListener(e -> showMoveMenu());

		this.buttonPanel.add(foundButton);
		this.buttonPanel.add(pause);
		this.buttonPanel.add(moveButton);
	}
	
	private void setupMoveButtonPanel(){
		this.moveButtonPanel = new JPanel();
		JButton positionMoveButton = new JButton("Move to Position");
		positionMoveButton.addActionListener(e -> {
            moving = true;
            exploreClicked = false;
            foundClicked = false;
            currentMessage = "Click where you want to move on the map.";
        });
		
		JButton cancelMoveButton = new JButton("Cancel");
		cancelMoveButton.addActionListener(e -> {
            currentMessage = "";
            uiPanel.remove(moveButtonPanel);
            uiPanel.add(buttonPanel);
            moving = false;
        });

		this.moveButtonPanel.add(positionMoveButton);
		this.moveButtonPanel.add(cancelMoveButton);

		this.moveButtonPanel.setLayout(new BoxLayout(this.moveButtonPanel, BoxLayout.PAGE_AXIS));
	}
	
	private void setup(String name){
		setSize(1200, 600);

		this.lastUpdate = System.currentTimeMillis();
		this.gameMap = Map.create();
		this.pc = Player.create(name, gameMap);

		this.players.add(pc);
//		players.add(new ComputerPlayer("ROBOT", gameMap));
		this.gameMap.player = pc;

		setupMoveButtonPanel();
		setupButtonPanel();
		JPanel basicsPanel = BasicsPanel.create(pc, this);
		this.uiPanel.add(basicsPanel);
		this.uiPanel.add(this.buttonPanel);
		setupMouseListener();
		
		JPanel container = new JPanel();
		container.setSize(700,700);
		container.add(this.gameMap);
		container.add(this.uiPanel);
		BoxLayout b = new BoxLayout(container, BoxLayout.LINE_AXIS);
		container.setLayout(b);
		container.setBackground(Color.YELLOW);
		add(container);
	}
	
	private JPanel createMessageBox(){
		this.messages = new JEditorPane();
		this.messages.setEditable(false);
		this.messages.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
		this.messages.setMargin(new Insets(50,50,50,50));
		this.messages.setForeground(Color.BLUE);
		this.messages.setBounds(0,50,250, 450);
		this.messages.setBackground(Color.WHITE);
		this.messages.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		JPanel messagesHolder = new JPanel();
		messagesHolder.setLayout(null);
		messagesHolder.setPreferredSize(new Dimension(1,1));
		messagesHolder.add(messages);
		messagesHolder.setBounds(0,0,50,50);
		messagesHolder.setBackground(Color.YELLOW);
		return messagesHolder;
	}
	
	private void showMoveMenu(){
		this.currentMessage = "Move to Where?";
		this.uiPanel.remove(this.buttonPanel);
		this.uiPanel.add(this.moveButtonPanel);
	}

	private void foundCity(int x, int y) {
		boolean wasPaused = this.paused;
		this.paused = true;
		String cityName = PromptUser.forText("Choose A City Name");
		this.paused = wasPaused;

		if(StringUtils.isEmpty(cityName)) {
			return;
		}

		final City c = this.pc.foundCity(cityName, x, y);
		if (c == null) {
			return;
		}

        this.foundClicked = false;
		JButton cityButton = new JButton(cityName);
		cityButton.addActionListener(e -> showCity(c));
		
		JButton moveToCityButton = new JButton(cityName);
		moveToCityButton.addActionListener(e -> pc.setLocation(c));

        this.buttonPanel.add(cityButton);
        this.moveButtonPanel.add(moveToCityButton);
        this.currentMessage = cityName + " founded.";
		repaint();
	}

	/**
	 * Displays the Sidebar for the given City
	 * @param city the City instance to display on the Panel
	 */
	private void showCity(City city) {
		final CityPanel citySidePanel = CityPanel.create(city);
		JButton back = new JButton("Back");
		back.addActionListener(e -> {
            uiPanel.remove(citySidePanel);
            uiPanel.add(buttonPanel);
        });
        citySidePanel.add(back);
        this.uiPanel.remove(buttonPanel);
        this.uiPanel.add(citySidePanel);
	}

	private void explore(int i, int j) {
		if(this.pc.canExplore()){
            this.pc.explore(new Point(i, j));
            this.currentMessage = "Explorer Sent.";
            this.exploreClicked = false;
		} else{
            this.currentMessage = "No Explorers Left";
		}
	}
	
	private String showMenu(){
        this.menu = 0;
		JPanel container = setupMenu();
		add(container);
		setSize(500, 500);
		while(this.menu != 1){
			repaint();
			container.repaint();
			if(this.menu == 2){
				container.removeAll();
				JButton backButton = new JButton("Back");
				backButton.addActionListener(e -> menu = 3);
				JPanel instructions = createMessageBox();
                this.messages.setText("Colonialism!\n\nExplore the map and found cities to \nmake money!\n\n" + "TIPS:\n\n You lose if you run out of money, so be careful!\n\n" + "Your explorers can be a bit lazy. You may have to move yourself a bit to get them to explore what you want\n\n" + "You've gotta spend money to make money!");
				container.add(backButton);
				container.add(instructions);
				container.validate();
                this.menu = 0;
			}
			if(this.menu == 3){
				remove(container);
				container = setupMenu();
				add(container);
				validate();
                this.menu = 0;
			}
		}
		
		String name = PromptUser.forText("Enter Name");

		if(StringUtils.isEmpty(name)) {
            name = "Jack Delia";
        }
		
		remove(container);
		return name;
	}
	
	private JPanel setupMenu(){
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		JButton startButton = new JButton("Start!");
		startButton.addActionListener(e -> menu = 1);
		
		JButton instructionsButton = new JButton("Instructions");
		instructionsButton.addActionListener(e -> menu = 2);
		
		JButton exitButton = new JButton("Quit");
		exitButton.addActionListener(e -> System.exit(0));
		
		container.add(startButton);
		container.add(instructionsButton);
		container.add(exitButton);
		container.setBackground(Color.YELLOW);
		return container;
	}

	public void run() {	
		setup(showMenu());
		while(this.pc.getMoney() >= 1) {
			validate();
			repaint();
            this.gameMap.repaint();

			if(this.paused) {
                this.lastUpdate = System.currentTimeMillis();
            }

			if(System.currentTimeMillis() - this.lastUpdate > 500) {
                this.day++;
                this.lastUpdate = System.currentTimeMillis();

				for(Player p: this.players) {
					p.update(1);
				}

			}
		}
		
		JOptionPane.showMessageDialog(null, "You ran out of money. Game Over.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dispose();
		Game.create().run();
	}
	
	public int getDay(){
		return this.day;
	}

    public boolean isExploreClicked() {
        return exploreClicked;
    }

    public void setExploreClicked(boolean exploreClicked) {
        this.exploreClicked = exploreClicked;
    }

    public boolean isFoundClicked() {
        return foundClicked;
    }

    public void setFoundClicked(boolean foundClicked) {
        this.foundClicked = foundClicked;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }

    public Player getPc() {
        return pc;
    }

    public void setPc(Player pc) {
        this.pc = pc;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Map getGameMap() {
        return gameMap;
    }

    public void setGameMap(Map gameMap) {
        this.gameMap = gameMap;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    public JPanel getMoveButtonPanel() {
        return moveButtonPanel;
    }

    public void setMoveButtonPanel(JPanel moveButtonPanel) {
        this.moveButtonPanel = moveButtonPanel;
    }

    public JPanel getUiPanel() {
        return uiPanel;
    }

    public void setUiPanel(JPanel uiPanel) {
        this.uiPanel = uiPanel;
    }

    public JEditorPane getMessages() {
        return messages;
    }

    public void setMessages(JEditorPane messages) {
        this.messages = messages;
    }

    public String getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(String currentMessage) {
        this.currentMessage = currentMessage;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
