 package mahjong;

import java.util.Random;
public class Deck {
	
	private Tile[] deck;
	private String classType;
	private String value;
	Random rand = new Random();
	public int currentTile = -1;
	
	//Deck constructor creates and shuffles a deck object of 136 tile objects.
	public Deck() {
		deck = new Tile[136]; 
		//This nested loop adds all copies of sticks, buns, and thousands to the deck in order -- this takes up the first 108 indices in deck[].
		for(int c = 0; c<3; c++) {
			if(c == 0) {
				classType = "Stick";
			} else if(c == 1 ) {
				classType = "Bun";
			} else {
				classType = "Thousand";
			}
			for(int i = 0; i<9; i++) {
				for(int j = 0; j<4; j++) {
					Tile temp = new Tile();
					temp.setClassType(classType);
					temp.setValue(Integer.toString(i+1));
					deck[36*c+i*4+j] =  temp;
				}
			}
		}
		//This nested for loop adds all 4 copies of every non-suited tile in the game to the deck in order.
		for (int i = 0; i<7; i++) {
			switch (i) {
				case 1: value = "North";
						break;
				case 2: value = "East";
						break;
				case 3: value = "South";
						break;
				case 4: value = "West";
						break;
				case 5: value = "Center";
						break;
				case 6: value = "Lucky";
						break;
				default: value = "Mirror";
						break;
			}
			for (int j = 0; j<4; j++) {
				Tile temp = new Tile();
				temp.setClassType("Other");
				temp.setValue(value);
				deck[108+4*i+j] = temp;
			}
		}
		shuffle();
	}
	
	//Shuffles deck by swapping each tile in order with a random one after it (or itself).
	private void shuffle() {
		for(int i = 0; i<136; i++) {
			int r = i + rand.nextInt(136-i);
			Tile temp = deck[r];
			deck[r] = deck[i];
			deck[i] = temp;
		}
	}
	
	//Returns the tile at currentTile index and increments currentTile.
	public Tile draw() {
		currentTile++;
		return deck[currentTile];
	}
}
