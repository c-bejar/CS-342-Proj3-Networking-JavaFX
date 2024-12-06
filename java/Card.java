public class Card {
    private int value;
    private char suit;
    private String imageURL;

    //constructor that doubles as a setter
    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
        this.imageULR = "/images/" + value + "of" + suit;
    }

    //getters for accessing object's value
    public char getSuit() {
        return suit;
    }
    public int getValue() {
        return value;
    }
    public String getImage() {
        return String;
    }

    public String output() {
        return suit + "" + value;
    }

    public 
}
