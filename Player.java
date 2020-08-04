package mahjong;

public class Player {
	
	//Each player has a ComboChecker object and a PlacedCombos object.
	ComboChecker ComboChecker = new ComboChecker();
	PlacedCombos PlacedCombos = new PlacedCombos();
	
	Tile[] hand;
	Tile[] pocket;
	String playerName = "N/A";
	int handSize = 13;
	
	//Constructor initializes player's hand to store 14 tiles and pocket to store 5 tiles then fills them both. 
	public Player(Deck theDeck) {
		hand  = new Tile[14];
		pocket = new Tile[5];
		fillHandAndPocket(theDeck);
	}
	
	//Each of the 13 indices in hand and 5 indices in pocket are initialized to tiles drawn from a shuffled deck.
	private void fillHandAndPocket(Deck theDeck) {
		for(int i=0; i<13; i++) {
			hand[i] = theDeck.draw();
		}
		for(int i=0; i<5; i++) {
			pocket[i] = theDeck.draw();
		}
	}
	
	public void addName(String name) {
		playerName =  name;
	}
	
	public Tile draw(Deck theDeck) {
		return theDeck.draw();
	}
	
	//Tosses a tile into the pot, setting its previous location in hand to null, and handSize is decremented.
	public Tile toss(int tileToss) {
		Tile tempTile = hand[tileToss-1];
		hand[tileToss-1] = null;
		handSize--;
		return tempTile;
	}
	
	//When drawing a tile from the deck or pot, the tile is added to the end of the hand, and handSize is incremented.
	public void addToHand(Tile tileAdd) {
		hand[handSize] = tileAdd;
		handSize++;
	}
	
	//When choosing to place a tile in the pocket, this function is called to replace it with a tile at a specified index.
	public Tile swapInPocket(Tile currentTile, int index) {
		Tile tempTile = pocket[index-1];
		pocket[index-1] = currentTile;
		return tempTile;
	}
	
	//Swaps two tiles with one another.
	public void rearrange(int tileOne, int tileTwo) {
		Tile temp = hand[tileOne-1];
		hand[tileOne-1] = hand[tileTwo-1];
		hand[tileTwo-1] = temp;
	}
	
	//Tries to place a triple combo by checking its validity with ComboChecker, then placing it in PlacedCombos if it is valid.
	public boolean placeCombo(Tile tile1, Tile tile2, Tile tile3) {
		if(ComboChecker.checkCombo(tile1, tile2, tile3)) {
			PlacedCombos.createCombo(tile1, tile2, tile3);
			System.out.println("\nCreated a Triple Combo!");
			return true;
		} else {
			return false;
		}
	}
	
	//Tries to place a double combo by checking its validity with ComboChecker, then placing it in PlacedCombos if it is valid.
	public boolean placeCombo(Tile tile1, Tile tile2) {
		if(ComboChecker.checkCombo(tile1, tile2)) {
			PlacedCombos.createCombo(tile1, tile2);
			System.out.println("\nCreated a Double Combo!");
			return  true;
		} 
		return false;
	}
	
	//Stitches hand together by removing all of the null tiles. This is done after tossing tiles from one's hand.
	public void fixHand() {
		int newSpot = 0;
		for(int i=0; i<14; i++) {
			if (hand[i] != null) {
				hand[newSpot] = hand[i];
				newSpot++;
			}
		}
	}

	//Prints the contents of a player's hand and pocket.
	public void printHandAndPocket() {
		System.out.println("-----" + playerName + "'s HAND-----");
		for(int i=0; i<handSize; i++) {
			System.out.print(i+1 + ": ");
			System.out.println(hand[i]);
		}
		System.out.println("\n-----" + playerName + "'s POCKET-----");
		for(int k=0; k<5; k++) {
			System.out.print(k+1 + ": ");
			System.out.println(pocket[k]);
		}
	}
	
	//Print all of a player's placed combos.
	public void printCombos() {
		PlacedCombos.printCombos(playerName);
		
	}
	
}

