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
	private ArrayList<String> availableResources = new ArrayList<String>();
	private HashMap<String, Double> stockpile = new HashMap<String, Double>();
	private HashMap<String, HashMap<String, Double>> instructions = new HashMap<String, HashMap<String, Double>>();
	private HashMap<String, Double> production = new HashMap<String, Double>();
	
	public City(String name, int xpos, int ypos, Player controller, Map map) {
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
			this.availableResources.add("fish");
	}
	
	public double getProductionPower(){
		return Game.trim((((int)funding/10)+1)*(size/10)*.01);
	}
	
	private double getProductionOf(String res){
		return Game.trim(getProductionPower()*(production.get(res)/100));
	}
	
	private void addPopulation(){
		
		int add = (int)(funding * 100.0/size); 
		add += getProductionOf("grain");
		add += getProductionOf("meat")/2;
		switch(terrain){
			case MOUNTAINS:
				add /= 2;
			case DESERT:
				add /= 2;
			case FORREST:
				add -= 1;
		}
		
		if(add > 10)
			size+= 10;
		else if(add == 0 && Math.random() > .75)
			size += 1;
		else
			size+= add;
		controller.incrementMoney(-funding);
	}
	
	private void addToStockpile(String k, int days){
		double produce = production.get(k);
		double stockpiled = stockpile.get(k);
		
		double baseAmount = ((produce/100.0)*days*getProductionPower());
		if(Game.ADVANCED.get(k) != null){
			String baseRes = Game.ADVANCED.get(k);
			if(stockpile.get(baseRes) < baseAmount)
				baseAmount = stockpile.get(baseRes);
			stockpile.put(baseRes, stockpile.get(baseRes)-baseAmount);
		}
		
		stockpile.put(k, 
					(stockpiled + baseAmount));	
		
	}
	
	public void update(int days){
		if(controller.getMoney() < funding)
			funding = controller.getMoney();
		
		for (int i = 0; i < days; i++){
			addPopulation();
		}
		
		for(java.util.Map.Entry<String, Double> entry : production.entrySet()){
			String k = entry.getKey();
			if(k.equals("soldiers")){
				int base = (int)((entry.getValue()/100.0)*days*getProductionPower());
				double weapons = stockpile.get("weapons");
				if(base > size-50 || base > weapons);
					base = Math.min(size-50, (int) weapons);
				size -= base;
				stockpile.put("weapons", stockpile.get("weapons")-base);
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
					controller.incrementMoney((excess*kInstr.get("sell"))*Game.prices.get(k));
					
				}
			}
		}
		
				
		if(size >= 100*(production.size()+1)){
			String r = availableResources.get(((int)(Math.random()*100))%availableResources.size());
			if(production.get(r) != null){
				ArrayList<String> adv = advancedResources();
				if(adv.size() > 0)
					r = adv.get(((int)(Math.random()*100))%adv.size());
			}
			
			if(production.get(r) != null)
				return;
			
			production.put(r, 0.0);
			if(!r.equals("soldiers")){
				if(production.size() == 1)
					production.put(r, 100.0);
				stockpile.put(r, 0.0);
			}
			HashMap<String, Double> inst = new HashMap<String, Double>();
			inst.put("stockpile", 10.0);
			inst.put("return", 0.0);
			inst.put("sell", 100.0);
			instructions.put(r, inst);
			
//			balanceProduction();
		}
	}
	
	private ArrayList<String> advancedResources() {
		ArrayList<String> res = new ArrayList<String>();
		if(stockpile.get("iron") != null && stockpile.get("iron") > 0)
			res.add("weapons");
		if(stockpile.get("cotton") != null && stockpile.get("cotton") > 0)
			res.add("clothing");
		if(stockpile.get("stone") != null && stockpile.get("stone") > 0)
			res.add("tools");
		if(stockpile.get("gold") != null && stockpile.get("gold") > 0)
			res.add("jewelry");
		if(stockpile.get("weapons") != null && stockpile.get("weapons") > 0)
			res.add("soldiers");
		
		return res;
	}

	public void balanceProduction() {
		for(java.util.Map.Entry<String, Double> e : production.entrySet()){
			production.put(e.getKey(), 100.0/production.size());
		}
	}
	public void balanceProduction(String s, double d){
		if(d>100 || d < 0){
			return;
		}
		double increase = d-production.get(s);
		double distribute = increase/(production.size()-1);
		ArrayList<String> ignore = new ArrayList<String>();
		ignore.add(s);
		for(java.util.Map.Entry<String, Double> e : production.entrySet()){
			String key = e.getKey();
			double val = e.getValue();
				if(val < distribute && !key.equals(s)){
					increase -= val;
					ignore.add(key);
					production.put(key, 0.0);
				}
		}
		
		distribute = increase/(production.size()-ignore.size());
		
		for(java.util.Map.Entry<String, Double> e : production.entrySet()){
			if(!ignore.contains(e.getKey()))
				production.put(e.getKey(), e.getValue()-distribute);
		}
		production.put(s, d);
	}
	
	public void incrementProduction(String s, double d){
		balanceProduction(s, production.get(s)+d);
	}
	

	public String toString(){
		String ret = "City Name: " + name + "\nPopulation: " + size + "\nfunding:  " + funding + 
				"\nproduction:" + "\n";
		for(java.util.Map.Entry<String, Double> e : production.entrySet()){
			ret+= "\t" + e.getKey() + " " + e.getValue().intValue() + "%\n";
		}
		ret+= "stockpiled:\n";
		for(java.util.Map.Entry<String, Double> e : stockpile.entrySet()){
			ret+= "\t" + e.getKey() + " " + e.getValue().intValue() + "\n";
		}
		return ret;
	}

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
		return Game.trim(funding);
	}

	public void setFunding(double funding) {
		this.funding = funding;
	}
	
	public void incrementFunding(double add){
		this.funding += add;
		if(this.funding < 0)
			this.funding = 0;
	}

	public double getProduction(String type) {
		return Game.trim(production.get(type));
	}
	
	public ArrayList<String> getProductionTypes(){
		ArrayList<String> ret = new ArrayList<String>();
		for(java.util.Map.Entry<String, Double> e : production.entrySet()){
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

	public double getStockpile(String type) {
		return Game.trim(stockpile.get(type));
	}
	
	public ArrayList<String> getStockpileTypes(){
		ArrayList<String> ret = new ArrayList<String>();
		for(java.util.Map.Entry<String, Double> e : stockpile.entrySet()){
			ret.add(e.getKey());
		}
		return ret;
	}

	public void setInstruction(String type, String inst, double val) {
		this.instructions.get(type).put(inst, val);
	}
	
	
	public Double getInstruction(String type, String inst) {
		return instructions.get(type).get(inst);
	}

	public int getSoldiers() {
		return soldiers;
	}

	public void incrementSoldiers(int soldiers) {
		this.soldiers += soldiers;
	}

	

}
