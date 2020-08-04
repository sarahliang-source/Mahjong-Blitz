package mahjong;

public class Mahjong {
	
	public static void main(String[] args) {
		
		//Create GameObjects which holds 2 Players, their placed combos, and the deck and pot.
		GameObjects GO = new GameObjects();
		//GameEngine runs our game and holds all methods vital to the operation of a turn, set-up, and ending the game.
		GamePlay GameEngine =  new GamePlay();
		
		//This prints a menu and allows someone to play the game, read the rules, or quit the program directly. 
		GameEngine.printMenu();
		
		//Both players choose names, roll dice to decide who goes first, and have the opportunity to rearrange their hand at this time.
		GameEngine.setUpPlayers(GO);
		
		//This is the main game loop that represents 1 complete turn of play. It will loop until the game ends, in which case it will break from the loop and proceed with the endgame function.
		while(true) {
			GameEngine.alertPlayer(GO.currentPlayer);
			GO.printAll();
			GO.currentTile = GameEngine.takeOrDraw(GO); //takeOrDraw gives the player the option to take the fresh tile from the pot, or take a tile from the deck, and then returns that tile 
			if(GO.tookFromPot && !GameEngine.mustMakeCombo(GO)) { //IF you could not successfully make a combo with tile from pot, put tile back in pot and draw new
				GO.printAll();
				GO.mainPot.add(GO.currentPlayer.hand[GO.currentPlayer.handSize-1]);
				GO.currentPlayer.toss(GO.currentPlayer.handSize-1);
				GO.currentPlayer.fixHand();
				GameEngine.playerDraw(GO);
			}
			GO.printAll();
			if(GameEngine.makeCombos(GO)) {  //makeCombos will return true only if all combos needed to win are made, and will initiate breaking from the game loop.
				break; //The game will end here.
			}
			
			//If the player did not use their pocket (where swapping is already accounted for), then they will be prompted to toss 1 tile from their hand here.
			if(!GO.usedPocket) {
				GO.mainPot.add(GO.currentPlayer.toss(GameEngine.askToToss(GO.currentPlayer.handSize)));
				GO.currentPlayer.fixHand();
				GO.printAll();
			}
			
			//Players are given a chance to rearrange their hand as many times as they want, and their hand will be printed after every rearrangement.
			GameEngine.rearrangeAndPrint(GO, GO.currentPlayer);
			GO.nextTurn(); //GameObjects are updated to represent the next player's turn.
		}
		
		//Once broken out of the loop, the endGame function will display a nice graphic along with the winner of the game.
		GameEngine.endGame(GO);
		System.exit(0);
		}
		
	}
	
	
