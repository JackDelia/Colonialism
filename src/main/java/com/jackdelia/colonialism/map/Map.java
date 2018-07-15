package com.jackdelia.colonialism.map;

import com.jackdelia.colonialism.city.City;
import com.jackdelia.colonialism.player.Player;

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
				if(player != null && !player.canSee(i,j))
					g.setColor(Color.WHITE);
				if(player != null && player.getLocation() == null && player.getPosition().x == i && player.getPosition().y == j && !(age%7000 < 3500))
					g.setColor(Color.RED);
				g.fillRect(i*PIXELSTEP, j*PIXELSTEP, PIXELSTEP, PIXELSTEP);
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
	
	public void formMountains(){
		int count = r.nextInt((int) (Math.pow(MAPSIZE, 2)/1000))+18;
		for(int i = 0; i < count; i++){
			int rangeLength = r.nextInt(MAPSIZE/3);
			int rangeWidth = r.nextInt(MAPSIZE/17) + 2;
			boolean northSouth = r.nextBoolean();
			boolean eastWest = r.nextBoolean();
			if(northSouth && eastWest)
				eastWest = false;

			Point startPoint = new Point(r.nextInt(MAPSIZE), r.nextInt(MAPSIZE));
			while(getTerrain(startPoint.x, startPoint.y) != Terrain.PLAINS){
				startPoint = new Point(r.nextInt(MAPSIZE), r.nextInt(MAPSIZE));
			}
			
			formMountainRange(rangeLength, rangeWidth, northSouth, eastWest, startPoint);
		}
	}
	
	private void setTerrain(int x, int y, Terrain t){
		if(x > 0 && x < MAPSIZE && y > 0 && y < MAPSIZE && mapTerrain[x][y] != Terrain.OCEAN)
			mapTerrain[x][y] = t;
	}
	
	
	private void formMountainRange(int rangeLength, int rangeWidth,
			boolean northSouth, boolean eastWest, Point startPoint) {
		
		int dx = 0;
		int dy = 0;
		for(int i = 0; i <= rangeLength; i ++){
			if(northSouth)
				dy = i;
			else
				dy += r.nextInt(3)-1;
			if(eastWest)
				dx = i;
			else
				dx += r.nextInt(3)-1;
			
			int xpos = startPoint.x+dx;
			int ypos = startPoint.y+dy;
			setTerrain(xpos,ypos,Terrain.MOUNTAINS);
			int effectiveWidth = (rangeWidth/2)-r.nextInt(3);
			for(int j = 1; j <= effectiveWidth; j++)
				if(northSouth){
					setTerrain(xpos-j,ypos,Terrain.MOUNTAINS);
					setTerrain(xpos+j,ypos,Terrain.MOUNTAINS);
				} else{
					setTerrain(xpos,ypos+j,Terrain.MOUNTAINS);
					setTerrain(xpos,ypos-j,Terrain.MOUNTAINS);
				}
		}
		
	}

	public void formTerrain(){
		formMountains();
		formDeserts();
		formForrests();
		for(int i = 3; i<=4; i++){
			Terrain t = Terrain.PLAINS;
			switch(i){
			case 2: t = Terrain.MOUNTAINS;
				break;
			case 3: t = Terrain.DESERT;
				break;
			case 4: t = Terrain.FORREST;
			
			}
			int count = r.nextInt((int) (Math.pow(MAPSIZE, 2)/1000))+18;
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
		
		
	}
	
	private void formForrests() {
		
		
	}

	private void formDeserts() {
		// TODO Auto-generated method stub
		
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
		if(mass < (MAPSIZE*MAPSIZE)/2){
			formContinents();
			return;
		}
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
