import java.util.ArrayList;
import java.util.Random;

public class Deck extends ArrayList<Card> {

    //deck creates the deck cards and stores it as a part of the class.
    public Deck() {
        //calls new deck to create a new deck with a random order
        newDeck();
    }

    //newDeck shuffles the deck to create a new deck to play with.
    public void newDeck() {
        this.clear();

        //standard ordered deck creation
        char[] suits = new char[]{'C', 'D', 'S', 'H'};
        for(int i = 0; i < 4; i++) {
            for(int j = 2; j < 15; j++) {
                Card currCard = new Card(suits[i],j);
                this.add(currCard);
            }
        }

        //randomization of the newly created deck
        Random rand = new Random();
        for(int i = 0; i < 52; i++) {
            int r = i + rand.nextInt(52 - i);

            Card temp = this.get(r);
            this.set(r,this.get(i));
            this.set(i, temp);
        }
    }

}
