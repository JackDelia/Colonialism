import java.util.ArrayList;
import java.util.Random;

public class Ship implements Explorer{
	public String name;
	public int speed = 1;
	public int health = 100;
	public boolean exploring = false;
	public int targetx;
	public int targety;
	public int xloc;
	public int yloc;
	public int xOrigin;
	public int yOrigin;
	public int manCapacity = 5;
	public int resCapacity = 5;
	public Player owner;
	public City origin = null;
	public int number = 1;
	public ArrayList<Integer> knowledge = new ArrayList<Integer>();
	public ArrayList<Soldier> carrying;
	public Ship(int xloc, int yloc, Player owner, int number){
		this.xloc = xloc;
		this.yloc = yloc;
		this.xOrigin = xloc;
		this.yOrigin = yloc;
		this.owner = owner;
		this.number = number;
		this.name = "Ship: " + xloc + ", " + yloc + ": " + number;
	}
	public Ship(City origin, Player owner, int number){
		this.origin = origin;
		this.xloc = origin.lattitude;
		this.yloc = origin.longitude;
		this.xOrigin = xloc;
		this.yOrigin = yloc;
		this.owner = owner;
		this.number = number;
		this.name = "Ship: " + origin.name + ": " + number;
	}
	
	@Override
	public ArrayList<Integer> getknowledge() {
		// TODO Auto-generated method stub
		return knowledge;
	}
	
	//1 = e
	//2 = w
	//3 = n
	//4 = s
	public int explore(int targetx, int targety) {
		int dis = 0;
		owner.ships.remove(this);
		if(origin != null)
			origin.fleet.remove(this);
		Random r = new Random();
		int dirx = -1;
		if(targetx > xloc)
			dirx = 1;
		if(targetx == xloc)
			dirx = 0;
		int diry = -1;
		if(targety > yloc)
			diry = 1;
		if(targety == yloc)
			diry = 0;
		
		while(r.nextInt(dis+100)> -1 && !(dirx == 0 && diry == 0)){
			xloc+= dirx;
			yloc+= diry;
			if(owner.map.mapTerrain[yloc][xloc] != 0)
				break;
			dis++;
			knowledge.add(yloc*100+xloc);
			knowledge.add((yloc-1)*100+xloc);
			knowledge.add((yloc+1)*100+xloc);
			knowledge.add(yloc*100+(xloc-1));
			knowledge.add(yloc*100+(xloc+1));
			knowledge.add((yloc-1)*100+(xloc-1));
			knowledge.add((yloc-1)*100+(xloc+1));
			knowledge.add((yloc+1)*100+(xloc-1));
			knowledge.add((yloc+1)*100+(xloc+1));
			if(xloc == targetx)
				dirx = 0;
			if(yloc == targety)
				diry =0;
		}
		return dis*2/speed;
	}
	@Override
	public void update() {
		if (exploring){
			for(int i = 0; i<speed; i++){
				if(targetx > xloc)
					xloc++;
				if(targetx < xloc)
					xloc--;
				if(targety > yloc)
					yloc++;
				if(targety < yloc)
					yloc--;
				
				knowledge.add(yloc*100+xloc);
				knowledge.add((yloc-1)*100+xloc);
				knowledge.add((yloc+1)*100+xloc);
				knowledge.add(yloc*100+(xloc-1));
				knowledge.add(yloc*100+(xloc+1));
				knowledge.add((yloc-1)*100+(xloc-1));
				knowledge.add((yloc-1)*100+(xloc+1));
				knowledge.add((yloc+1)*100+(xloc-1));
				knowledge.add((yloc+1)*100+(xloc+1));
				if(owner.map.mapTerrain[yloc][xloc] != 0){
					exploring = false;
					break;
				}
			}
		}
		else if((origin!=null) && (xloc != origin.lattitude || yloc != origin.longitude)){
			for(int i = 0; i<speed; i++){
				if(origin.lattitude > xloc)
					xloc++;
				if(origin.lattitude < xloc)
					xloc--;
				if(origin.longitude > yloc)
					yloc++;
				if(origin.longitude < yloc)
					yloc--;
				if(xloc == origin.lattitude && yloc == origin.longitude){
					owner.gainExploreKnowledge(knowledge);
					knowledge = new ArrayList<Integer>();
					break;
				}
			}
		}
	}

}
