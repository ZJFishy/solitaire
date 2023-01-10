/*      Deck class
 *      Contains a deck of cards, which can be shuffled and dealt from
 *      The card's suit can be found by dividing its ID number by 13
 *      The card's number can be found by modding its ID number by 13 and adding 1
 */

import java.util.Random;

class Deck {
	private static int[] cards;
	private static int sizeOfDeck;

	public Deck(){
		for (int i = 0; i < 52; i++){
			cards[i] = i + 1;
		}
		sizeOfDeck = 52;
	}

	// Shuffle the remaining deck randomly
	public void shuffleDeck(){
		Random rng = new Random();
		int temp;
		for (int i = 52 - sizeOfDeck; i < 52; i++){
			int switchPosition = rng.nextInt(52 - sizeOfDeck, 52);
			temp = cards[i];
			cards[i] = cards[switchPosition];
			cards[switchPosition] = temp;
		}
	}

	/**	Deal the top card of the deck when possible
	 * @return	The integer value of the top card in the deck
	 */
	public int dealCard(){
		if (sizeOfDeck == 0){
			System.out.println("Empty deck");
			return 0;
		}
		sizeOfDeck -= 1;
		return cards[51 - sizeOfDeck];
	}

	// Add cards back to the deck
	public void returnToDeck(int[] cardsBack){
		// Make sure deck can hold all current and new cards
		// Shift current cards forward
		// Add new cards to rear
		// Adjust sizeOfDeck
	}

	/** Check if all the cards have been dealt
	 * 
	 */
	public boolean isEmpty(){
		return (sizeOfDeck == 0);
	}
}