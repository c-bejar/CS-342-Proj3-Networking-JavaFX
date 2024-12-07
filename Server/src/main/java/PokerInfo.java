import java.io.Serializable;

public class PokerInfo implements Serializable{
    Player player;
    Dealer dealer;

    PokerInfo() {
        player = new Player();
        dealer = new Dealer();
    }
}