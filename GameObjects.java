package mahjong;

import java.util.Stack;

public class GameObjects {
	public Deck mainDeck = new Deck();
	public Pot mainPot = new Pot();
	public Player player1 = new Player(mainDeck);
	public Player player2 = new Player(mainDeck);
	public Player currentPlayer;
	public Player opponentPlayer;
	public Tile currentTile;
	public Tile tempTile;
	public int currentTurn = 0;
	public boolean firstRound = true;
	public boolean tookFromPot = false;
	public boolean mustWin = false;
	public boolean usedPocket = false;
	public Stack<Tile[]> comboTiles = new Stack<>();
	
	public void setCurrentPlayer(Player cPlayer) {
		currentPlayer =  cPlayer;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setOpponentPlayer(Player oPlayer) {
		opponentPlayer =  oPlayer;
	}
	
	//Resets usedPocket and tookFromPot booleans to false for the upcoming turn. Swaps the current and opponent players without using an intermediary player.
	public void nextTurn() {
		usedPocket = false;
		tookFromPot = false;
		currentTurn++;
		currentTurn = currentTurn%2;
		if(currentTurn == 0) {
			currentPlayer  = player1;
			opponentPlayer = player2;
		}  else {
			currentPlayer = player2;
			opponentPlayer = player1;
		}
	}
	
	//Prints the pot, current player's hand and pocket, current player's placed combos, and opponent player's placed combos.
	public void printAll() {
		System.out.println(mainPot);
		currentPlayer.printHandAndPocket();
		currentPlayer.printCombos();
		opponentPlayer.printCombos();
		System.out.println();
	}

};
