import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Tester {

	public static void printMap(Map m, Player p){
		System.out.print("  ");
		for(int a = 0; a< 100; a++){
			System.out.print(a+ " ");
			if(a < 10)
				System.out.print(" ");
		}
		System.out.println();
		for(int i = 0; i< 100; i++){
			System.out.print(i);
			if(i<10)
				System.out.print(" ");
			for(int j = 0; j< 100; j++){
				if(p.visible[i][j])
					System.out.print(m.mapTerrain[j][i]+ "  ");
				else
					System.out.print("?  ");
			}
			System.out.println();
			System.out.println();
		}
	}
	public static void main(String[] asdsfafs){

		
		Scanner s = new Scanner(System.in);
		int day = 0;
		int waiting = 0;
		HashMap<Integer,ArrayList<Integer>> exploration = new HashMap<Integer,ArrayList<Integer>>();
		HashMap<Integer, ArrayList<Explorer>> explorersOut = new HashMap<Integer, ArrayList<Explorer>>();
		Map map = new Map();
		
		
		
		/*
		System.out.print("Enter name: ");*/
		Player p = new Player("Jack", map);
		map.player = p;
		
		JFrame frame = new JFrame();
		frame.add(map);
		frame.setSize(600, 600);
		frame.setVisible(true);
		/*
		System.out.print("Enter city name: ");
		*/
		p.ships.add(new Ship(p.yloc,p.xloc,p,1));
		int input;
		
		while(p.cities.size()==0){
			map.repaint();
			if(exploration.get(day) != null){
				System.out.println("Exploration finished");
				for(Integer returnData : exploration.get(day)){
					System.out.println(returnData);
					p.visible[returnData%100][(returnData/100)-1] = true;
				}
				for(Explorer explore : explorersOut.get(day)){
					if(explore instanceof Ship){
						if(((Ship)explore).origin != null){
							((Ship)explore).origin.fleet.add((Ship)explore);
							p.ships.add((Ship)explore);
						}
						else{
							((Ship)explore).xloc = ((Ship)explore).xOrigin;
							((Ship)explore).yloc = ((Ship)explore).yOrigin;
							p.ships.add((Ship)explore);
						}
					}
					else{
						if(((Soldier)explore).origin != null){
							((Soldier)explore).origin.garrison.add((Soldier)explore);
							p.troops.add((Soldier)explore);
						}
						else{
							((Soldier)explore).xloc = ((Soldier)explore).xOrigin;
							((Soldier)explore).yloc = ((Soldier)explore).yOrigin;
							p.troops.add((Soldier)explore);
						}
					}
				}
			}
			while(true && waiting== 0){
				System.out.println("What to do?\n1: explore\n2: found city\n3: pass time\n4: check map\n5: quit");
				input = s.nextInt();
				if(input == 1){
					explore(p,s,exploration,explorersOut,day);
				}
				if(input == 2){
					foundCity(p,s);
				}
				if(input ==3){
					System.out.println("How long?");
					waiting += s.nextInt()-1;
					break;
				}
				if(input ==4){
					printMap(map,p);
				}
				if(input == 5){
					System.exit(0);
				}
			}
			day++;
			p.Update(1);
			if(waiting >0)
				waiting--;
		}
		
		
		
		while(true){
			if(exploration.get(day) != null){
				System.out.println("Exploration finished");
				for(Integer returnData : exploration.get(day)){
					p.visible[returnData/100][returnData] = true;
				}
				for(Explorer explore : explorersOut.get(day)){
					if(explore instanceof Ship){
						if(((Ship)explore).origin != null){
							((Ship)explore).origin.fleet.add((Ship)explore);
							p.ships.add((Ship)explore);
						}
						else{
							((Ship)explore).xloc = ((Ship)explore).xOrigin;
							((Ship)explore).yloc = ((Ship)explore).yOrigin;
							p.ships.add((Ship)explore);
						}
					}
					else{
						if(((Soldier)explore).origin != null){
							((Soldier)explore).origin.garrison.add((Soldier)explore);
							p.troops.add((Soldier)explore);
						}
						else{
							((Soldier)explore).xloc = ((Soldier)explore).xOrigin;
							((Soldier)explore).yloc = ((Soldier)explore).yOrigin;
							p.troops.add((Soldier)explore);
						}
					}
				}
			}
			while(true){
				System.out.println("what to do?\n1: found city\n2: view info\n3: look at a city\n4: pass time"
						+ "\n5: recruit\n6: check map\n7: quit");
				input = s.nextInt();
				
				if(input == 1){
					foundCity(p,s);
				}
				
				if(input == 2){
					System.out.println(p);
				}
				
				if(input == 3)
				{
					lookAtCity(p,s);
				}
				
				if(input == 4){
					day++;
					p.Update(1);
					break;
				}
				if(input == 5){
					recruit(p,s);
				}
				if(input == 6){
					printMap(map, p);
				}
				if(input >= 7)
					System.exit(0);
			}
		}		
	}
	
	public static int chooseCity(Player p, Scanner s){
		System.out.println("Which city?");
		for(City c : p.cities)
			System.out.println(c.cityId + ": " + c.name);
		System.out.println(p.cities.size() + " to quit");
		return s.nextInt();
	}
	
	public static void foundCity(Player p, Scanner s){
		System.out.println("where(x)?");
		int x = s.nextInt();
		System.out.println("where(y)?");
		int y = s.nextInt();
		System.out.println("name?");
		p.foundCity(s.next(), x, y);
	}
	
	public static void lookAtCity(Player p, Scanner s){
		while(true){
			int k = chooseCity(p,s);
			if(k>= p.cities.size())
				break;
			while(true){
				System.out.println("what to do with " + p.cities.get(k).name + "?\n"
						+ "1: view\n"
						+ "2: adjust funding\n"
						+ "3: adjust production\n"
						+ "4: adjust instructions\n"
						+ "5: transfer troops\n"
						+ "6: quit");
				int cc = s.nextInt();
				if(cc == 1)
					System.out.println(p.cities.get(k));
				if(cc == 2){
					System.out.println("adjust to what?");
					p.cities.get(k).funding = s.nextDouble();
				}
				if(cc == 3){
					System.out.println("of what?");
					int resCount = 1;
					ArrayList<String> resStore = new ArrayList<String>();
					for(java.util.Map.Entry<String, Double> entry : p.cities.get(k).production.entrySet()){
						System.out.println(resCount +": " + entry.getKey());
						resCount++;
						resStore.add(entry.getKey());
					}
					System.out.println(resCount + ": Never Mind");
					int choice = s.nextInt();
					if(choice <resCount){
						String resChosen = resStore.get(choice-1);
						System.out.println("to what?");
						p.cities.get(k).balanceProduction(resChosen, s.nextDouble());
					}
				}
				if(cc == 5){
					System.out.print("To ");
					int kk = chooseCity(p,s);
					if(kk< p.cities.size()){
						System.out.println("How Many?");
						int many = s.nextInt();
						if(many >= p.cities.get(k).garrison.size())
							System.out.println("not enough troops.");
						else{
							for(int jk = 0; jk < many; jk++){
								p.cities.get(kk).garrison.add(p.cities.get(k).garrison.get(0));
								p.cities.get(k).garrison.remove(0);
							}
						}
					}
						
				}
				if(cc>5)
					break;
				
			}
		}
	}
	public static void recruit(Player p, Scanner s){
		System.out.println("how many?");
		int quantity = s.nextInt();
		Soldier sol = new Soldier(p.capitol,p, quantity);
		p.money -= p.map.prices.get("soldiers")*quantity;
		p.troops.add(sol);
		if(p.capitol != null)
			p.capitol.garrison.add(sol);
	}
	public static void explore(Player p, Scanner s, HashMap<Integer,ArrayList<Integer>> exploration, 
			HashMap<Integer, ArrayList<Explorer>> explorersOut, int day){
		System.out.println("With what?");
		for(int i = 0; i < p.troops.size(); i++)
			System.out.println(i+1 + ": " + p.troops.get(i).name);
		for(int j = 0; j< p.troops.size()+p.ships.size(); j++)
			System.out.println(j+1+p.troops.size() + ": " + p.ships.get(j).name);
		System.out.println(p.troops.size()+p.ships.size()+1+": Cancel");
		int choice = s.nextInt();
		Explorer k = null;
		if(choice <p.troops.size() && choice>0){
			k = p.troops.get(choice-1);
		}
		else if(choice < p.troops.size()+p.ships.size()+1&& choice >0){
			k = p.ships.get(choice-1-p.troops.size());
		}
		else{
			return;
		}
		System.out .println("to what x?");
		int xtar = s.nextInt();
		System.out .println("to what y?");
		int ytar = s.nextInt();
		int l = k.explore(xtar, ytar);
		System.out.println(l);
		exploration.put(l+day, k.getknowledge());
		if(explorersOut.get(l+day) == null){
			ArrayList<Explorer> ex = new ArrayList<Explorer>();
			ex.add(k);
			explorersOut.put(l+day, ex);
		}
		else{
			ArrayList<Explorer> ex = explorersOut.get(l+day);
			ex.add(k);
			explorersOut.put(l+day, ex);
		}
	}
}
