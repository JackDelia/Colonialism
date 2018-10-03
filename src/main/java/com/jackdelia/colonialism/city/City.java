package com.jackdelia.colonialism.city;

import com.jackdelia.colonialism.map.resource.Resource;
import com.jackdelia.colonialism.map.terrain.Terrain;
import com.jackdelia.colonialism.Game;
import com.jackdelia.colonialism.map.Map;
import com.jackdelia.colonialism.math.RandomNumberGenerator;
import com.jackdelia.colonialism.player.Player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//A city is created at certain coordinates on the map.
public class City {

	private String name;
	private int cityId;
	private Point position;

	private Population population;

	private double funding = 1;
	private Player player;
	private Terrain terrain;
	private boolean coastal;
	private Map gameMap;
	private int soldiers = 0;
	private ArrayList<Resource> availableResources;
	private HashMap<Resource, Double> stockpile;
	private HashMap<Resource, HashMap<String, Double>> instructions;
	private HashMap<Resource, Double> production;
	private HashMap<Resource, HashMap<City, Double>> exports;
	
	private static final HashMap<String,Double> DEFAULT_INSTRUCTIONS = new HashMap<>();

	static{
		DEFAULT_INSTRUCTIONS.put("stockpile", 10.0);
		DEFAULT_INSTRUCTIONS.put("return", 0.0);
		DEFAULT_INSTRUCTIONS.put("sell", 100.0);
		DEFAULT_INSTRUCTIONS.put("export", 0.0);
		
	}

	/**
	 * @param name the name of the City
	 * @param xPosition the x coordinate of the city
	 * @param yPosition the y coordinate of the city
	 * @param player the player that owns this city
	 * @param map the game map
	 */
	public City(String name, int xPosition, int yPosition, Player player, Map map) {

        // initialize class attributes
        this.stockpile = new HashMap<>();
        this.instructions = new HashMap<>();
        this.production = new HashMap<>();
        this.exports = new HashMap<>();

        this.population = new Population();

		if(name.equals("")) {
			this.name = "City " + player.getCities().size();
		} else {
			this.name = name;
		}

		this.position = new Point(xPosition, yPosition);
		this.player = player;
		this.gameMap = map;
		this.cityId = player.getCities().size();
		this.terrain = map.getTerrain(xPosition, yPosition);


		// i = {-2, -1, 0, 1, 2}
		for(int i = -2; i <= 2; i++) {
			if((this.gameMap.getTerrain(xPosition + i, yPosition + i) == Terrain.OCEAN)
					|| (this.gameMap.getTerrain(xPosition + i, yPosition) == Terrain.OCEAN)
					|| (this.gameMap.getTerrain(xPosition, yPosition + i) == Terrain.OCEAN)) {
				this.coastal = true;
			}
		}

        this.gameMap.getEmpire().addCity(this);
		this.availableResources = this.gameMap.getNearbyResources(xPosition, yPosition);

		if(this.coastal) {
            this.availableResources.add(Resource.FISH);
        }

    }
	
	public double getProductionPower(){
		double toolsMult = 1;
		if(this.stockpile.get(Resource.TOOLS) != null){
			toolsMult += this.stockpile.get(Resource.TOOLS) / 5;
		}
		return (((int) this.funding / 10) + 1) * (this.population.getNumberOfPeople() / 10) * .01 * toolsMult;
	}
	
	private double getProductionOf(Resource res){
		Double prod = this.production.get(res);
		if(prod == null) {
            return 0;
        }
		return getProductionPower()*(prod/100);
	}
	
	private void addPopulation(){
		
		int increaseByAmount = (int)(this.funding * 100.0 / this.population.getNumberOfPeople());
		increaseByAmount += getProductionOf(Resource.GRAIN);
		increaseByAmount += getProductionOf(Resource.MEAT) / 2;
		switch(this.terrain) {
			case MOUNTAINS:
				increaseByAmount /= 2;
			case DESERT:
				increaseByAmount /= 2;
			case FORREST:
				increaseByAmount -= 1;
		}

		double food = 0;
		Double grain = this.stockpile.get(Resource.GRAIN);
		Double meat = this.stockpile.get(Resource.MEAT);
		Double fish = this.stockpile.get(Resource.FISH);
		
		int foodTypes = 0;
		
		if(grain != null) {
			food += grain;
			foodTypes++;
		}

		if(meat != null) {
			food += meat;
			foodTypes++;
		}

		if(fish != null) {
			food += fish;
			foodTypes++;
		}
		
		double foodMult = Math.max(.5, 10* food / this.population.getNumberOfPeople());

		if(foodTypes != 0) {
			incrementStockpile(Resource.GRAIN, - this.population.getNumberOfPeople() / (1000 * foodTypes));
			incrementStockpile(Resource.MEAT, - this.population.getNumberOfPeople() / (1000 * foodTypes));
			incrementStockpile(Resource.FISH, - this.population.getNumberOfPeople() / (1000 * foodTypes));
		}
		increaseByAmount *= foodMult;
		
		if(increaseByAmount > 10) {
            this.population.increasePopulation(10);
        } else if(increaseByAmount <= 0 && RandomNumberGenerator.generate() > .75) {
            this.population.increasePopulation(1);
        } else {
			if(increaseByAmount < 0) {
			    this.population.decreasePopulation(increaseByAmount);
            } else {
			    this.population.increasePopulation(increaseByAmount);
            }
        }

        this.player.decrementMoney(-1 * funding);
	}
	
	private void addToStockpile(Resource k, int days){
		double produce = this.production.get(k);
		double stockpiled = this.stockpile.get(k);
		
		double baseAmount = ((produce/100.0)*days*getProductionPower());
		if(Game.ADVANCED.get(k) != null){
			Resource baseRes = Game.ADVANCED.get(k);
			if(this.stockpile.get(baseRes) < baseAmount) {
                baseAmount = this.stockpile.get(baseRes);
            }
            this.stockpile.put(baseRes, this.stockpile.get(baseRes)-baseAmount);
		}

        this.stockpile.put(k, (stockpiled + baseAmount));
		
	}
	
	private void export(Resource res, double excess, int days){
        this.exports.get(res).forEach((city, value) -> {
            double percent = value;

            double toExport = excess * (percent / 100);
            incrementStockpile(res, -toExport);
            city.incrementStockpile(res, toExport);

        });
	}
	
	public void update(int days) {
		if(this.player.getMoney() < this.funding) {
			this.funding = this.player.getMoney();
		}

        IntStream.range(0, days).forEachOrdered(i -> addPopulation());

        this.production.forEach((curProducingResource, value) -> {
            if (curProducingResource == Resource.SOLDIERS) {
                int base = (int) ((value / 100.0) * days * getProductionPower());
                double weapons = this.stockpile.get(Resource.WEAPONS);

                if (base > this.population.getNumberOfPeople() - 50 || base > weapons) {
                    base = Math.min(this.population.getNumberOfPeople() - 50, (int) weapons);
                }

                this.population.decreasePopulation(base);
                this.stockpile.put(Resource.WEAPONS, this.stockpile.get(Resource.WEAPONS) - base);
                this.soldiers += base;
            } else {
                HashMap<String, Double> kInstr = this.instructions.get(curProducingResource);
                double stockpiled = this.stockpile.get(curProducingResource);

                addToStockpile(curProducingResource, days);

                double toStockpile = kInstr.get("stockpile");
                if (stockpiled > toStockpile) {
                    double excess = stockpiled - toStockpile;

                    double sendBack = kInstr.get("return");
                    this.stockpile.put(curProducingResource, stockpiled - (excess * (sendBack / 100.0)));
                    this.player.addInfluence((int) (excess * (sendBack / 100.0)));

                    this.stockpile.put(curProducingResource, stockpiled - (excess * (kInstr.get("sell") / 100)));
                    this.player.incrementMoney((excess * kInstr.get("sell")) * curProducingResource.getPrice());

                }
            }
        });
		
				
		if(this.population.getNumberOfPeople() >= 100 * (this.production.size() + 1)) {
			Resource r = this.availableResources.get(((int)(RandomNumberGenerator.generate() * 100)) % this.availableResources.size());
			if(this.production.get(r) != null) {
				ArrayList<Resource> adv = getAdvancedResources();
				if(adv.size() > 0) {
					r = adv.get(((int)(RandomNumberGenerator.generate() * 100)) % adv.size());
				}
			}
			
			if(this.production.get(r) != null) {
                return;
            }

            this.production.put(r, 0.0);

			if(!(r == Resource.SOLDIERS)) {

				if(this.production.size() == 1) {
                    this.production.put(r, 100.0);
                }
                this.stockpile.put(r, 0.0);
			}

			HashMap<String, Double> inst = new HashMap<>();
			inst.put("stockpile", 10.0);
			inst.put("return", 0.0);
			inst.put("sell", 100.0);
			inst.put("export", 0.0);
            this.instructions.put(r, inst);
			
		}
	}
	
	private ArrayList<Resource> getAdvancedResources() {
		ArrayList<Resource> res = new ArrayList<>();
        Game.ADVANCED.forEach((key, value) -> {
            if (this.stockpile.get(value) != null && this.stockpile.get(value) > 0) {
                res.add(key);
            }
        });
		return res;
	}

	public void balanceProduction() {
        this.production.forEach((key, value) -> this.production.put(key, 100.0 / this.production.size()));
	}

	private void balanceProduction(Resource s, double d) {

	    // validate that param d is between 1 and 100
	    if(d > 100 || d < 0) {
			return;
		}

		double increase = d - this.production.get(s);
		double distribute = increase / (this.production.size() - 1);

		ArrayList<Resource> ignore = new ArrayList<>();
		ignore.add(s);
		for(Entry<Resource, Double> e : this.production.entrySet()) {
			Resource key = e.getKey();
			double val = e.getValue();
				if(val < distribute && !key.equals(s)) {
					increase -= val;
					ignore.add(key);
                    this.production.put(key, 0.0);
				}
		}
		
		distribute = increase / (this.production.size() - ignore.size());
		
		for(Entry<Resource, Double> e : this.production.entrySet()) {
			if(!ignore.contains(e.getKey())) {
                this.production.put(e.getKey(), e.getValue() - distribute);
            }
		}
        this.production.put(s, d);
	}
	
	public void incrementProduction(Resource s, double d){
		balanceProduction(s, this.production.get(s)+d);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCityId() {
		return this.cityId;
	}

	public Point getPosition() {
		return this.position;
	}

	public int getSize() {
		return this.population.getNumberOfPeople();
	}

	public double getFunding() {
		return this.funding;
	}

	public void setFunding(double funding) {
		this.funding = funding;
	}
	
	public void incrementFunding(double add) {
		this.funding += add;
		if(this.funding < 0) {
            this.funding = 0;
        }
	}

	public double getProduction(Resource type) {
		return this.production.get(type);
	}
	
	public ArrayList<Resource> getProductionTypes() {
		return this.production.entrySet().stream()
				.map(Entry::getKey)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public boolean isCoastal() {
		return coastal;
	}

	public double getStockpile(Resource type) {
		return stockpile.get(type);
	}
	
	
	private void incrementStockpile(Resource type, double amount) {

	    // input validation: verify that the amount != 0
	    if(amount == 0) {
            return;
        }

        // if the stockpile does not have the resource, then include it
		if(this.stockpile.get(type) == null) {
            this.stockpile.put(type, 0.0);
            this.instructions.put(type, DEFAULT_INSTRUCTIONS);
		}

		double result;
	    double stockpileAmount = this.stockpile.get(type) + amount;

	    // insert either 0 or the new amount
	    if(stockpileAmount > 0){
            result = stockpileAmount;
        } else {
	        result = 0;
        }

        this.stockpile.put(type, result);
	}
	
	public ArrayList<Resource> getStockpileTypes(){
        return this.stockpile.entrySet().stream()
                .map(Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
	}

	public void setInstruction(Resource type, String inst, double val) {
		this.instructions.get(type).put(inst, val);
	}
	
	
	public Double getInstruction(Resource type, String inst) {
		return this.instructions.get(type).get(inst);
	}

	public int getSoldiers() {
		return this.soldiers;
	}

	public void incrementSoldiers(int soldiers) {
		this.soldiers += soldiers;
	}

	public void addExport(City city, Resource resource, double percent) {

	    // insert resource, if doesn't already contain
        this.exports.computeIfAbsent(resource, k -> new HashMap<>());
		
		double exportDelta = percent;
		if(this.exports.get(resource).get(city) != null) {
            exportDelta = percent - this.exports.get(resource).get(city);
        }

        this.exports.get(resource).put(city, percent);
		
		double currentExport = this.instructions.get(resource).get("export");
        this.instructions.get(resource).put("export", exportDelta + currentExport);
		
		double currentSell = this.instructions.get(resource).get("sell");
        this.instructions.get(resource).put("sell", currentSell - exportDelta);
		System.out.println(city.getName() + " " + resource + " " + percent);
	}

}
