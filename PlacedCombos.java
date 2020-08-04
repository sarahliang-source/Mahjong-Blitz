package mahjong;

public class PlacedCombos {
	
	public Tile[][] tripleCombos;
	public Tile[] doubleCombo;
	public int tripleComboCount;
	
	//PlacedCombos constructor initializes arrays of tiles to house the combos that a player has placed.
	public PlacedCombos() {
		tripleCombos = new Tile[4][3];
		doubleCombo = new Tile[2];
		tripleComboCount = 0; //This is used to monitor the number of triple combos for the tripleCombos 2D array.
	}
	
	//Adds a 2 tile combo to your doubleCombo array.
	public void createCombo(Tile tile1, Tile tile2) {
		doubleCombo[0] = tile1;
		doubleCombo[1] = tile2;
	}
	
	//Adds a 3 tile combo to the first open slot in your tripleCombo array in increasing order.
	public void createCombo(Tile tile1, Tile tile2, Tile tile3) {
		Tile temp;
		//Sorts the tiles in increasing order if they are not identical values
		if (tile1.isGreaterThan(tile2)) {
			temp = tile1;
			tile1 = tile2;
			tile2 = temp;
		}
		if (tile2.isGreaterThan(tile3)) {
			temp = tile2;
			tile2 = tile3;
			tile3 = temp;
		}
		if (tile1.isGreaterThan(tile2)) {
			temp = tile1;
			tile1 = tile2;
			tile2 = temp;
		}
		tripleCombos[tripleComboCount][0] = tile1;
		tripleCombos[tripleComboCount][1] = tile2;
		tripleCombos[tripleComboCount][2] = tile3;
		tripleComboCount++;
	}
	
	//This utilizes a GameObject stack object called comboTiles and removes those tiles from your placed combos in the case that you were unable to place all of your tiles after
	//attempting to make a double combo with a tile from the pot.
	public void undoAllCombos(GameObjects GO) {
		Tile[] temp;
		for(int i=0; i<GO.comboTiles.size(); i++) {
			temp = GO.comboTiles.pop();
			if(temp.length == 2) {
				undoCombo(temp[0], temp[1], GO.currentPlayer);
			} else {
				undoCombo(temp[0], temp[1], temp[2], GO.currentPlayer);
			}
		}
		GO.comboTiles.clear();
	}
	
	//Undoes a double combo and returns the double flag to false.   This function only assists undoAllCombos
	private void undoCombo(Tile tile1, Tile tile2, Player currentPlayer) {
		currentPlayer.addToHand(tile1);
		currentPlayer.addToHand(tile2);
		doubleCombo[0] = null;
		doubleCombo[1] = null;
		currentPlayer.ComboChecker.flagDouble = false;
		System.out.println("\nUndid double combo\n");
	}
	
	//Undoes a triple combo and decrements tripleComboCount.   This function only assists undoAllCombos
	private void undoCombo(Tile tile1, Tile tile2, Tile tile3, Player currentPlayer) {
		currentPlayer.addToHand(tile1);
		currentPlayer.addToHand(tile2);
		currentPlayer.addToHand(tile3);
		tripleComboCount--;
		tripleCombos[tripleComboCount][0] = null;
		tripleCombos[tripleComboCount][1] = null;
		tripleCombos[tripleComboCount][2] = null;
		System.out.println("\nUndid triple combo\n");
	}
	
	//Prints all your combos, and changes "null" tiles to "x"'s instead for readability
	public void printCombos(String playerName) {
		System.out.println("\n-----" + playerName + "'s Triples-----");
		for(int i=0; i<4; i++) {
			if(tripleCombos[i][0] == null) { //if a tripleCombo row is empty, print x's instead
				System.out.print("x, x, x\n");
			} else {
				System.out.print(tripleCombos[i][0] + ", ");
				System.out.print(tripleCombos[i][1] + ", ");
				System.out.print(tripleCombos[i][2] + "\n");
			}
		}
		System.out.println("\n-----" + playerName + "'s Double-----");
		if(doubleCombo[0] == null) { //if doubleCombo is empty, print x's instead
			System.out.println("x, x");
		} else {
			System.out.println(doubleCombo[0] + ", " + doubleCombo[1]);
		}
	}
}
