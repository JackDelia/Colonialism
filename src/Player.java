import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

public class Player {

	public String name;
	public Map map;
	public double money = 1000;
	public Player liege;
	public double influence = 50;
	public ArrayList<Player> vassals = new ArrayList<Player>();
	public ArrayList<City> cities = new ArrayList<City>();
	public ArrayList<Explorer> explorers = new ArrayList<Explorer>();
	public City capitol;
	public boolean[][] visible = new boolean[Map.MAPSIZE][Map.MAPSIZE];
	public int xloc = Map.MAPSIZE-1;
	public int yloc = 0;
	public City location = null;
	public boolean inOldWorld = false;
	public double exploreRange = 20;
	
	public Player(String name, Map map) {
		this.name = name;
		this.map = map;
		explorers.add(new Explorer(new Point(xloc, yloc)));
		for(boolean[] b1 : visible)
			for(boolean b2 : b1)
				b2 = false;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j< 8; j++)
				if(i+j < 8)
					visible[Map.MAPSIZE-1-i][j] = true;
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
				xloc = lattitude;
				yloc = longitude;
				capitol = n;
				this.location = n;
				for(Explorer e: explorers){
					e.setOrigin(new Point(xloc, yloc));
				}
			}
			return n;
		}
		else
			System.out.println("Not a Valid location.");
		return null;
	}
	
	public void Update(int days){
		for(Player p: vassals){
			p.Update(days);
		}
		for(City c : cities){
			c.update(days);
		}
		for(Explorer e : explorers){
			if(e.update())
				gainExploreKnowledge(e.getKnowledge());
		}
		
	}
	
	public boolean inRange(int x, int y)
	{
		double dis;
		if(cities.size() == 0){
			int dx = xloc - x;
			int dy = this.yloc - y;
			dis = Math.sqrt(dx*dx+dy*dy);
			System.out.println(dis);
			if(dis < exploreRange*2)
				return true;
		}
		for(City c: cities){
			int dx = c.xpos - x;
			int dy = c.ypos - y;
			dis = Math.sqrt(dx*dx+dy*dy);
			if(dis < exploreRange)
				return true;
		}
		return false;
	}
	
	public String toString(){
		String ret = "name: " + name + "\nMoney: " + money + "\nlocation: ";
		if(location!= null)
			ret+= location.name;
		else
			ret+=xloc + ", "+ yloc;		
		ret+= "\nCities:\n";
		for(City c : cities){
			ret += c.toString() + "\n";
		}
		return ret + "\n";
	}

}
