import java.awt.Point;
import java.util.HashSet;



public class Explorer{
	public static final String[] FIRSTNAMES = {"Jack", "Devin", "Brandon", "James", "Kenneth", "Allen", "Joeseph", "Jonathon", "George", "Thomas", "Christopher"};
	public static final String[] LASTNAMES = {"Delia", "Smith", "Jones", "Joestar", "Mitchell", "Davidson", "Johnson", "Jefferson", "Washington", "Colombus"};
	private String name;
	private HashSet<Point> knowledge = new HashSet<Point>();
	private int speed = 4;
	private int vision = 3;
	private Point location;
	private Point origin;
	private boolean exploring = false;
	private int travelled = 0;
	private int range = 25;
	private int funding = 1;
	private Point target;
	
	public Explorer(Point start){
		name = FIRSTNAMES[(int)(Math.random()*FIRSTNAMES.length)] + " " + 
				LASTNAMES[(int)(Math.random()*LASTNAMES.length)];
		location = (Point) start.clone();
		origin = start;
	}
	
	public void setOrigin(Point o){
		origin = o.getLocation();
		if(!exploring){
			location = o.getLocation();
		}
	}
	
	public boolean isExploring(){
		return exploring;
	}
	
	public String getName(){
		return name;
	}
	
	public void setTarget(Point p){
		exploring = true;
		target = p.getLocation();
	}
	
	public void setLocation(Point p){
		location = p.getLocation();
	}
	
	public int getFunding() {
		return funding;
	}

	public void incrementFunding(int amnt) {
		funding += amnt;
		if(funding <= 0)
			funding = 1;
		range = 25 + (funding-1)* 6;
		vision = 3 + (funding-1)/5;
	}

	private void moveTowardTarget(){
		int[] direction ={0,0};
		
		if(target.x != location.x && target.y != location.y){
			if(Math.random() > .5)
				direction[0] = (target.x-location.x)/Math.abs(target.x-location.x);
			else
				direction[1] = (target.y-location.y)/Math.abs(target.y-location.y);
		} else if(target.x != location.x)
			direction[0] = (target.x-location.x)/Math.abs(target.x-location.x);
		else
			direction[1] = (target.y-location.y)/Math.abs(target.y-location.y);
		
		location.translate(direction[0], direction[1]);
		gainKnowledge();
	}
	
	private void gainKnowledge(){
		for(int i = -vision; i < vision; i++){
			for(int j = -vision; j < vision; j++){
				Point seen = (Point)location.clone();
				seen.translate(i, j);
				if(seen.x >= 0 && seen.x < Map.MAPSIZE && seen.y >= 0 && seen.y < Map.MAPSIZE && seen.distance(location) <= vision)
					knowledge.add(seen);
			}
		}
	}
	
	
	public boolean update(){
		if(exploring){
			for(int i = 0; i < speed; i++){
				if(target.equals(location) || 
						(travelled > range + (Math.random()*(range/2)) && !target.equals(origin))){
					if(target.equals(origin)){
						exploring = false;
						travelled = 0;
						resetKnowledge();
					} else {
						target = origin;
					}
				} else{
					moveTowardTarget();
					travelled++;
				}
			}
			return true;
		}

		return false;
	}
	
	public HashSet<Point> getKnowledge(){
		return knowledge;
	}
	
	private void resetKnowledge(){
		knowledge.clear();
	}
	
	public String toString(){
		String ret = name + ": \t";
		if(exploring)
			ret+= "Exploring";
		else
			ret+= " Free";
		ret += "\t Funding: \t"+ funding;
		return ret;
	}
	
	
}
