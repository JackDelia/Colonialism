import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//A city is created at certain coordinates on the map.
public class City {

	public String name;
	public int cityId;
	public int xpos;
	public int ypos;
	public int size = 50;
	public double funding = 1;
	public Player controller;
	public boolean discovered = false;
	public int terrain = 5;
	public boolean coastal;
	public ArrayList<Soldier> garrison = new ArrayList<Soldier>();
	public ArrayList<Ship> fleet = new ArrayList<Ship>();
	public Map map;
	public ArrayList<String> availableResources = new ArrayList<String>();
	public HashMap<String, Double> stockpile = new HashMap<String, Double>();
	//0: stockpiled
	//1: sent back %
	//2: sold %
	//3-x: transfer to city x %
	public HashMap<String, ArrayList<Double>> instructions = new HashMap<String, ArrayList<Double>>();
	public HashMap<String, Double> production = new HashMap<String, Double>();
	
	public City(String name, int xpos, int ypos, Player controller, Map map) {
		this.name = name;
		this.xpos = xpos;
		this.ypos = ypos;
		this.controller = controller;
		this.map = map;
		this.cityId = controller.cities.size();
		this.terrain = map.getTerrain(xpos, ypos);
		for(int i = -2; i<=2; i++){
			if((map.getTerrain(xpos+i, ypos+i) == 0) 
					|| (map.getTerrain(xpos+i, ypos) == 0)
					|| (map.getTerrain(xpos, ypos+i) == 0))
				this.coastal = true;
		}
		map.cities.add(this);
		this.availableResources = map.getNearbyResources(xpos,ypos);
		if(coastal)
			this.availableResources.add("fish");
	}
	
	public double getProductionPower(){
		return funding*(size/10);
	}
	
	public void update(int days){
		for (int i = 0; i < days; i++){
			int add = (int)(funding * 100.0/size); 
			if(add > 10)
				size+= 10;
			else 
				size+= add;
			controller.money-=funding;
			}
		for(java.util.Map.Entry<String, Double> entry : production.entrySet()){
			String k = entry.getKey();
			stockpile.put(k, 
						(stockpile.get(k) + (entry.getValue()/100.0)*days*getProductionPower()));	
			stockpile.put(k, (entry.getValue()/100.0)*days*getProductionPower());
			if(stockpile.get(k) > instructions.get(k).get(0)){
				double excess = stockpile.get(k)-instructions.get(k).get(0);
				
				stockpile.put(k, stockpile.get(k)-(excess*(instructions.get(k).get(1)/100)));
				controller.influence+= excess*(instructions.get(k).get(1)/100);
		
				stockpile.put(k, stockpile.get(k)-(excess*(instructions.get(k).get(2)/100)));
				controller.money+= (excess*instructions.get(k).get(2))*map.prices.get(k);
				
				for(int i = 3; i<instructions.get(k).size(); i++){
					stockpile.put(k, stockpile.get(k)-(excess*(instructions.get(k).get(i)/100)));
					controller.cities.get(i-3).stockpile.put(k, 
							controller.cities.get(i-3).stockpile.get(k)+(excess*(instructions.get(k).get(i)/100)));
				}
			}
		}
		
				
		if(size >= 100*(production.size()+1)){
			String r = availableResources.get(((int)(Math.random()*100))%availableResources.size());
			production.put(r, 0.0);
			stockpile.put(r, 0.0);
			ArrayList<Double> inst = new ArrayList<Double>();
			inst.add(0, 20.0);
			inst.add(1, 50.0);
			inst.add(2,50.0);
			instructions.put(r, inst);
			
			balanceProduction();
		}
	}
	
	public void balanceProduction() {
		for(java.util.Map.Entry<String, Double> e : production.entrySet()){
			production.put(e.getKey(), 100.0/production.size());
		}
	}
	public void balanceProduction(String s, double d){
		if(d>100){
			System.out.println("invalid");
			return;
		}
		double increase = d-production.get(s);
		for(java.util.Map.Entry<String, Double> e : production.entrySet()){
			production.put(e.getKey(), e.getValue()-(increase/(production.size()-1)));
		}
		production.put(s, d);
	}
	
	public int getTotalGarrison(){
		int totalGarrison = 0;
		for(int i = 0; i<garrison.size(); i++)
			totalGarrison += garrison.get(i).number;
		return totalGarrison;
	}
	

	public String toString(){
		String ret = "City Name: " + name + "\nPopulation: " + size + "\nfunding:  " + funding + 
				"\ngarrison: " + getTotalGarrison() + "\nresources:" + production.size() + "\n";
		for(java.util.Map.Entry<String, Double> e : production.entrySet()){
			ret+= "\t" + e.getKey() + " " + e.getValue().intValue() + "%\n";
		}
		ret+= "stockpiled:\n";
		for(java.util.Map.Entry<String, Double> e : stockpile.entrySet()){
			ret+= "\t" + e.getKey() + " " + e.getValue() + "\n";
		}
		return ret;
	}

}
