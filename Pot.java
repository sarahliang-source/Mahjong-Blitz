package mahjong;

import java.util.ArrayList;

public class Pot {
	
	ArrayList<Tile> pot;
	
	//Pot constructor initializes the object as an array list of tiles.
	public Pot() {
		pot = new ArrayList<Tile>();
	}
	
	//Adds tile specified in the parameter to the end of the pot.
	public void add(Tile tile) {
		pot.add(tile);
	}
	
	//Returns the last addition to the pot. 
	public Tile getLastToss() {
		return pot.get(pot.size()-1);
	}
	
	//Remove the last addition from the pot.
	public void removeFromPot() {
		pot.remove(pot.size()-1);
	}
	
	//Prints out the entirety of the pot's contents.
	public String toString() {
		System.out.println("\n\n==========POT==========");
		for(int i = 0; i<pot.size(); i++) {
			System.out.print(pot.get(i) + ", ");
			if(i%10==9) {
				System.out.print("\n");
			}
		}
		return "\n=======================\n";
	}
}
