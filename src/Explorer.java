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
	}
	
	public String getName(){
		return name;
	}
	
	private void moveTowardTarget(){
		int[] direction ={0,0};
		int xdir = (target.x-location.x)/Math.abs(target.x-location.x);
		int ydir = (target.y-location.y)/Math.abs(target.y-location.y);
		
		if(target.x != location.x && target.y != location.y){
			if(Math.random() > .5)
				direction[0] = xdir;
			else
				direction[1] = ydir;
		} else if(target.x != location.x)
			direction[0] = xdir;
		else
			direction[1] = ydir;
		
		location.translate(direction[0], direction[1]);
		gainKnowledge();
	}
	
	private void gainKnowledge(){
		for(int i = -vision; i < vision; i++){
			for(int j = -vision; j < vision; j++){
				Point seen = location;
				seen.translate(i, j);
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
				}
			}
		}
		return false;
	}
	
	public HashSet<Point> getKnowledge(){
		return knowledge;
	}
	
	
}
