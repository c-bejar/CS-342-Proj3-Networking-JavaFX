import java.io.Serializable;
import java.util.ArrayList;

public class PokerInfo implements Serializable {
    char command; //either W,L,or,''
    long winnings = 0; //the winnings that will be sent to the client
    int winResult = -1;

    //not to be used by the server, will be sent from the client
    public short anteBet = 5;
    public short PPbet = 0;
    int ppWinnings = 0;

    ArrayList<String> playerHand;
    ArrayList<String> dealerHand;

    PokerInfo(char command) {
        this.command = command;
    }

    PokerInfo(char command, short ante, short pp) {
        this.command = command;
        anteBet = ante;
        PPbet = pp;
    }

    PokerInfo(char command, ArrayList<String> p, ArrayList<String> d) {
        this.command = command;
        playerHand = p;
        dealerHand = d;
    }

    PokerInfo(char command, ArrayList<String> p, short pp) {
        this.command = command;
        playerHand = p;
        PPbet = pp;
    }

    void setValues(short ante, short pp) {
        anteBet = ante;
        PPbet = pp;
    }
}