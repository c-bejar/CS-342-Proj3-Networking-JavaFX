import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand;
    private int anteBet;
    private int playBet;
    private int pairPlusBet;
    private int totalWinnings;

    public Player() {
        hand = new ArrayList<Card>();
        anteBet = 5;
        playBet = 5;
        pairPlusBet = 0;
        totalWinnings = 0;
    }

    //sets the AnteBet with a value in the acceptable range
    public Boolean setAnteBet(int anteBet) {
        if(anteBet < 5) {//if ante bet is within the acceptable range
            this.anteBet = 5;
            this.playBet = 5;
            return false;
        }
        else if(anteBet > 25) {
            this.anteBet = 25;
            this.playBet = 25;
            return false;
        }
        else {
            this.anteBet = anteBet;
            this.playBet = anteBet;
            return true;
        }
    }

    //sets the bet of the player
    public Boolean setPairPlusBet(int ppBet) {
        if(ppBet < 5) {//if ante bet is within the acceptable range
            this.pairPlusBet = 0;
            return false;
        }
        else if(ppBet >= 25) {
            this.pairPlusBet = 25;
            return false;
        }
        else {
            this.pairPlusBet = ppBet;
            return true;
        }
    }

    //sets the hand of the player
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    //clears the players bets and hand for the next session of play
    public void resetBets() {
        hand.clear();
        anteBet = 5;
        playBet = 5;
        pairPlusBet = 0;
    }

    //resets the players bets, hand, and winnings
    public void resetAll() {
        hand.clear();
        anteBet = 5;
        playBet = 5;
        pairPlusBet = 0;
        totalWinnings = 0;
    }

    //getters for each of the data values
    public int getAnteBet() {
        return anteBet;
    }
    public int getPlayBet() {
        return playBet;
    }
    public int getPairPlusBet() {
        return pairPlusBet;
    }
    public int getTotalWinnings() {
        return totalWinnings;
    }
    public ArrayList<Card> getHand() {
        return hand;
    }

    //function to modify totalWinnings
    public void addToWinnings(int change) {
        totalWinnings = totalWinnings + change;
    }
}
