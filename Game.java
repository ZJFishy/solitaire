/*		Game class
 * 		Piles hold the ints of the cards, top ones at the front
 * 		Suits hold the ints of the cards, higher ones at the front
 */

import java.util.*;

public class Game {
	private static int hand, moveCount;
	private static ArrayList<Integer> previousDraws;
	private static int[] pile1, pile2, pile3, pile4, pile5, pile6, pile7, cardsRevealed = {1, 1, 1, 1, 1, 1, 1};
	private static int[] suit1, suit2, suit3, suit4;
	private static int[][] piles = {pile1, pile2, pile3, pile4, pile5, pile6, pile7}, suitStacks = {suit1, suit2, suit3, suit4};
	private static Deck gameDeck;

	public Game(){
		// Initializing the piles and hand
		pile1 = new int[1];
		pile2 = new int[2];
		pile3 = new int[3];
		pile4 = new int[4];
		pile5 = new int[5];
		pile6 = new int[6];
		pile7 = new int[7];
		hand = 0;
		previousDraws = new ArrayList<Integer>();
		moveCount = 0;

		// Creating the deck, shuffling, and distributing into the proper piles
		gameDeck = new Deck();
		for (int i = 0; i < 5; i++){gameDeck.shuffleDeck();}
		for (int level = 0; level < 7; level++){
			for (int pile = level; pile < 7; pile++){
				piles[pile][level] = gameDeck.dealCard();
			}
		}
	}

	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		while(!checkWin()){
			printBoard();
			System.out.println("Enter the number of the pile you want to pull from");
			int pileFrom = input.nextInt();
			System.out.println("Enter how many cards you would like to take off the pile");
			int depth = input.nextInt();
			System.out.println("Enter the number of the pile you want to put the cards in");
			int pileTo = input.nextInt();
			moveCard(pileFrom, pileTo, depth);
		}
	}

	/**
	 * Puts new card from deck into hand
	 */
	
	public static void drawFromDeck(){
		moveCount += 1;
		// If deck is empty, return unused pile to deck
		if (gameDeck.isEmpty()){
			Integer[] tempIntegers = previousDraws.toArray(new Integer[0]);
			int[] tempCards = new int[tempIntegers.length];
			for (int i = 0; i < tempCards.length; i++){
				tempCards[i] = tempIntegers[i].intValue();
			}
			gameDeck.returnToDeck(tempCards);
			while(previousDraws.size() > 0){
				previousDraws.remove(0);
			}
			return;
		}

		// Put previous into previous draws
		previousDraws.add(hand);

		// Put new card in hand from deck
		hand = gameDeck.dealCard();
	}

	/**
	 * Moves one card from one pile or hand to a different pile
	 * @param fromPile 	the pile the card(s) is/are being taken from
	 * @param toPile 	the pile the card(s) is/are going to
	 * @param depth 	how many cards should be moved
	 */

	public static void moveCard(int fromPile, int toPile, int depth){
		fromPile -= 1;
		toPile -= 1;
		
		// Handles moving to suit stacks
		if (toPile >= 10 && depth == 1){
			if (piles[fromPile][0] / 13 == toPile - 10 && piles[fromPile][0] % 13 - 1 == suitStacks[toPile - 10][0]){
				moveCount += 1;
				int movingCard = piles[fromPile][0];

				// Move the now-smaller pile to an appropriately sized one
				int[] downsizePile = new int[piles[fromPile].length - 1];
				for (int i = 0; i < downsizePile.length; i++){
					downsizePile[i] = piles[fromPile][i+1];
				}
				piles[fromPile] = downsizePile;

				// Move the card to the top of the suit stack
				int[] upsizePile = new int[suitStacks[toPile - 10].length + 1];
				upsizePile[0] = movingCard;
				for (int i = 0; i < upsizePile.length - 1; i++){
					upsizePile[i+1] = suitStacks[toPile - 10][i + 1];
				}
				suitStacks[toPile - 10] = upsizePile;
			}
			else {
				System.out.println("Invalid move");
			}
			return;
		}

		// Handles all other movement
		else if (fromPile < 8 && toPile < 7 && fromPile != toPile  && ((piles[fromPile].length >= depth && depth <= cardsRevealed[fromPile]) || (fromPile == 7 && depth == 1))){
			// Handles playing from hand
			if (fromPile == 7){
				moveCount += 1;
				int[] tempPile = new int[piles[toPile].length + 1];
				tempPile[0] = hand;
				hand = previousDraws.remove(previousDraws.size());
				for (int i = 0; i < piles[toPile].length; i++){
					tempPile[i + 1] = piles[toPile][i];
				}
				piles[toPile] = tempPile;
				return;
			}

			// Make sure the deepest card being moved can be placed on top card of the toPile
			if (!((piles[fromPile][depth] / 13 > 1 && piles[toPile][0] / 13 < 2) || (piles[fromPile][depth] / 13 < 2 && piles[toPile][0] / 13 > 1))){
				System.out.println("Invalid move");
				return;
			}

			moveCount += 1;

			// Isolate the cards being moved in a new pile
			int[] movingPile = new int[depth];
			for (int i = 0; i < depth; i++){
				movingPile[i] = piles[fromPile][i];
			}
			cardsRevealed[fromPile] = Math.max(1, cardsRevealed[fromPile] - depth);

			// Move the now-smaller pile to an appropriately sized one
			int[] downsizePile = new int[piles[fromPile].length - depth];
			for (int i = 0; i < downsizePile.length; i++){
				downsizePile[i] = piles[fromPile][i + depth];
			}
			piles[fromPile] = downsizePile;

			// Add the moved cards and toPile to a new pile to replace toPile
			int[] upsizePile = new int[depth + piles[toPile].length];
			for (int i = 0; i < depth; i++){
				upsizePile[i] = movingPile[i];
			}
			for (int i = 0; i < piles[toPile].length; i++){
				upsizePile[i + depth] = piles[toPile][i];
			}
			piles[toPile] = upsizePile;
		}

		else {
			System.out.println("Invalid move");
		}
	}

	/**
	 * Checks if the player has won the game
	 * @return			A boolean representing the game win state
	 */
	public static boolean checkWin(){
		for (int i = 0; i < 4; i++){
			if (suitStacks[i].length != 13){return false;}
		}
		return true;
	}

	/**
	 * 
	 */
	public static void printBoard(){
		int row = 1;
		while (printContinue(row)){
			for (int i = 0; i < 7; i++){
				if (piles[i].length - row >= 0){
					if (piles[i].length - row <= cardsRevealed[i]){
						String suit = "";
						switch (piles[i][piles[i].length - row] / 13) {
							case 0:
								suit = " of hearts";
								break;
							
							case 1:
								suit = " of diamonds";
								break;
							
							case 2:
								suit = " of clubs";
								break;
							
							case 3:
								suit = " of spades";
								break;
							
							default:
								break;
						}
						int rank = piles[i][piles[i].length - row] % 13 + 1;
						String rankString = "";
						switch (rank) {
							case 0:
							case 1:
							case 2:
							case 3:
							case 4:
							case 5:
							case 6:
							case 7:
							case 8:
							case 9:
							case 10:
								rankString = String.valueOf(rank);
								break;

							case 11:
								rankString = "J";
								break;

							case 12:
								rankString = "Q";
								break;

							case 13:
								rankString = "K";
								break;

							default:
								break;
							}
						System.out.print("| " + rankString + suit + "\t | ");
					}
					else {
						System.out.print(" |\t\t\t | ");
					}
				}
				if (row == 1){
					// Print the hand
				}
				System.out.println();
			}
		}
	}

	/** 
	 * Checks whether printing should continue based on the lengths of the piles
	 * @param rowIn	The current row to be printed
	 * @return		Whether the printing needs to continue
	 */
	public static boolean printContinue(int rowIn){
		for (int i = 0; i < 7; i++){
			if (piles[i].length >= rowIn){
				return true;
			}
		}
		return false;
	}
}
