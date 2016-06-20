import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;



public class Explorer{
	public static final String[] NAMES = {"Jack Delia", "D-Tru", "B-Cai", "Teja", "Kenneth"};
	
	private String name;
	private HashSet<Point> knowledge = new HashSet<Point>();
	private int speed = 1;
	private int vision = 3;
	private Point location;
	private Point origin;
	private boolean exploring = false;
	private int travelled = 0;
	private Point target;
	
	public Explorer(Point start){
		name = NAMES[(int)(Math.random()*NAMES.length)];
		location = start;
		origin = (Point) start.clone();
	}
	
	public void setOrigin(Point o){
		origin = o;
		if(!exploring){
			location = o;
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
		target = p;
	}
	
	public void setLocation(Point p){
		location = p;
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
				if(target.equals(location)){
					if(target.equals(origin)){
						exploring = false;
						return true;
					} else {
						target = origin;
					}
				} else{
					moveTowardTarget();
					return true;
				}
			}
		}

		return false;
	}
	
	public HashSet<Point> getKnowledge(){
		return knowledge;
	}
	
	
}
