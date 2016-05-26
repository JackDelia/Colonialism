import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class Map extends JPanel{

	public int[][] mapTerrain = new int[MAPSIZE][MAPSIZE];
	public String[][][] mapResources = new String[MAPSIZE][MAPSIZE][];
	public ArrayList<City> cities = new ArrayList<City>();
	public HashMap<String, Double> prices = new HashMap<String,Double>();
	public Random r = new Random();
	public static final int MAPSIZE = 100;
	public static final int PIXELSTEP = 6;
	public static final String[] NATURAL = {"gold", "stone", "iron"};
	public Player player;
	private int age = 0;
	
	public Map() {
		//procedural generation possible later
		//or maybe just a set map
		prices.put("iron", .01);
		prices.put("stone", .01);
		prices.put("gold", .10);
		prices.put("meat", .01);
		prices.put("fish", .01);
		prices.put("grain", .01);
		prices.put("wood", .01);
		prices.put("soldiers", .01);
		formContinents();
		formTerrain();
		stockResources();
		this.setSize(500,500);
	}
	
	public void stockResources(){
		for(String[][] s1: mapResources){
			for(String[] s2: s1){
				double value = Math.random();
				if(value > (335/336.0)){
					
					s2 = new String[3];
					s2[0] = NATURAL[r.nextInt(NATURAL.length)];
					s2[1] = NATURAL[r.nextInt(NATURAL.length)];
					while(s2[0] == s2[1])
						s2[1] = NATURAL[r.nextInt(NATURAL.length)];
					s2[2] = NATURAL[r.nextInt(NATURAL.length)];
					while(s2[0] == s2[2] || s2[1] == s2[2])
						s2[2] = NATURAL[r.nextInt(NATURAL.length)];
				}
				else if(value > (48/49.0)){
					System.out.println("2");
					s2 = new String[2];
					s2[0] = NATURAL[r.nextInt(NATURAL.length)];
					s2[1] = NATURAL[r.nextInt(NATURAL.length)];
					while(s2[0] == s2[1])
						s2[1] = NATURAL[r.nextInt(NATURAL.length)];
				}
				else if(value>(7/10.0)){
					System.out.println("1");
					s2 = new String[1]; 
					s2[0] = NATURAL[r.nextInt(NATURAL.length)];
				}
				else
					s2 = new String[0];
				
			}
		}
		
	}
	
	public void paintComponent(Graphics g){
		for(int i = 0; i< MAPSIZE; i++){
			for(int j = 0; j< MAPSIZE; j++){
				switch (mapTerrain[i][j]){
				case 0: g.setColor(Color.BLUE);
					break;
				case 1: g.setColor(Color.ORANGE);
					break;
				case 2: g.setColor(Color.BLACK);
					break;
				case 3: g.setColor(Color.YELLOW);
					break;
				case 4: g.setColor(Color.GREEN);
					break;
				case 5: g.setColor(Color.CYAN);
					break;
				default: g.setColor(Color.RED);
					break;
				}
				if(player != null && !player.visible[i][j])
					g.setColor(Color.WHITE);
				if(player != null && player.location == null && player.xloc == i && player.yloc == j && !(age%7000 < 3500))
					g.setColor(Color.RED);
				g.fillRect(i*6, j*6, 6, 6);
			}
			
			for(City c : cities){
				if(!(player.location == c && age%7000 < 3500)){
					g.setColor(Color.RED);
					int ovalSize = 7 + c.size/2000;
					g.fillOval(c.xpos*6 - ovalSize/2, c.ypos * 6-ovalSize/2, ovalSize , ovalSize);
					g.drawString(c.name, c.xpos*6, c.ypos*6-10);
				}
			}
			age++;
		}
	}
	
	public void formTerrain(){
		for(int i = 2; i<=4; i++){
			int count = r.nextInt((int) (Math.pow(MAPSIZE, 2)/1000))+18;
			System.out.println(i + "  " + count);
			while (count > 0){
				int x = r.nextInt(MAPSIZE-2)+1;
				int y = r.nextInt(MAPSIZE-2)+1;
				if(mapTerrain[x][y] == 5){
					mapTerrain[x][y] = i;
					int dirCount = 1;
					while(Math.random()<.75 && dirCount<x){
						if(mapTerrain[x-dirCount][y] == 5){
							mapTerrain[x-dirCount][y] = i;
							if(mapTerrain[x-dirCount][y+1] ==5 && Math.random()>=.5)
								mapTerrain[x-dirCount][y+1] = i;
							if(mapTerrain[x-dirCount][y+1] ==5 && Math.random()>=.5)
								mapTerrain[x-dirCount][y-1] = i;
						}
						dirCount++;
					}
					dirCount = 1;
					while(Math.random()<.75 && dirCount+x<MAPSIZE){
						if(mapTerrain[x+dirCount][y] == 5){
							mapTerrain[x+dirCount][y] = i;
							if(mapTerrain[x+dirCount][y+1] ==5 && Math.random()>=.5)
								mapTerrain[x+dirCount][y+1] = i;
							if(mapTerrain[x+dirCount][y+1] ==5 && Math.random()>=.5)
								mapTerrain[x+dirCount][y-1] = i;
						}
						dirCount++;
					}
					dirCount = 1;
					while(Math.random()<.75&& dirCount<y){
						if(mapTerrain[x][y-dirCount] == 5){
							mapTerrain[x][y-dirCount] = i;
							if(mapTerrain[x+1][y-dirCount] ==5 && Math.random()>=.5)
								mapTerrain[x+1][y-dirCount] = i;
							if(mapTerrain[x-1][y-dirCount] ==5 && Math.random()>=.5)
								mapTerrain[x-1][y-dirCount] = i;
						}
						dirCount++;
					}
					dirCount = 1;
					while(Math.random()<.75 && dirCount+y < MAPSIZE){
						if(mapTerrain[x][y+dirCount] == 5){
							mapTerrain[x][y+dirCount] = i;
							if(mapTerrain[x+1][y+dirCount] ==5 && Math.random()>=.5)
								mapTerrain[x+1][y+dirCount] = i;
							if(mapTerrain[x-1][y+dirCount] ==5 && Math.random()>=.5)
								mapTerrain[x-1][y+dirCount] = i;
						}
						dirCount++;
					}
					count--;
				}
			}
		}
		for(int w = 0; w< MAPSIZE; w++)
			for(int z = 0; z< MAPSIZE; z++)
				if(mapTerrain[w][z] == 5 && Math.random()>.75)
					mapTerrain[w][z] = 1;
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
					mapTerrain[j][i] = 0;
				else{
					mapTerrain[j][i] = 5;
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
	public int getTerrain(int lattitude, int longitude){
		if(lattitude >= MAPSIZE || longitude >= MAPSIZE || lattitude < 0 || longitude < 0)
			return -1;
		return mapTerrain[lattitude][longitude];
	}

	public ArrayList<String> getNearbyResources(int lattitude,
			int longitude) {
		
		ArrayList<String> ret = new ArrayList<String>();
		if(Math.random() > .5)
			ret.add("meat");
		for(int i = -2; i<=2; i++){
			for(int j = -2; j<=2; j++){
				if(mapTerrain[lattitude+i][longitude+j] == 4 && !ret.contains("wood"))
					ret.add("wood");
				if(lattitude+i > 0 && longitude+j > 0 && lattitude+i < Map.MAPSIZE && longitude+j < Map.MAPSIZE && mapResources[lattitude+i][longitude+j] != null){
					for(String s: mapResources[lattitude+i][longitude+j]){
						if(!ret.contains(s))
							ret.add(s);
					}
				}
			}
		}
		
		if(Math.random() > .5 || ret.size() == 0)
			ret.add("grain");
		
		return ret;
	}

	public boolean valid(int xpos, int ypos) {
		if(mapTerrain[xpos][ypos] == 0)
			return false;
		for(City c : cities)
			if(((Math.pow((c.xpos-xpos),2))+ (Math.pow((c.ypos-ypos),2)))<10)
				return false;
		return true;
	}
	

}
