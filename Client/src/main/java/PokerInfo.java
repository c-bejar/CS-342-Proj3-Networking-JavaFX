import java.io.Serializable;

public class PokerInfo implements Serializable{
    //command to be sent to the server
    char command;

//    //not to be used in this implementation, will be sent from server
//    public String hand;
//    public String message;
//    public long winnings;
//
//    //input bets for the server
//    public short anteBet;
//    public short PPbet;

    PokerInfo(char command) {
        System.out.println("inside poker info");
        this.command = command;
    }

//    PokerInfo(char command, short anteBet, short PPbet) {
//        this.command = command;
//        this.anteBet = anteBet;
//        this.PPbet = PPbet;
//    }
}