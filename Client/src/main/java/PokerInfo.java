import java.io.Serializable;

public class PokerInfo implements Serializable{
    char command; //either W,L,or,''
    long winnings = 0; //the winnings that will be sent to the client

    //not to be used by the server, will be sent from the client
    public short anteBet = 5;
    public short PPbet = 0;

    PokerInfo(char command) {
        this.command = command;
    }

    void setValues(short ante, short pp) {
        anteBet = ante;
        PPbet = pp;
    }
}