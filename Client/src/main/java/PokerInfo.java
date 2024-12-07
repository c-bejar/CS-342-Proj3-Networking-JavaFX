import java.io.Serializable;

public class PokerInfo implements Serializable{
    public int count;
    public String command;
    public String playerBets;
    public boolean foldOrPlay;

    PokerInfo(int count) {
        this.count = count;
    }
}
