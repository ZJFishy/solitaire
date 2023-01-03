import java.util.Random;
import java.util.random;

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
    public static void shuffleDeck(){
        Random rng = new Random();
        int temp;
        for (int i = 52 - sizeOfDeck; i < 52; i++){
            int switchPosition = rng.nextInt(52 - sizeOfDeck, 52);
            temp = cards[i];
            cards[i] = cards[switchPosition];
            cards[switchPosition] = temp;
        }
    }

    // Deal the top card of the deck
    public static int dealCard(){
        sizeOfDeck -= 1;
        return cards[51 - sizeOfDeck];
    }
}