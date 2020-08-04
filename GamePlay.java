package mahjong;

import java.util.*;

public class GamePlay {
	
	Scanner scan = new Scanner(System.in);
	
	public GamePlay() {
	}
	
	//Prints the main menu as the first thing users see, which provides them with 3 options: To enter the game, display rules/tutorial, and to quit the application.
	public void printMenu() {
		int menuChoice;
		int playOrReturn;
		System.out.println("_____________________________________________");
		System.out.println("|                                           |");
		System.out.println("|  ~~~~~~~~~~~~ MAHJONG BLITZ ~~~~~~~~~~~~  |");
		System.out.println("|                 MAIN MENU                 |");
		System.out.println("|                                           |");
		System.out.println("|                                           |");
		System.out.println("|   (1) Play Mahjong Blitz                  |");
		System.out.println("|   (2) Display Rules & Tutorial            |");
		System.out.println("|   (3) Quit                                |");
		System.out.println("|                                           |");
		System.out.println("|       By: Aidan Hoppe & Sarah Liang       |");
		System.out.println("|___________________________________________|");
		menuChoice = answerCheckInt(1,3,"(Type choice (1,2,3) and press Enter): ",scan);
		switch(menuChoice) {
			//Play Mahjong Blitz --- this just returns from the printMenu method so the game can continue in main.
			case 1: 
				return;
			//Tutorial & Rules
			case 2:
				printRules();
				playOrReturn = answerCheckInt(1, 2, "\nWould you like to start playing Mahjong Blitz (1) or return to main menu (2)? (1,2): ", scan); //option to return to menu or play directly
				if (playOrReturn == 1) {
					return;
				} else {
					printMenu();
					return;
				}
			// Exit
			case 3:
				System.exit(0);
		}
	}
	
	//Clears screen so next player can't see previous player's hand. Prompts next player to press Enter when they're ready to start their turn.
	public void alertPlayer(Player currentPlayer) {
		clearScreen();
		System.out.print(currentPlayer.playerName + " press Enter to start your turn!");
		scan.nextLine();
	}
	
	//Retrieves player names, they each roll the dice to see who starts off the game, and each player is given the opportunity to rearrange their hand as many times as they want.
	public int setUpPlayers(GameObjects GO) {
		Random rand = new Random();
		int rVal;
		int firstDie = 0;
		int secondDie = 0;
		
		System.out.print("\n\nFirst Player's Name: ");
		String name1 = scan.nextLine();
		System.out.print("Second Player's Name: ");
		String name2 = scan.nextLine();
		
		GO.player1.addName(name1);
		GO.player2.addName(name2);
		System.out.println("\n" + name1 + " and " + name2 +  " prepare to duke it out in MAHJONG BLITZ 1v1!\n\n");
		
		while(firstDie  ==  secondDie) {
			System.out.print("\n" + GO.player1.playerName + " press Enter to roll the dice");
			scan.nextLine();
			firstDie = rand.nextInt(5) + 1;
			System.out.println(GO.player1.playerName + " rolled a " + firstDie + "\n");
			
			System.out.print(GO.player2.playerName + " press Enter to roll the dice");
			scan.nextLine();
			secondDie = rand.nextInt(5) + 1;
			System.out.println(GO.player2.playerName + " rolled a " + secondDie);
		}
		
		if(firstDie > secondDie) {
			System.out.println("\n~~~" + GO.player1.playerName + " will go first!~~~");
			GO.setCurrentPlayer(GO.player1);
			GO.setOpponentPlayer(GO.player2);
			rVal = 0;
			GO.currentTurn = 0;
		} else {
			System.out.println("\n~~~" + GO.player2.playerName + " will go first!~~~");
			GO.setCurrentPlayer(GO.player2);
			GO.setOpponentPlayer(GO.player1);
			rVal = 1;
			GO.currentTurn = 1;
		}
		
		System.out.print("\nBoth players will have the option to rearrange their hand now. \n\n"+GO.getCurrentPlayer().playerName+" press Enter when ready.\n");
		scan.nextLine();
		GO.currentPlayer.printHandAndPocket();
		rearrangeAndPrint(GO, GO.currentPlayer);
		GO.nextTurn();
		clearScreen();
		System.out.print("\n\nNow it is " + GO.getCurrentPlayer().playerName + "'s turn to rearrange. \n\n" + GO.getCurrentPlayer().playerName + " press Enter when ready.\n");
		scan.nextLine();
		GO.currentPlayer.printHandAndPocket();
		rearrangeAndPrint(GO, GO.currentPlayer);
		GO.nextTurn();
		clearScreen();
		
		System.out.println("\nPlayers get ready! The game will start NOW!\n");
		return rVal;
	}
	
	//Player can either get a tile by drawing from the deck or taking the last addition to the pot.
	public Tile takeOrDraw(GameObjects GO){
		//If it is the first round, player must draw from the deck.
		if(GO.firstRound) {
			GO.firstRound=false;
			return playerDraw(GO);
		} else {
			//If not the first round, player may draw from the deck or take from the pot.
			String tempString = answerCheckString("Y", "N", "\nWould you like to take the freshly tossed " + GO.mainPot.getLastToss() + "? (Y/N): ");
			//If they choose to take the last tossed tile, check and make sure they can make a combo with it.
			if(tempString.equalsIgnoreCase("Y")) {
				GO.tempTile = GO.mainPot.getLastToss();
				GO.tookFromPot = true;//the tookFromPot flag is set to true to indicate in main that if they took from the pot and did not make their necessary combo, they will instead have to draw from the deck.
				GO.mainPot.removeFromPot();
				GO.currentPlayer.addToHand(GO.tempTile);
				return GO.tempTile;
			} else { //Player chose to draw from the deck.
				return playerDraw(GO);
			}
		}
	}
	
	//Player uses standard drawing from the deck and gives the player the choice to put that tile in their hand or pocket.
	public Tile playerDraw(GameObjects GO) {
		Tile tempTile = GO.currentPlayer.draw(GO.mainDeck);
		System.out.println("\n\n[----------->You drew '" + tempTile + "'<-----------]");
		String handOrPocket = answerCheckString("H", "P", "\nWould you like to place in Hand(H) or Pocket(P): ");
		if(handOrPocket.equalsIgnoreCase("H")) { //if the player chooses to add the tile to their hand, it will simply be added to their hand with addToHand method.
			GO.currentPlayer.addToHand(tempTile);
		} else { //if the player chooses to add the tile to their pocket, this method will swap out the tile of their choice with the drawn tile and add the old tile to the pot automatically.
			int index = answerCheckInt(1, 5, "Which tile from your pocket would you like to replace? (1-5): ", scan);
			GO.mainPot.add(GO.currentPlayer.swapInPocket(tempTile, index));
			GO.usedPocket = true;//A 'usedPocket' flag will also be set to true to indicate in main that the player does not have to be prompted to toss a tile from their hand this turn.
		}
		return tempTile;
	}
	
	//mustMakeCombo is called only when the player attempts to take a tile from the pot on their turn, and it forces them to make a combo with that tile.
	public boolean mustMakeCombo(GameObjects GO) {
		int t1 = GO.currentPlayer.handSize;
		int t2 = 0;
		int t3 = 0;
		int numTiles = 0;
		String continueCombos = "Y";
		
		//This loop continues to run as long as the player wants to try and make a combo with the tile they took from the pot.
		while(continueCombos.equalsIgnoreCase("Y")) {
			numTiles = answerCheckInt(2, 3, "\nWould you like to place 2 or 3 tiles in your combo? (2/3): ", scan);
			System.out.println("The first tile in your combo is " + GO.currentPlayer.hand[GO.currentPlayer.handSize-1]);
			do { //asks for the index of the second tile in the combo until it is within handSize and is not the same as their other tile.
				t2 = answerCheckInt(1, GO.currentPlayer.handSize, "What is the index of the second tile in the combo? (1-" +(GO.currentPlayer.handSize-1) + "): ", scan);
			} while(t2 == t1);
			
			if(numTiles == 2) { //if they chose to place a 2 tile combo with a tile from the pot, it will call the placeCombo method for 2 tiles and set mustWin flag to true, then force them to makeCombos until they have won.
				if(GO.currentPlayer.placeCombo(GO.currentPlayer.hand[t1-1], GO.currentPlayer.hand[t2-1])) {
					GO.currentPlayer.toss(t1);
					GO.currentPlayer.toss(t2);
					GO.currentPlayer.fixHand();
					GO.mustWin=true;
					GO.comboTiles.push(GO.currentPlayer.PlacedCombos.doubleCombo);
					makeCombos(GO);
					break;
				}
			} else { //if they choose to make a 3 tile combo with a tile from the pot.
				do {//asks for the index of the third tile in the combo until it is within handSize and is not the same as their other tiles.
					t3 = answerCheckInt(1, GO.currentPlayer.handSize, "What is the index of the third tile in the combo? (1-" + (GO.currentPlayer.handSize-1) + "): ", scan);
				} while(t3 == t2 || t3 == t1);

				//if they successfully can place that combo, then it will remove the used tiles from their hand and add them to their placed combos as a combo.
				if(GO.currentPlayer.placeCombo(GO.currentPlayer.hand[t1-1], GO.currentPlayer.hand[t2-1], GO.currentPlayer.hand[t3-1])) {
					GO.currentPlayer.toss(t1);
					GO.currentPlayer.toss(t2);
					GO.currentPlayer.toss(t3);
					GO.currentPlayer.fixHand();
					return true;
				}
			}
			//If the combo they try to place did not pass checks for some reason, they have the option to enter the loop again by answering "Y" to trying to place the combo again.
			t2 = 0;
			t3 = 0;
			continueCombos = answerCheckString("Y","N","\nUnsuccessful combo. Would you like to try placing the combo again? (Y/N): ");
		}
		//When they choose to not try and continue making a combo with the tile from the pot, this method returns false and indicates they need to draw from the deck in main.
		return false;
	}
	
	//makeCombos handles the multitude of cases that can occur when trying to place a combo with tiles from your hand.
	public boolean makeCombos(GameObjects GO) {
		String continueCombos="Y";
		boolean placedSuccessfully  = true;
		int numTiles = 3; 
		int t1 = 0;
		int t2 = 0;
		int t3 = 0;
		
		//Unless they need to win (have tried to place a double combo with tile from pot), they are asked if they want to place any combos as part of their standard turn.
		if(!GO.mustWin) {
			continueCombos= answerCheckString("Y","N","\nWould you like to place any combos? (Y/N): ");
		} 
		
		//If they choose to place a combo from their hand or mustWin is true, enter this while loop.
		while(continueCombos.equalsIgnoreCase("Y")) {
			GO.printAll();
			if(!GO.mustWin) { //if they choose to place a combo from their hand, and they don't need to win they are asked if they want to place 2 or 3 tiles in their combo
				if(GO.currentPlayer.ComboChecker.tripleCounter== 4) { //if they have all their triples, they may only place a double and numTiles is automatically set to 2.
					System.out.println("You can only place a double.");
					numTiles = 2;
				} else if (GO.currentPlayer.ComboChecker.flagDouble) { //if they have a double, they may only place a triple and numTiles is automatically set to 3.
					System.out.println("You can only place a triple.");
					numTiles = 3;	
				} else { //if they don't have a double and still have space for another triple, they choose here which they would like to place and set numTiles accordingly
					numTiles = answerCheckInt(2,3,"\nWould you like to place (2) or (3) tiles in your combo? (2,3): ", scan);
				}
			} else { //if they must win on this turn, this means they took a double from the pot and numTiles is set to 3.
				System.out.println("\nYou must place the rest of your triples now.");
				numTiles=3;	
			}
			
			do { //asks for index of tiles desired for combo until they are within handSize and are not the same tile.
				t1 = answerCheckInt(1,GO.currentPlayer.handSize, "What is the index of the first tile in the combo? (1-" + GO.currentPlayer.handSize+"): ", scan);
				t2 = answerCheckInt(1,GO.currentPlayer.handSize, "What is the index of the second tile in the combo? (1-" + GO.currentPlayer.handSize+"): ", scan);
				if(numTiles == 3) {
					t3 = answerCheckInt(1,GO.currentPlayer.handSize, "What is the index of the third tile in the combo? (1-" + GO.currentPlayer.handSize+"): ", scan);
				}
			} while(t1 == t2 || t1 == t3 || t2 == t3);
			
			// This attempts to place either a 2 or 3 tile combo based on their inputs and sets placedSuccessfully accordingly.
			if(numTiles==2) {
				placedSuccessfully = GO.currentPlayer.placeCombo(GO.currentPlayer.hand[t1-1], GO.currentPlayer.hand[t2-1]);
			} else {
				placedSuccessfully = GO.currentPlayer.placeCombo(GO.currentPlayer.hand[t1-1], GO.currentPlayer.hand[t2-1], GO.currentPlayer.hand[t3-1]);
			}
			
			//If they did successfully place a combo, then remove the tiles they placed from their hand and fix their hand.
			if(placedSuccessfully) {
				GO.currentPlayer.toss(t1);
				GO.currentPlayer.toss(t2);
				if(numTiles == 3) {
					GO.currentPlayer.toss(t3);
				}
				GO.currentPlayer.fixHand();
			}
			
			GO.printAll();
			
			if(GO.mustWin) { //remember mustWin is set to true only when they attempt to take a tile for a double combo from the pot.
				if(!placedSuccessfully) { //if they could not successfully make a combo with the tile they took from the pot, ask them if they would like to try again.
					continueCombos = answerCheckString("Y", "N", "\nUnsuccessfully placed. Would you like to try placing a triple again? (Y/N): ");
					if(continueCombos.equalsIgnoreCase("N")) { //If they don't want to try again, undo the combos they did make and reset mustWin to false. (they could not take the tile from pot).
						GO.currentPlayer.PlacedCombos.undoAllCombos(GO);
						GO.mustWin=false;
					}
				} else { //if they did place the combo successfully, add it to the comboTiles stack.
					GO.comboTiles.push(GO.currentPlayer.PlacedCombos.tripleCombos[GO.currentPlayer.PlacedCombos.tripleComboCount-1]);
				}
			} else { 
				if(!placedSuccessfully) { //if they do not need to win, but did not successfully place their combo, let them know it was an invalid combo
					System.out.println("\nSorry, invalid combo.");
				} else { //if they successfully placed their combo, check if they won the game. If they did, return true which breaks the game loop in main and ends the game.
					if(GO.currentPlayer.ComboChecker.winChecker()) {
						return true;
					}
				}
				if(GO.currentPlayer.handSize > 1) { //Give the player the option to continue placing combos and re-enter this loop from the start if they still have more than 1 tile in their hand.
					continueCombos = answerCheckString("Y", "N", "\nWould you like to place anymore combos? (Y/N): ");
				} else {
					continueCombos = "N";
				}
			}
			//If they could not successfully place a combo but want to keep trying, reset their selected tiles so they can reselect them on the next run of the loop.
			t1 = 0;
			t2 = 0;
			t3 = 0;
		}
		//returns false until a winning combo is placed.
		return false;
	}
	
	//Player is asked if they would like to rearrange their tiles. They may do so as many times as they need.
	public void rearrangeAndPrint(GameObjects GO, Player currentPlayer) {
		String tempString = " ";
		int t1 = 0;
		int t2 = 0;
		tempString = answerCheckString("Y", "N", "\nWould you like to rearrange your tiles? (Y/N): ");

		while(tempString.equalsIgnoreCase("Y")) {
			t1 = 0;
			t2 = 0;
			while(t1 == t2) {
				t1 = answerCheckInt(1, currentPlayer.handSize, "What is the index of the first tile you want to move? (1-" + currentPlayer.handSize + "): ", scan);
				t2 = answerCheckInt(1, currentPlayer.handSize, "What is the index of the second tile you want to swap it with? (1-" + currentPlayer.handSize + "): ", scan);
			}
			//Tiles at index t1-1 and t2-1 in player's hand are swapped.
			currentPlayer.rearrange(t1, t2);
			GO.printAll();
			tempString = answerCheckString("Y","N","\nWould you like to rearrange anymore tiles? (Y/N): ");
		}
	}
	
	//Asks player which tile from their hand they would like to toss at the end of their turn.
	public int askToToss(int handSize) {
		int tossTile = answerCheckInt(1, handSize, "You must choose a tile to toss (1-" + handSize + "): ", scan);
		return tossTile;
	}
	
	//Repeats question until player enters either option1 or option2 as a response.
	private String answerCheckString(String option1, String option2, String question) {
		String tempString = " ";
		
		while(!tempString.equalsIgnoreCase(option1) && !tempString.equalsIgnoreCase(option2)){
			System.out.print(question);
			tempString = scan.nextLine();
		}
		
		return tempString;
	}
	
	//Repeats question until player enters a number between and including option1 and option2.
	private int answerCheckInt(int option1, int option2, String question, Scanner in) {
		int tempInt = 0;
		while(tempInt < option1 || tempInt > option2){
			System.out.print(question);
			tempInt = scanInt(in);
			while(tempInt == -1) {
				System.out.print(question);
				in.nextLine();
				tempInt = scanInt(in);
			}
		}
		in.nextLine();
		return tempInt;
	}
	
	//Ensures player enters an integer. Returns that integer.
    private int scanInt(Scanner in) {
		if(in.hasNextInt()) {
			int myInt = in.nextInt() ; 
			return myInt;
		}
		System.out.println("Please enter a number.");
		return -1;
    }
    
    private void printRules() {
		System.out.println(
				"\n\nMahjong Blitz is an adaptation on the game of Mahjong to allow for a competitive experience between\n"
				+ "only two players. This adaptation's defining feature is the addition a 'pocket'. The pocket is used to\n"
				+ "try and block your opponent from making their combos. The tiles in your pocket may never be used in\n"
				+ "your own combos, so there's no point is holding tiles you want there.\n"
				+ "\nMahjong consists of a deck of 136 tiles. Each player is dealt 13 tiles at the start of the game. The goal of the game is to get\n"
				+ "rid of your hand by placing all your tiles in successful combos. The first player to place their entire hand down wins.\n"
				+ "\n* TILES:\n\tIn Mahjong there are 4 copies of every tile. There are 2 types of tiles: Suited and Non-Suited\n"
				+ "\t\tThere are 3 types of suited tiles: Bun, Stick, and Thousand.\n"
				+ "\t\t\tEach suit has 9 unique tiles and are identified by their number 1-9 EX: 2 Bun or 9 Thousand\n"
				+ "\t\tThere are 7 types of non-suited tiles: North, East, South, West, Center, Mirror, and Lucky\n"
				+ "\t\t\tThe non-suited tiles do not have an accompanying number value and are identified only by their name EX: West or Mirror\n"
				+ "\n* COMBOS:\n\tIn order to place tiles from your hand, you must make a successful combo. Placed combos are displayed on the game\n"
				+ "\tboard and are visible to either player every turn. Combos come in 3 distinct types.\n"
				+ "\t\tType 1: Pong\n"
				+ "\t\t\tA Pong type combo is made with any 3 identical tiles. These can be either suited or non-suited.\n"
				+ "\t\tType 2: Chi\n"
				+ "\t\t\tA Chi type combo is a sequential combo of 3 tiles, and as such can only be made with suited tiles.\n"
				+ "\t\t\tA valid sequence is any 3 tiles that share the same suit AND have number values in ascending order. EX: 2 Bun, 3 Bun, 4 Bun\n"
				+ "\t\tType 3: Double\n"
				+ "\t\t\tA Double type combo is made with any 2 identical tiles. A double combo can only be placed with your winning hand.\n\n"
				+ "\tDuring each game, you may only use 2 out of the 3 types of suited tiles to make your combos. Once you make combos with 2 types (EX: Bun, Stick)\n"
				+ "\tthe third type (EX: Thousand) will be un-usable for any of your combos until the game is over. Any amount of non-suited tiles may be used\n"
				+ "\tin any game and they do not contribute to the 2 suit limit.\n"
				+ "\n* POT:\n\tThe pot is a discard pile and can be thought of as a tile graveyard. All tiles in the pot are visible to the player during\n"
				+ "\ttheir turn. At the start of your turn, you will have the option to take the most recent addition to the pot if you can use that tile in a\n"
				+ "\tcombo with tiles from your hand. If you do not choose to take that tile, or you can't make a combo with it at that time, it will remain\n"
				+ "\tin the pot for the rest of the game and will be completely inaccessable by any means.\n"
				+ "\n* HAND/POCKET:\n\tYour hand and pocket are both private and cannot be seen by the opponent. Your hand consists of 13 tiles and can be used\n"
				+ "\tto make combos while your pocket consists of only 5 tiles which cannot be used to make combos. The purpose of the pocket is to try to hold\n"
				+ "\ttiles that you think your opponent may want or need to make their combos.\n"
				+ "\n* STANDARD TURN:\n\tEach turn follows the same format (Steps 1-4).\n"
				+ "\t\tStep 1) You choose to receive a tile in one of 2 ways\n"
				+ "\t\t\tFirst way:\n"
				+ "\t\t\t\tIf it is not the first round, you will be asked if you want to take the most recent addition to the pot. You can only take this tile\n"
				+ "\t\t\t\tif you can make a combo with it. If you do take it, the game will prompt you to select the combo you would like to place using that tile.\n"
				+ "\t\t\t\tIf you successfully make a combo, then you will skip step (2) of your turn and go directly to step (3). If no combo can be made, the\n"
				+ "\t\t\t\ttile will return to the pot and you will be forced to receive a tile in the second way.\n"
				+ "\t\t\tSecond way:\n"
				+ "\t\t\t\tIf you choose not to take the tile from the pot or you can't make a combo with it, you will draw a fresh tile from the deck.\n"
				+ "\n\t\tStep 2) You choose to place your freshly drawn tile in either your hand or pocket.\n"
				+ "\t\t\tIf you add it to your pocket, it will ask you for the tile in your pocket to swap it with. The tile that you choose to remove from your\n"
				+ "\t\t\tpocket will automatically be tossed for step (4) of your turn. You will still continue to step (3) as normal.\n"
				+ "\t\t\tElse, if you add it to your hand, the tile will appear in your hand, and you will proceed to step (3).\n"
				+ "\n\t\tStep 3) At this point you have the opportunity to make combos with the tiles from your hand.\n"
				+ "\t\t\tIf you can win, do it now! ~tip: It is not always wise to place combos from your hand if you drew them naturally. Remember once you place\n"
				+ "\t\t\tyour combo, it will become visible to the other player on their turn as well.\n"
				+ "\n\t\tStep 4) At this time, you must choose a tile from your hand to toss. Tossing a tile adds it to the pot.\n"
				+ ""
				+ "\n* HOW TO WIN:\n\tTo win you must make four triple combos and one double combo. You may only make a double combo with the freshly tossed tile in the\n"
				+ "\tpot if it is the final tile you need to win.\n");
	}
    
    public void clearScreen() {
    	for(int i = 0; i < 100; i++) // Default Height of cmd is 300 and Default width is 80
    	    System.out.println(); // Prints a backspace
    }
    
    public void endGame(GameObjects GO) {
    	GO.printAll();
    	System.out.println("\n\n");
    	for(int i = 0; i<12; i++) {
    		for(int j = 0; j<15; j++) {
    			System.out.print("*~ *~ *~ ");
    		}
    		System.out.println();
    	}									
    	System.out.println("\n\t\t\t\t\t\t\t\t\tCongratulations " + GO.currentPlayer.playerName + "!! You won the game.\n");
    	for(int i = 0; i<12; i++) {
    		for(int j = 0; j<15; j++) {
    			System.out.print("*~ *~ *~ ");
    		}
    		System.out.println();
    	}
    }

}


