public class Card {
    private int value;
    private char suit;

    //constructor that doubles as a setter
    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    //getters for accessing object's value
    public char getSuit() {
        return suit;
    }
    public int getValue() {
        return value;
    }

    public String output() {
        return suit + "" + value;
    }
}
