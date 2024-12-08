import java.util.ArrayList;

public class ThreeCardLogic {

    public static int evalHand(ArrayList<Card> hand) {
        boolean flush = false;
        boolean strait = false;

        //sorts the list so it is easier to find straits
        int[] valueList = sortHand(hand);

        //Check for three of a kind
        if(valueList[0] == valueList[1] && valueList[0] == valueList[2]) {
            return 2; // if there are three of a kind then there is no way to get higher value hands
        }

        //checks for a flush
        if(hand.get(0).getSuit() == hand.get(1).getSuit() && hand.get(0).getSuit() == hand.get(2).getSuit()) {
            flush = true;
        }

        //checks for a strait
        if(valueList[0] == valueList[1]+1 && valueList[0] == valueList[2]+2) {
            strait = true;
        }
        else if(valueList[0] == 14 && valueList[1] == 3 && valueList[2] == 2) {
            strait = true;
        }

        //confirming for a strait flush, flush and strait
        if(flush && strait) {
            return 1;
        }
        else if(strait) {
            return 3;
        }
        else if(flush) {
            return 4;
        }

        //checking for a double
        if(valueList[0] == valueList[1] || valueList[1] == valueList[2]) {
            return 5;
        }

        return 0;

    }

    public static int evalPPWinnings(ArrayList<Card> hand,int bet) {
        switch(evalHand(hand)) {
            case 1:
                return bet*40;
            case 2:
                return bet*30;
            case 3:
                return bet*6;
            case 4:
                return bet*3;
            case 5:
                return bet;
            default:
                return 0;
        }
    }

    //compares the hands of the dealer and player and then returns an integer containing the result of the comparison
    //
    // outputs an integer signaling the result of the comparisons
    //
    // takes as an input two array lists of cards: one dealer and one player
    public static int compareHand(ArrayList<Card> dealer, ArrayList<Card> player) {
        //players have the same hand type
        if(evalHand(dealer) == evalHand(player)) {

            //sorts each hand from highest to lowest
            int[] sDealerH = sortHand(dealer);
            int[] sPlayerH = sortHand(player);

            if(evalHand(player) == 0) { //HighCard
                if(sDealerH[0] < 12) { //dealer has less than a queen
                    return 0;//Tie
                }
            }
            return compareThreeCards(sPlayerH,sDealerH);
        }
        //player only has a high card while dealer does not
        if(evalHand(player) == 0) {
            //dealer wins
            return 1;
        }
        //dealer only has a high card while the player does not
        if(evalHand(dealer) == 0) {
            //player wins
            return 2;
        }

        //checking who has higher hand type
        if(evalHand(player) > evalHand(dealer)) { //dealer wins
            return 1;
        }
        else { // player wins
            return 2;
        }
    }

    //sorts the values of the hand from smallest to largest
    //
    //takes an arraylist of cards
    //
    //returns an array of ints
    public static int[] sortHand( ArrayList<Card> hand) {
        //creates a three value array to be sorted and returned
        int[] valueArray = {hand.get(0).getValue(),hand.get(1).getValue(),hand.get(2).getValue()};

        //simple sorting algorithm to create a list from largest to smallest
        if (valueArray[0] < valueArray[1]) {
            int temp = valueArray[0];
            valueArray[0] = valueArray[1];
            valueArray[1] = temp;
        }
        if (valueArray[1] < valueArray[2]) {
            int temp = valueArray[1];
            valueArray[1] = valueArray[2];
            valueArray[2] = temp;
        }
        if (valueArray[0] < valueArray[1]) {
            int temp = valueArray[0];
            valueArray[0] = valueArray[1];
            valueArray[1] = temp;
        }
        return valueArray;
    }

    private static int compareThreeCards(int[] sPlayerH, int[] sDealerH) {

        if(sDealerH[0] == sPlayerH[0]) { //player and dealer have same highest card
            if(sDealerH[1] == sPlayerH[1]) { // checks if the second cards are the same
                if(sDealerH[2] == sPlayerH[2]) { //the hands are the same and are equal
                    return 0; //draw between dealer and player
                }
                else if(sDealerH[2] > sPlayerH[2]) { //dealer has higher third card and wins
                    return 1;//player loses
                }
                else { //player has higher third card and wins
                    return 2;//dealer loses
                }
            }
            else if(sDealerH[1] > sPlayerH[1]) { //dealer has higher second card and wins
                return 1;//player loses
            }
            else { //player has higher second card and wins
                return 2;//dealer loses
            }
        }
        else if(sDealerH[0] > sPlayerH[0]) { //dealer has higher first card and wins
            return 1;//player loses
        }
        else { //player has higher first card and wins
            return 2;//dealer loses
        }
    }


}
