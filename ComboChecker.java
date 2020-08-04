package mahjong;

public class ComboChecker {
	
	public boolean flagDouble=false;
	public int tripleCounter = 0;
	private String[] suits = new String[2];
	private int suitCount=0;
	
	public ComboChecker() {
	}
	
	//Checks to see if a valid triple combo can be made with parameter tiles. 
	public boolean checkCombo(Tile tile1, Tile tile2, Tile tile3) {
		//Return boolean to signify if this was a valid combo
		boolean placed = false;
		//Checks if all class types are the same and there are no more than two suits being used to make combos. Only then is it possible that a combo can be made.
		if(tile1.getClassType().equalsIgnoreCase(tile2.getClassType()) && tile1.getClassType().equalsIgnoreCase(tile3.getClassType()) && checkSuit(tile1)) {			
			//If all values are equal, a valid three-of-a-king combo was made.
			if(tile1.getValue().equalsIgnoreCase(tile2.getValue()) && tile1.getValue().equalsIgnoreCase(tile3.getValue())) {
				placed = true;
				tripleCounter++;
				return placed;
			} else if(tile1.getClassType().equalsIgnoreCase("Other")) { //If the class type is non-suited and the values are not all equal, it is an invalid combo.
				System.out.println("\nNon-suited must be three of a kind.");
				return placed;
			} else { //If the class type is suited and the values are not all equal, check if the values can be put in ascending order. If so, it is a valid combo.
				int value1 = Integer.parseInt(tile1.getValue());
				int value2 = Integer.parseInt(tile2.getValue());
				int value3 = Integer.parseInt(tile3.getValue());
				int temp;
				if (value1>value2) {
					temp =  value1;
					value1 = value2;
					value2 = temp;
				}
				if (value2>value3) {
					temp = value2;
					value2 = value3;
					value3 = temp;
				}
				if (value1>value2) {
					temp = value1;
					value1 = value2;
					value2  = temp;
				}
				if(++value1==value2 && ++value2==value3) {
					tripleCounter++;
					placed = true;
				}
				return placed;
			}
		} else {
			return placed;
		}
	}

	//Checks to see if a valid double combo can be made with parameter tiles. 
	public boolean checkCombo(Tile tile1,Tile tile2) {
		boolean placed = false;
		//If flagDouble is true meaning they already have one double combo, no more double combos can be placed.
		if(flagDouble) {
			System.out.println("\nYou already have a double. ");
		} else if(tile1.getClassType().equals(tile2.getClassType()) && tile1.getValue().equals(tile2.getValue())) { //If the two tiles' class type and values match, it is a valid double combo.
			if(checkSuit(tile1)) { //If suit checker is passed, they may place the double combo.
				placed = true;
				flagDouble = true;
			} else { //If suit checker is not passed, they may not place the double combo.
				System.out.println("\nSorry, you may only use 2 suits.");
			}
		} 
		return placed;
	}
	
	//Returns true if player has placed 4 triple combos and a double combo, false otherwise.
	public boolean winChecker() {
		if(tripleCounter == 4 && flagDouble) {
			return true;
		} else {
			return false;
		}
	}
	
	//If player tries to place a suited combo, checkSuit ensures that they are only allowed to use up to two suits for all of their combos. If they've used less than two suits
	//the new suit will be tracked in the suits[] array and suitCount is incremented.
	private boolean checkSuit(Tile tile1) {
		String classTemp  = tile1.getClassType();
		if(suitCount == 2 ) {
			if(classTemp!=suits[0] && classTemp!=suits[1]) {
				System.out.println("\nYou already have two suits. \n");
				return false;
			}
			else {
				return true;
			}
		} else if(suitCount == 1){
			if(suits[0] != classTemp) {
				suits[1] = classTemp;
				suitCount++;
			}
			return true;
		} else {
			suits[0] = classTemp;
			suitCount++;
			return true;
		}
	}

}
