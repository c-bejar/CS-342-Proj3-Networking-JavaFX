import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Dealer {
    Deck theDeck;
    ArrayList<Card> dealersHand;

    Dealer() {
        theDeck = new Deck();
        dealersHand = new ArrayList<>();
    }

    public ArrayList<Card> dealHand() {
        ArrayList<Card> hand = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            hand.add(theDeck.get(0));
            theDeck.remove(0);
        }

        // Sorts in ascending order of card's value
        Collections.sort(hand, new Comparator<Card>() {
            public int compare(Card one, Card two) {
                return one.value - two.value;
            }
        });

        resetDeck();

        return hand;
    }

    boolean resetDeck() {
        if(theDeck.size() <= 34) {
            theDeck.newDeck();
            return true;
        }
        return false;
    }
}
