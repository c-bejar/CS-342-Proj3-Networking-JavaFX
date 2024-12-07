import java.io.Serializable;

public class PokerInfo implements Serializable{
      char command; //either W,L,or,''
//    long winnings; //the winnings that will be sent to the client
//
//    //can contain card info represented by two chars (rank,suit)ex: 4H = 4 of hearts, full hand = 2D6H0C = 2 of diamonds, 6 of hearts, 10 of clubs
//    String hand;

    //can contain messages of what is going on(player lost) ex "player lost to the dealer"
    //String message;

    //not to be used by the server, will be sent from the client
    //public short anteBet;
    //public short PPbet;


//    PokerInfo(char command,String message,String Hand, long Winnings) {//sending everything
//        this.command = command;
//        this.message = message;
//        this.hand = Hand;
//        this.winnings = Winnings;
//    }

    PokerInfo(char command) {
        this.command = command;
    }


}