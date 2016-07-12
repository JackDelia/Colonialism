import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JPanel;



public class Map extends JPanel{

	public Terrain[][] mapTerrain = new Terrain[MAPSIZE][MAPSIZE];
	public Resource[][][] mapResources = new Resource[MAPSIZE][MAPSIZE][];
	public ArrayList<City> cities = new ArrayList<City>();
	public Random r = new Random();
	public static final int MAPSIZE = 100;
	public static final int PIXELSTEP = 6;
	public static final Resource[] NATURAL = {Resource.GOLD, Resource.STONE, Resource.IRON};
	public Player player;
	private int age = 0;
	
	private static final HashMap<Terrain, Color> colors = new HashMap<Terrain, Color>();
	static{
		colors.put(Terrain.OCEAN, Color.BLUE);
		colors.put(Terrain.PLAINS, new Color(255, 225, 79));
		colors.put(Terrain.HILLS, Color.ORANGE);
		colors.put(Terrain.MOUNTAINS, Color.BLACK);
		colors.put(Terrain.DESERT, Color.YELLOW);
		colors.put(Terrain.FORREST, Color.GREEN);
	}
	
	public Map() {
		//procedural generation possible later
		//or maybe just a set map
		formContinents();
		formTerrain();
		stockResources();
		this.setSize(500,500);
	}
	
	public void stockResources(){
		for(int i = 0; i < mapResources.length; i++){
			for(int j = 0; j < mapResources[i].length; j++){
				double value = Math.random();
				if(value > (7/8.0) && mapTerrain[i][j] != Terrain.MOUNTAINS){
					
					mapResources[i][j] = new Resource[3];
					mapResources[i][j][0] = NATURAL[r.nextInt(NATURAL.length)];
					mapResources[i][j][1] = NATURAL[r.nextInt(NATURAL.length)];
					while(mapResources[i][j][0] == mapResources[i][j][1])
						mapResources[i][j][1] = NATURAL[r.nextInt(NATURAL.length)];
					mapResources[i][j][2] = NATURAL[r.nextInt(NATURAL.length)];
					while(mapResources[i][j][0] == mapResources[i][j][2] || mapResources[i][j][1] == mapResources[i][j][2])
						mapResources[i][j][2] = NATURAL[r.nextInt(NATURAL.length)];
				}
				else if(value > (1/2.0)){
					System.out.println("2");
					mapResources[i][j] = new Resource[2];
					Resource res1 = NATURAL[r.nextInt(NATURAL.length)];
					Resource res2 = NATURAL[r.nextInt(NATURAL.length)];
					if(res1 != Resource.IRON && res2 != Resource.IRON){
						res1 = NATURAL[r.nextInt(NATURAL.length)];
						res2 = NATURAL[r.nextInt(NATURAL.length)];
					}
					mapResources[i][j][0] = res1;
					mapResources[i][j][1] = res2;
					while(mapResources[i][j][0] == mapResources[i][j][1])
						mapResources[i][j][1] = NATURAL[r.nextInt(NATURAL.length)];
				}
				else if(value>(1/10.0)){
					System.out.println("1");
					mapResources[i][j] = new Resource[1]; 
					Resource res = NATURAL[r.nextInt(NATURAL.length)];
					if(res != Resource.IRON)
						res = NATURAL[r.nextInt(NATURAL.length)];
					mapResources[i][j][0] = res;
				}
				else{
					if(mapTerrain[i][j] == Terrain.MOUNTAINS)
						mapResources[i][j] = new Resource[]{Resource.STONE};
					else
						mapResources[i][j] = new Resource[0];
				}
			}
		}
		
	}
	
	public void paintComponent(Graphics g){
		for(int i = 0; i< MAPSIZE; i++){
			for(int j = 0; j< MAPSIZE; j++){
				g.setColor(colors.get(mapTerrain[i][j]));
				if(player != null && !player.canSee(i,j) && !player.getName().equals("_—l"))
					g.setColor(Color.WHITE);
				if(player != null && player.getLocation() == null && player.getPosition().x == i && player.getPosition().y == j && !(age%7000 < 3500))
					g.setColor(Color.RED);
				g.fillRect(i*6, j*6, 6, 6);
			}
			
			for(City c : cities){
				if(!(player.getLocation() == c && age%7000 < 3500) && c.getController() == player || player.canSee(c.getPosition())){
					g.setColor(new Color(181,80,137));
					if(player.getLocation() == c)
						g.setColor(Color.red);
					int ovalSize = 7 + c.getSize()/2000;
					g.fillOval(c.getPosition().x*6 - ovalSize/2, c.getPosition().y * 6-ovalSize/2, ovalSize , ovalSize);
					g.drawString(c.getName(), c.getPosition().x * 6, c.getPosition().y * 6-10);
				}
			}
			age++;
		}
	}
	
	public void formTerrain(){
		for(int i = 2; i<=4; i++){
			Terrain t = Terrain.PLAINS;
			switch(i){
			case 2: t = Terrain.MOUNTAINS;
				break;
			case 3: t = Terrain.DESERT;
				break;
			case 4: t = Terrain.FORREST;
			
			}
			int count = r.nextInt((int) (Math.pow(MAPSIZE, 2)/1000))+18;
			System.out.println(i + "  " + count);
			while (count > 0){
				int x = r.nextInt(MAPSIZE-2)+1;
				int y = r.nextInt(MAPSIZE-2)+1;
				if(mapTerrain[x][y] == Terrain.PLAINS){
					mapTerrain[x][y] = t;
					int dirCount = 1;
					while(Math.random()<.75 && dirCount<x){
						if(mapTerrain[x-dirCount][y] == Terrain.PLAINS){
							mapTerrain[x-dirCount][y] = t;
							if(mapTerrain[x-dirCount][y+1] == Terrain.PLAINS && Math.random()>=.5)
								mapTerrain[x-dirCount][y+1] = t;
							if(mapTerrain[x-dirCount][y+1] == Terrain.PLAINS && Math.random()>=.5)
								mapTerrain[x-dirCount][y-1] = t;
						}
						dirCount++;
					}
					dirCount = 1;
					while(Math.random()<.75 && dirCount+x<MAPSIZE){
						if(mapTerrain[x+dirCount][y] == Terrain.PLAINS){
							mapTerrain[x+dirCount][y] = t;
							if(mapTerrain[x+dirCount][y+1] == Terrain.PLAINS && Math.random()>=.5)
								mapTerrain[x+dirCount][y+1] = t;
							if(mapTerrain[x+dirCount][y+1] == Terrain.PLAINS && Math.random()>=.5)
								mapTerrain[x+dirCount][y-1] = t;
						}
						dirCount++;
					}
					dirCount = 1;
					while(Math.random()<.75&& dirCount<y){
						if(mapTerrain[x][y-dirCount] == Terrain.PLAINS){
							mapTerrain[x][y-dirCount] = t;
							if(mapTerrain[x+1][y-dirCount] == Terrain.PLAINS && Math.random()>=.5)
								mapTerrain[x+1][y-dirCount] = t;
							if(mapTerrain[x-1][y-dirCount] == Terrain.PLAINS && Math.random()>=.5)
								mapTerrain[x-1][y-dirCount] = t;
						}
						dirCount++;
					}
					dirCount = 1;
					while(Math.random()<.75 && dirCount+y < MAPSIZE){
						if(mapTerrain[x][y+dirCount] == Terrain.PLAINS){
							mapTerrain[x][y+dirCount] = t;
							if(mapTerrain[x+1][y+dirCount] == Terrain.PLAINS && Math.random()>=.5)
								mapTerrain[x+1][y+dirCount] = t;
							if(mapTerrain[x-1][y+dirCount] == Terrain.PLAINS && Math.random()>=.5)
								mapTerrain[x-1][y+dirCount] = t;
						}
						dirCount++;
					}
					count--;
				}
			}
		}
		for(int w = 0; w< MAPSIZE; w++)
			for(int z = 0; z< MAPSIZE; z++)
				if(mapTerrain[w][z] == Terrain.PLAINS && Math.random()>.75)
					mapTerrain[w][z] = Terrain.HILLS;
		System.out.println("Terrain formed.");
		
	}
	
	public void formContinents(){
		int rt = r.nextInt(MAPSIZE-7);
		int lt = r.nextInt(MAPSIZE);
		int mass = 0;
		if(rt<lt){
			int temp = rt;
			rt = lt;
			lt = temp;
		}
		for(int i = 0; i< MAPSIZE; i++){
			for(int j = 0; j< MAPSIZE; j++){
				if(j<=lt || j>=rt)
					mapTerrain[j][i] = Terrain.OCEAN;
				else{
					mapTerrain[j][i] = Terrain.PLAINS;
					mass++;
				}
			}
			if(rt == lt){
				if(Math.random()>.85){
					rt+= r.nextInt(7);
					lt-= r.nextInt(7); 
				}
			}
			else{
				rt+= r.nextInt(15)-7;
				lt+= r.nextInt(15)-7;
				if(rt<lt)
					rt = lt;
			}
			if(rt>= MAPSIZE)
				rt = MAPSIZE-1;
			if(lt<0)
				lt = 0;
			
		}
		if(mass<5000){
			formContinents();
			return;
		}
		System.out.println("Continents formed.");
	}
	
	//0-ocean
	//1-hills
	//2-mountains
	//3-desert
	//4-woods
	//5-plains
	public Terrain getTerrain(int lattitude, int longitude){
		if(lattitude >= MAPSIZE || longitude >= MAPSIZE || lattitude < 0 || longitude < 0)
			return Terrain.INVALID;
		return mapTerrain[lattitude][longitude];
	}

	public ArrayList<Resource> getNearbyResources(int lattitude,
			int longitude) {
		
		ArrayList<Resource> ret = new ArrayList<Resource>();
		if(Math.random() > .5)
			ret.add(Resource.MEAT);
		
		for(Resource s: mapResources[lattitude][longitude]){
			if(!ret.contains(s))
				ret.add(s);
		}
		
		for(int i = -2; i<=2; i++){
			for(int j = -2; j<=2; j++){
				if(lattitude+i > 0 && longitude+j > 0 && lattitude+i < Map.MAPSIZE && longitude+j < Map.MAPSIZE){

					if(mapTerrain[lattitude+i][longitude+j] == Terrain.FORREST && !ret.contains(Resource.WOOD))
						ret.add(Resource.WOOD);
					if(mapTerrain[lattitude+1][longitude+1] == Terrain.DESERT && !ret.contains(Resource.COTTON))
						ret.add(Resource.COTTON);
				}
			}
		}
		
		if(Math.random() > .5 || ret.size() == 0)
			ret.add(Resource.GRAIN);
		System.out.println(ret.size());
		return ret;
	}

	public boolean valid(int xpos, int ypos) {
		if(mapTerrain[xpos][ypos] == Terrain.OCEAN)
			return false;
		for(City c : cities)
			if(c.getPosition().distance(new Point(xpos,ypos))<10)
				return false;
		return true;
	}
	

}
