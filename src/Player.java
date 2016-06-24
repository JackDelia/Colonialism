import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

public class Player {

	private String name;
	protected Map map;
	private double money = 1000;
	private int influence = 50;
	private ArrayList<Player> vassals = new ArrayList<Player>();
	private ArrayList<City> cities = new ArrayList<City>();
	private ArrayList<Explorer> explorers = new ArrayList<Explorer>();
	private City capitol;
	private boolean[][] visible = new boolean[Map.MAPSIZE][Map.MAPSIZE];
	private Point position = new Point(Map.MAPSIZE-1, 0);
	private City location = null;
	
	public Player(String name, Map map) {
		this.name = name;
		this.map = map;
		explorers.add(new Explorer(position));
		for(boolean[] b1 : visible)
			for(boolean b2 : b1)
				b2 = false;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j< 8; j++)
				if(i+j < 8)
					visible[Map.MAPSIZE-1-i][j] = true;
	}
	
	public boolean canExplore(){
		for(Explorer e: explorers){
			if(!e.isExploring()){
				return true;
			}
		}
		return false;
	}
	
	public void explore(Point target){
		for(Explorer e: explorers){
			if(!e.isExploring()){
				e.setTarget(target);
				return;
			}
		}
	}
	
	public void gainExploreKnowledge(HashSet<Point> knowledge){
		for(Point p: knowledge)
			visible[p.x][p.y] = true;
	}
	
	public City foundCity(String name, int lattitude, int longitude){
		if(map.valid(lattitude,longitude) && visible[lattitude][longitude]){
			City n = new City(name, lattitude, longitude, this, map);
			cities.add(n);
			System.out.println("City " + name + " founded.");
			if(cities.size() == 1){
				position = new Point(lattitude, longitude);
				capitol = n;
				this.location = n;
				for(Explorer e: explorers){
					e.setOrigin(position);
				}
			}
			return n;
		}
		else
			System.out.println("Not a Valid location.");
		return null;
	}
	
	public void update(int days){
		for(City c : cities){
			c.update(days);
		}
		for(Explorer e : explorers){
			if(e.update())
				gainExploreKnowledge(e.getKnowledge());
		}
		
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public double getMoney() {
		return money;
	}

	public void incrementMoney(double money) {
		this.money += money;
	}
	

	public ArrayList<City> getCities() {
		return cities;
	}
	
	public void addCity(City c){
		cities.add(c);
	}

	public ArrayList<Explorer> getExplorers() {
		return explorers;
	}
	
	public void addExplorer(Explorer e){
		explorers.add(e);
	}
	
	public void addInfluence(int i){
		influence += i;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
		for(Explorer e: explorers){
			e.setOrigin(position);
			if(!e.isExploring()){
				e.setLocation(position);
			}
		}
	}

	public City getLocation() {
		return location;
	}

	public void setLocation(City location) {
		this.location = location;
		if(location != null)
			setPosition(new Point(location.xpos, location.ypos));
	}

	public String toString(){
		String ret = "name: " + name + "\nMoney: " + money + "\nlocation: ";
		if(location!= null)
			ret+= location.name;
		else
			ret+= position;		
		ret+= "\nCities:\n";
		for(City c : cities){
			ret += c.toString() + "\n";
		}
		return ret + "\n";
	}

	public boolean canSee(int i, int j) {
		return visible[i][j];
	}

}
