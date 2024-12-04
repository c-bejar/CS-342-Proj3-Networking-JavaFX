import java.util.ArrayList;

public class ThreeCardLogic {
    public static int evalHand(ArrayList<Card> hand) {
        Card one = hand.get(0);
        Card two = hand.get(1);
        Card three = hand.get(2);

        boolean flush = one.suit == two.suit && two.suit == three.suit;
        boolean straight = one.value == two.value+1 && two.value == three.value+1 ||
                           one.value == two.value-1 && two.value == three.value-1;
        boolean threeOfAKind = one.value == two.value && two.value == three.value;
        boolean pair = one.value == two.value ||
                       one.value == three.value ||
                       two.value == three.value;
        
        if(straight && flush) {
            return 1;
        }
        if(threeOfAKind) {
            return 2;
        }
        if(straight) {
            return 3;
        }
        if(flush) {
            return 4;
        }
        if(pair) {
            return 5;
        }
        // hand just has a high card
        return 0;
    }

    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        int evaluatedHand = evalHand(hand);
        // Pair              1 to 1
        // Flush             3 to 1
        // Straight          6 to 1
        // Three of a Kind  30 to 1
        // Straight Flush   40 to 1
        switch(evaluatedHand) {
            case 5:
                return bet;
            case 4:
                return bet * 3;
            case 3:
                return bet * 6;
            case 2:
                return bet * 30;
            case 1:
                return bet * 40;
            default:
                return 0;
        }
    }

    public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
        int dealerScore = evalHand(dealer);
        int playerScore = evalHand(player);
        if(playerScore == 0) {
            playerScore = 6;
        }else if(dealerScore == 0) {
            dealerScore = 6;
        }

        if(playerScore < dealerScore) {
            return 2;
        }
        if(dealerScore < playerScore) {
            return 1;
        }
        if(playerScore == dealerScore){
            // If flush [same suits] or Highest Card
            if(playerScore == 2 || playerScore == 0) {
                // evaluate last card in sorted hand
                if(player.get(2).value > dealer.get(2).value) {
                    return 2;
                }
                if(dealer.get(2).value > player.get(2).value) {
                    return 1;
                }
            }
            // Evaluate the middle card in sorted hand (Works for all except flush & highest card)
            if(player.get(1).value > dealer.get(1).value) {
                return 2;
            }
            if(dealer.get(1).value > player.get(1).value){
                return 1;
            }
        }
        return 0;
    }
}
