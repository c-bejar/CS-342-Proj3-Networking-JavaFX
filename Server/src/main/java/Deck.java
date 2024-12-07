import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card>{
    Deck() {
        createDeck();
        shuffleDeck();
    }

    void newDeck() {
        super.clear();
        createDeck();
        shuffleDeck();
    }

    void createDeck() {
        // loop per suit
        for(int i = 0; i < 4; i++) {
            // loop per card
            for(int j = 2; j <= 14; j++) {
                switch(i) {
                    case 0:
                        super.add(new Card('C', j));
                        break;
                    case 1:
                        super.add(new Card('H', j));
                        break;
                    case 2:
                        super.add(new Card('S', j));
                        break;
                    case 3:
                        super.add(new Card('D', j));
                        break;
                    default:
                        System.out.println("Unexpected value of i {Deck.java:19}");
                        break;
                }
            }
        }
    }

    void shuffleDeck() {
        Collections.shuffle(this);
    }
}