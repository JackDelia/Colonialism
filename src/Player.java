import java.util.ArrayList;

public class Player {

	public String name;
	public Map map;
	public double money = 0;
	public Player liege;
	public double influence = 50;
	public ArrayList<Player> vassals = new ArrayList<Player>();
	public ArrayList<Soldier> troops = new ArrayList<Soldier>();
	public ArrayList<Ship> ships = new ArrayList<Ship>();
	public ArrayList<City> cities = new ArrayList<City>();
	public City capitol;
	public boolean[][] visible = new boolean[Map.MAPSIZE][Map.MAPSIZE];
	public int xloc = Map.MAPSIZE-1;
	public int yloc = 0;
	public City location = null;
	public boolean inOldWorld = false;
	
	public Player(String name, Map map) {
		this.name = name;
		this.map = map;
		for(boolean[] b1 : visible)
			for(boolean b2 : b1)
				b2 = false;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j< 8; j++)
				if(i+j < 8)
					visible[Map.MAPSIZE-1-i][j] = true;
	}
	
	public void gainExploreKnowledge(ArrayList<Integer> knowledge){
		
	}
	
	public void foundCity(String name, int lattitude, int longitude){
		if(map.valid(lattitude,longitude) && visible[lattitude][longitude]){
			City n = new City(name, lattitude, longitude, this, map);
			cities.add(n);
			System.out.println("City " + name + " founded.");
			if(cities.size() == 1){
				capitol = n;
				for (int i = 0; i < troops.size(); i++)
					n.garrison.add(troops.get(i));
			}
		}
		else
			System.out.println("Not a Valid location.");
	}
	
	public void Update(int days){
		for(Player p: vassals){
			p.Update(days);
		}
		for(City c : cities){
			c.update(days);
		}
		
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
