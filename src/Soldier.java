import java.util.ArrayList;
import java.util.Random;


public class Soldier implements Explorer{
	public String name;
	public int xp;
	public double health = 100;
	public int level;
	public Player owner;
	public double equipment;
	public int type = 1;
	public City origin= null;
	public int xOrigin;
	public int yOrigin;
	public int xloc;
	public int yloc;
	public int number = 1;
	public ArrayList<Integer> knowledge = new ArrayList<Integer>();
	
	public Soldier(int xloc, int yloc, Player owner, int number){
		this.xloc = xloc;
		this.yloc = yloc;
		this.xOrigin = xloc;
		this.yOrigin = yloc;
		this.owner = owner;
		this.number = number;
		this.name = "Soldier: " + xloc+ ", " + yloc + ": " + number;
	}
	
	public Soldier(City origin, Player owner, int number){
		this.origin = origin;
		this.xloc = origin.lattitude;
		this.yloc = origin.longitude;
		this.xOrigin = xloc;
		this.yOrigin = yloc;
		this.owner = owner;
		this.number = number;
		this.name = "Soldier: " + origin.name + ": " + number;
	}
	
	@Override
	public ArrayList<Integer> getknowledge() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int explore(int targetx, int targety) {
		int dis = 0;
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
		
		while(r.nextInt(dis)< 10 && !(dirx == 0 && diry == 0)){
			xloc+= dirx;
			yloc+= diry;
			if(owner.map.mapTerrain[yloc][xloc] == 0)
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
		return dis*2;
	}

	@Override
	public void update() {
		
		
	}

}
