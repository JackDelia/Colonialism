package com.jackdelia.colonialism.city;

import com.jackdelia.colonialism.map.Resource;
import com.jackdelia.colonialism.map.Terrain;
import com.jackdelia.colonialism.Game;
import com.jackdelia.colonialism.map.Map;
import com.jackdelia.colonialism.player.Player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

//A city is created at certain coordinates on the map.
public class City {

	private String name;
	private int cityId;
	private Point position;
	private int size = 50;
	private double funding = 1;
	private Player controller;
	private Terrain terrain = Terrain.PLAINS;
	private boolean coastal;
	private Map map;
	private int soldiers = 0;
	private ArrayList<Resource> availableResources = new ArrayList<Resource>();
	private HashMap<Resource, Double> stockpile = new HashMap<Resource, Double>();
	private HashMap<Resource, HashMap<String, Double>> instructions = new HashMap<Resource, HashMap<String, Double>>();
	private HashMap<Resource, Double> production = new HashMap<Resource, Double>();
	private HashMap<Resource, HashMap<City, Double>> exports = new HashMap<Resource, HashMap<City, Double>>();
	
	private static final HashMap<String,Double> DEFAULT_INSTRUCTIONS = new HashMap<String, Double>();
	static{
		DEFAULT_INSTRUCTIONS.put("stockpile", 10.0);
		DEFAULT_INSTRUCTIONS.put("return", 0.0);
		DEFAULT_INSTRUCTIONS.put("sell", 100.0);
		DEFAULT_INSTRUCTIONS.put("export", 0.0);
		
	}
	
	public City(String name, int xpos, int ypos, Player controller, Map map) {
		if(name.equals(""))
			name = "City "+ controller.getCities().size();
		else
			this.name = name;
		this.position = new Point(xpos, ypos);
		this.controller = controller;
		this.map = map;
		this.cityId = controller.getCities().size();
		this.terrain = map.getTerrain(xpos, ypos);
		for(int i = -2; i<=2; i++){
			if((map.getTerrain(xpos+i, ypos+i) == Terrain.OCEAN) 
					|| (map.getTerrain(xpos+i, ypos) == Terrain.OCEAN)
					|| (map.getTerrain(xpos, ypos+i) == Terrain.OCEAN))
				this.coastal = true;
		}
		map.cities.add(this);
		this.availableResources = map.getNearbyResources(xpos,ypos);
		if(coastal)
			this.availableResources.add(Resource.FISH);
	}
	
	public double getProductionPower(){
		double toolsMult = 1;
		if(stockpile.get(Resource.TOOLS) != null){
			toolsMult += stockpile.get(Resource.TOOLS)/5;
		}
		return (((int)funding/10)+1)*(size/10)*.01*toolsMult;
	}
	
	private double getProductionOf(Resource res){
		Double prod = production.get(res);
		if(prod == null)
			return 0;
		return getProductionPower()*(prod/100);
	}
	
	private void addPopulation(){
		
		int add = (int)(funding * 100.0/size); 
		add += getProductionOf(Resource.GRAIN);
		add += getProductionOf(Resource.MEAT)/2;
		switch(terrain){
			case MOUNTAINS:
				add /= 2;
			case DESERT:
				add /= 2;
			case FORREST:
				add -= 1;
		}
		
		
		double food = 0;
		Double grain = stockpile.get(Resource.GRAIN);
		Double meat = stockpile.get(Resource.MEAT);
		Double fish = stockpile.get(Resource.FISH);
		
		int foodTypes = 0;
		
		if(grain != null){
			food+= grain;
			foodTypes++;
		}
		if(meat != null){
			food+= meat;
			foodTypes++;
		}
		if(fish != null){
			food+= fish;
			foodTypes++;
		}
		
		double foodMult = Math.max(.5, 10*food/size);
		if(foodTypes != 0){
			incrementStockpile(Resource.GRAIN, -size/(1000*foodTypes));
			incrementStockpile(Resource.MEAT, -size/(1000*foodTypes));
			incrementStockpile(Resource.FISH, -size/(1000*foodTypes));
		}
		add *= foodMult;
		
		if(add > 10)
			size+= 10;
		else if(add <= 0 && Math.random() > .75)
			size += 1;
		else
			size+= add;
		controller.incrementMoney(-funding);
	}
	
	private void addToStockpile(Resource k, int days){
		double produce = production.get(k);
		double stockpiled = stockpile.get(k);
		
		double baseAmount = ((produce/100.0)*days*getProductionPower());
		if(Game.ADVANCED.get(k) != null){
			Resource baseRes = Game.ADVANCED.get(k);
			if(stockpile.get(baseRes) < baseAmount)
				baseAmount = stockpile.get(baseRes);
			stockpile.put(baseRes, stockpile.get(baseRes)-baseAmount);
		}
		
		stockpile.put(k, 
					(stockpiled + baseAmount));	
		
	}
	
	private void export(Resource res, double excess, int days){
		for(java.util.Map.Entry<City, Double> export : exports.get(res).entrySet()){
			City city = export.getKey();
			double percent = export.getValue();
			
			double toExport = excess*(percent/100);
			incrementStockpile(res, -toExport);
			city.incrementStockpile(res, toExport);
			
		}
	}
	
	public void update(int days){
		if(controller.getMoney() < funding)
			funding = controller.getMoney();
		
		for (int i = 0; i < days; i++){
			addPopulation();
		}
		
		for(java.util.Map.Entry<Resource, Double> entry : production.entrySet()){
			Resource k = entry.getKey();
			if(k == Resource.SOLDIERS){
				int base = (int)((entry.getValue()/100.0)*days*getProductionPower());
				double weapons = stockpile.get(Resource.WEAPONS);
				if(base > size-50 || base > weapons);
					base = Math.min(size-50, (int) weapons);
				size -= base;
				stockpile.put(Resource.WEAPONS, stockpile.get(Resource.WEAPONS)-base);
				soldiers += base;
			}else{
				HashMap<String, Double> kInstr = instructions.get(k);
				double stockpiled = stockpile.get(k);
				
				addToStockpile(k, days);
				
				double toStockpile = kInstr.get("stockpile"); 
				if(stockpiled > toStockpile){
					double excess = stockpiled-toStockpile;
					
					double sendBack = kInstr.get("return");
					stockpile.put(k, stockpiled-(excess*(sendBack/100.0)));
					controller.addInfluence((int)(excess*(sendBack/100.0)));
			
					stockpile.put(k, stockpiled-(excess*(kInstr.get("sell")/100)));
					controller.incrementMoney((excess*kInstr.get("sell"))* Game.prices.get(k));
					
				}
			}
		}
		
				
		if(size >= 100*(production.size()+1)){
			Resource r = availableResources.get(((int)(Math.random()*100))%availableResources.size());
			if(production.get(r) != null){
				ArrayList<Resource> adv = advancedResources();
				if(adv.size() > 0)
					r = adv.get(((int)(Math.random()*100))%adv.size());
			}
			
			if(production.get(r) != null)
				return;
			
			production.put(r, 0.0);
			if(!(r == Resource.SOLDIERS)){
				if(production.size() == 1)
					production.put(r, 100.0);
				stockpile.put(r, 0.0);
			}
			HashMap<String, Double> inst = new HashMap<String, Double>();
			inst.put("stockpile", 10.0);
			inst.put("return", 0.0);
			inst.put("sell", 100.0);
			inst.put("export", 0.0);
			instructions.put(r, inst);
			
//			balanceProduction();
		}
	}
	
	private ArrayList<Resource> advancedResources() {
		ArrayList<Resource> res = new ArrayList<Resource>();
		for(java.util.Map.Entry<Resource,Resource> e: Game.ADVANCED.entrySet()){
			if(stockpile.get(e.getValue()) != null && stockpile.get(e.getValue()) > 0)
				res.add(e.getKey());
		}		
		return res;
	}

	public void balanceProduction() {
		for(java.util.Map.Entry<Resource, Double> e : production.entrySet()){
			production.put(e.getKey(), 100.0/production.size());
		}
	}
	public void balanceProduction(Resource s, double d){
		if(d>100 || d < 0){
			return;
		}
		double increase = d-production.get(s);
		double distribute = increase/(production.size()-1);
		ArrayList<Resource> ignore = new ArrayList<Resource>();
		ignore.add(s);
		for(java.util.Map.Entry<Resource, Double> e : production.entrySet()){
			Resource key = e.getKey();
			double val = e.getValue();
				if(val < distribute && !key.equals(s)){
					increase -= val;
					ignore.add(key);
					production.put(key, 0.0);
				}
		}
		
		distribute = increase/(production.size()-ignore.size());
		
		for(java.util.Map.Entry<Resource, Double> e : production.entrySet()){
			if(!ignore.contains(e.getKey()))
				production.put(e.getKey(), e.getValue()-distribute);
		}
		production.put(s, d);
	}
	
	public void incrementProduction(Resource s, double d){
		balanceProduction(s, production.get(s)+d);
	}
	

//	public String toString(){
//		String ret = "City Name: " + name + "\nPopulation: " + size + "\nfunding:  " + funding + 
//				"\nproduction:" + "\n";
//		for(java.util.Map.Entry<String, Double> e : production.entrySet()){
//			ret+= "\t" + e.getKey() + " " + e.getValue().intValue() + "%\n";
//		}
//		ret+= "stockpiled:\n";
//		for(java.util.Map.Entry<String, Double> e : stockpile.entrySet()){
//			ret+= "\t" + e.getKey() + " " + e.getValue().intValue() + "\n";
//		}
//		return ret;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCityId() {
		return cityId;
	}

	public Point getPosition() {
		return position;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public double getFunding() {
		return funding;
	}

	public void setFunding(double funding) {
		this.funding = funding;
	}
	
	public void incrementFunding(double add){
		this.funding += add;
		if(this.funding < 0)
			this.funding = 0;
	}

	public double getProduction(Resource type) {
		return production.get(type);
	}
	
	public ArrayList<Resource> getProductionTypes(){
		ArrayList<Resource> ret = new ArrayList<Resource>();
		for(java.util.Map.Entry<Resource, Double> e : production.entrySet()){
			ret.add(e.getKey());
		}
		return ret;
	}

	public Player getController() {
		return controller;
	}

	public void setController(Player controller) {
		this.controller = controller;
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
	
	
	public void incrementStockpile(Resource type, double amount){
		if(amount == 0)
			return;
		if(stockpile.get(type) == null){
			stockpile.put(type, 0.0);
			instructions.put(type, DEFAULT_INSTRUCTIONS);
		}
		double result = Math.max(0, stockpile.get(type)+amount);
		stockpile.put(type, result);
	}
	
	public ArrayList<Resource> getStockpileTypes(){
		ArrayList<Resource> ret = new ArrayList<Resource>();
		for(java.util.Map.Entry<Resource, Double> e : stockpile.entrySet()){
			ret.add(e.getKey());
		}
		return ret;
	}

	public void setInstruction(Resource type, String inst, double val) {
		this.instructions.get(type).put(inst, val);
	}
	
	
	public Double getInstruction(Resource type, String inst) {
		return instructions.get(type).get(inst);
	}

	public int getSoldiers() {
		return soldiers;
	}

	public void incrementSoldiers(int soldiers) {
		this.soldiers += soldiers;
	}

	
	public void addExport(City city, Resource resource,
			double percent) {
		if(exports.get(resource) == null)
			exports.put(resource, new HashMap<City, Double>());
		
		double exportDelta = percent;
		if(exports.get(resource).get(city) != null)
			exportDelta = percent-exports.get(resource).get(city);
		
		exports.get(resource).put(city, percent);
		
		double currentExport = instructions.get(resource).get("export");
		instructions.get(resource).put("export", exportDelta+currentExport);
		
		double currentSell = instructions.get(resource).get("sell");
		instructions.get(resource).put("sell", currentSell-exportDelta);
		System.out.println(city.getName() +" " + resource + " " + percent);
	}

	

}
