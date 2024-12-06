import java.util.ArrayList;

public class Dealer {
    private Deck theDeck;
    private ArrayList<Card> dealersHand;

    //creates a new randomDeck
    public Dealer() {
        theDeck = new Deck();
        dealersHand = new ArrayList<>();
    }

    public ArrayList<Card> dealHand() {
        //if the deck is too small then it shuffles
        if(theDeck.size() < 35) {
            theDeck.newDeck();
        }
        //clears the previous hand and then inserts three new cards from the deck
        ArrayList<Card> newHand = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            //adds to dealer and removes from deck 3 cards
            newHand.add(theDeck.remove(0));
        }
        return newHand;
    }

    //sets the hand for the dealer when given an ArrayList
    public void setHand(ArrayList<Card> dealersHand) {
        this.dealersHand.clear(); //clears current hand
        this.dealersHand = dealersHand;// makes new hand the input hand
    }

    //getters
    public Deck getDeck() {
        return theDeck;
    }
    public ArrayList<Card> getDealersHand() {
        return dealersHand;
    }



}
