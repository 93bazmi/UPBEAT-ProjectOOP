package Game;

import java.util.Map;

public interface GameCommand {

    boolean attack(Direction dir, long v);

    boolean collect(long v);

    void invest(long eval);

    void relocate();

    long nearby(Direction dir);

    boolean move(Direction dir);

    long opponent();

    Map<String, Long> getIdentifiers();
    long getRow();
    long getCol();
    long getCityCrew_Row();
    long getCitiCrew_Col();
    long getBudget();
    long getDeposit();
    long getInterest();
    long getMaxDeposit();
    long getRandom();

}
