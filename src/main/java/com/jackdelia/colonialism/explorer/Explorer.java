package com.jackdelia.colonialism.explorer;

import com.jackdelia.colonialism.currency.Funding;
import com.jackdelia.colonialism.knowledge.Knowledge;
import com.jackdelia.colonialism.location.Location;
import com.jackdelia.colonialism.location.LocationEquality;
import com.jackdelia.colonialism.map.Map;
import com.jackdelia.colonialism.math.RandomNumberGenerator;

import java.awt.Point;
import java.util.stream.IntStream;

public class Explorer {
	private static final String[] FIRST_NAMES = new String[]{"Jack", "Devin", "Brandon", "James", "Kenneth", "Allen", "Joeseph", "Jonathon", "George", "Thomas", "Christopher"};
	private static final String[] LAST_NAMES = new String[]{"Delia", "Smith", "Jones", "Joestar", "Mitchell", "Davidson", "Johnson", "Jefferson", "Washington", "Colombus"};

	private static final int DEFAULT_SPEED = 4;
	private static final int DEFAULT_RANGE = 25;
	private static final int DEFAULT_VISIBILITY = 3;
	private static final int INITIAL_TRAVELED = 0;

	private String name;
	private Knowledge knowledge;
	private int vision;

	private Location location;
    private Location target;
    private Location origin;

    private boolean exploring;
    private int travelled;
    private int range;

	private Funding financing;


	private Explorer() {
        this.financing = new Funding();
        this.exploring = false;
        this.travelled = DEFAULT_RANGE;
        this.range = INITIAL_TRAVELED;
        this.vision = DEFAULT_VISIBILITY;
        this.knowledge = new Knowledge();
    }

    /**
     * Factory Method to handle creating new Explorer instances
     * @param start the Starting Position for the new Explorer
     * @return a constructed Explorer instance
     */
	public static Explorer create(Point start) {
	    Explorer constructedExplorer = new Explorer();

        constructedExplorer.setName(String.format("%s %s",
                FIRST_NAMES[(int) (RandomNumberGenerator.generate() * FIRST_NAMES.length)],
                LAST_NAMES[(int) (RandomNumberGenerator.generate() * LAST_NAMES.length)])
        );

        constructedExplorer.setOrigin(new Location(start));
        constructedExplorer.setLocation(new Location((Point) start.clone()));

	    return constructedExplorer;
    }
	
	public void setOrigin(Point o) {
		this.origin.setPoint(o.getLocation());
		if(!this.exploring) {
			this.location.setPoint(this.origin.getPoint());
		}
	}
	
	public boolean isExploring(){
		return this.exploring;
	}

	public void setTarget(Point p){
		this.exploring = true;
		this.target = new Location(p.getLocation());
	}
	
	public void setLocation(Point p){
		this.location.setPoint(p.getLocation());
	}
	
	public int getFunding() {
		return this.financing.getCash();
	}

	public void incrementFunding(int amount) {
	    if(amount > 0) {
            this.financing.addCash(amount);
        } else if(amount < 0){
	        this.financing.removeCash(Math.abs(amount));
        }

		this.range = 25 + (this.financing.getCash() - 1) * 6;
		this.vision = 3 + (this.financing.getCash() - 1) / 5;
	}

	private void moveTowardTarget() {
		Location direction = new Location(new Point(0,0));

        LocationEquality locationEquality = this.location.compareTo(this.target);

        switch (locationEquality) {
            case DIFFERENT_VALUE_XY:
                // move diagonal
                if (RandomNumberGenerator.generate() > .5) {
                    direction.setXValue(
                            (this.target.getXValue() - this.location.getXValue())
                                    / Math.abs(this.target.getXValue() - this.location.getXValue())
                    );
                } else {
                    direction.setYValue(
                            (this.target.getYValue() - this.location.getYValue())
                                    / Math.abs(this.target.getYValue() - this.location.getYValue())
                    );
                }

                break;
            case DIFFERENT_VALUE_X:
                // move along x
                direction.setXValue(
                        (this.target.getXValue() - this.location.getXValue())
                                / Math.abs(this.target.getXValue() - this.location.getXValue())
                );

                break;
            case DIFFERENT_VALUE_Y:
                // move along y
                direction.setYValue(
                        (this.target.getYValue() - this.location.getYValue())
                                / Math.abs(this.target.getYValue() - this.location.getYValue())
                );

                break;
        }

		this.location.translate(direction);
		gainKnowledge();
	}
	
	private void gainKnowledge() {
		for(int i = (-1 * this.vision); i < this.vision; i++) {
			for(int j = (-1 * vision); j < this.vision; j++) {
				Point seen = (Point) this.location.getPoint().clone();
				seen.translate(i, j);
				if((seen.x >= 0) && (seen.x < Map.MAP_SIZE) && (seen.y >= 0) && (seen.y < Map.MAP_SIZE) && (seen.distance(this.location.getPoint()) <= this.vision)) {
                    this.knowledge.markSeen(seen);
                }
			}
		}
	}

	public boolean update() {
		if(this.exploring) {
			IntStream.range(0, DEFAULT_SPEED)
					.forEach(this::updateExplorerLocation);
			return true;
		}

		return false;
	}
	
	public Knowledge getKnowledge(){
		return this.knowledge;
	}
	
	private void resetKnowledge(){
		this.knowledge.resetKnowledge();
	}
	
	public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name);
        stringBuilder.append(": 	");
        if(this.exploring) {
			stringBuilder.append("Exploring");
		} else {
            stringBuilder.append(" Free");
		}
        stringBuilder.append(String.format("\t Funding: \t%d", this.financing.getCash()));
		return stringBuilder.toString();
	}


    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    private void updateExplorerLocation(int i) {
        if (this.target.equals(this.location) ||
                (this.travelled > this.range + (RandomNumberGenerator.generate() * (this.range / 2)) && !this.target.equals(this.origin))) {
            if (this.target.equals(this.origin)) {
                this.exploring = false;
                this.travelled = 0;
                resetKnowledge();
            } else {
                this.target.setPoint(this.origin.getPoint());
            }
        } else {
            moveTowardTarget();
            this.travelled++;
        }
    }
}
