import java.io.Serializable;

public class Card implements Serializable{
    char suit;
    int value;

    Card(char suit, int value) {
        this.suit = suit;
        
        if(value >= 2 && value <= 14) {
            this.value = value;
        } else {
            this.value = 0;
        }
    }
}
