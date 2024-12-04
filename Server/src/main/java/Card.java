public class Card {
    char suit;
    int value;

    Card(char suit, int value) {
        switch(suit) {
            case 'C':
            case 'D':
            case 'S':
            case 'H':
                this.suit = suit;
                break;
            default:
                this.suit = 'X';
                break;
        }
        if(value >= 2 && value <= 14) {
            this.value = value;
        } else {
            this.value = 0;
        }
    }
}
