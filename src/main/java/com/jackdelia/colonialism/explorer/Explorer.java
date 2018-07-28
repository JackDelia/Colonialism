package com.jackdelia.colonialism.explorer;

import com.jackdelia.colonialism.currency.Funding;
import com.jackdelia.colonialism.map.Map;
import com.jackdelia.colonialism.math.RandomNumberGenerator;

import java.awt.Point;
import java.util.HashSet;

public class Explorer{
	private static final String[] FIRST_NAMES = {"Jack", "Devin", "Brandon", "James", "Kenneth", "Allen", "Joeseph", "Jonathon", "George", "Thomas", "Christopher"};
	private static final String[] LAST_NAMES = {"Delia", "Smith", "Jones", "Joestar", "Mitchell", "Davidson", "Johnson", "Jefferson", "Washington", "Colombus"};

	private static final int DEFAULT_SPEED = 4;

	private String name;
	private HashSet<Point> knowledge = new HashSet<>();
	private int vision = 3;
	private Point location;
	private Point origin;
	private boolean exploring = false;
	private int travelled = 0;
	private int range = 25;
	private Point target;

	private Funding financing;

	public Explorer(Point start) {
		this.name = FIRST_NAMES[(int)(RandomNumberGenerator.generate() * FIRST_NAMES.length)] + " " +
				LAST_NAMES[(int)(RandomNumberGenerator.generate() * LAST_NAMES.length)];
		this.location = (Point) start.clone();
		this.origin = start;

		this.financing = new Funding();
	}
	
	public void setOrigin(Point o){
		this.origin = o.getLocation();
		if(!this.exploring){
			this.location = o.getLocation();
		}
	}
	
	public boolean isExploring(){
		return this.exploring;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setTarget(Point p){
		this.exploring = true;
		this.target = p.getLocation();
	}
	
	public void setLocation(Point p){
		this.location = p.getLocation();
	}
	
	public int getFunding() {
		return this.financing.getCash();
	}

	public void incrementFunding(int amount) {
	    if(amount > 0){
            this.financing.addCash(amount);
        } else if(amount < 0){
	        this.financing.removeCash(amount);
        }

		this.range = 25 + (this.financing.getCash() - 1) * 6;
		this.vision = 3 + (this.financing.getCash() - 1) / 5;
	}

	private void moveTowardTarget() {
		int[] direction = {0, 0};
		
		if(this.target.x != location.x && this.target.y != this.location.y) {

			if(RandomNumberGenerator.generate() > .5) {
                direction[0] = (this.target.x - this.location.x) / Math.abs(this.target.x - this.location.x);
            } else {
                direction[1] = (this.target.y - this.location.y) / Math.abs(this.target.y - this.location.y);
            }

		} else if(this.target.x != this.location.x) {
            direction[0] = (this.target.x - this.location.x) / Math.abs(this.target.x - this.location.x);
        } else {
            direction[1] = (this.target.y - this.location.y) / Math.abs(this.target.y - this.location.y);
        }

		this.location.translate(direction[0], direction[1]);
		gainKnowledge();
	}
	
	private void gainKnowledge(){
		for(int i = -vision; i < vision; i++){
			for(int j = -vision; j < vision; j++){
				Point seen = (Point)location.clone();
				seen.translate(i, j);
				if(seen.x >= 0 && seen.x < Map.MAP_SIZE && seen.y >= 0 && seen.y < Map.MAP_SIZE && seen.distance(location) <= vision)
					knowledge.add(seen);
			}
		}
	}
	
	
	public boolean update(){
		if(this.exploring){
            for(int i = 0; i < DEFAULT_SPEED; i++){
				if(this.target.equals(this.location) ||
						(this.travelled > this.range + (RandomNumberGenerator.generate() * (this.range / 2)) && !this.target.equals(this.origin))){
					if(this.target.equals(this.origin)) {
						this.exploring = false;
						this.travelled = 0;
						resetKnowledge();
					} else {
						this.target = this.origin;
					}
				} else{
					moveTowardTarget();
					this.travelled++;
				}
			}
			return true;
		}

		return false;
	}
	
	public HashSet<Point> getKnowledge(){
		return this.knowledge;
	}
	
	private void resetKnowledge(){
		this.knowledge.clear();
	}
	
	public String toString(){
		String ret = this.name + ": \t";
		if(this.exploring) {
			ret+= "Exploring";
		} else {
			ret+= " Free";
		}
		ret += "\t Funding: \t"+ this.financing.getCash();
		return ret;
	}
	
}
