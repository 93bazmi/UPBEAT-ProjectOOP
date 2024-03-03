package Game;

import Game.Region.Region;

import java.util.Map;

public interface GameCommand {
    boolean attack(Direction dir, long v);
    boolean collect(long v);
    boolean invest(long eval);
    boolean relocate();
    long nearby(Direction dir);
    boolean move(Direction dir);
    long opponent();
    long getRow();
    long getCol();
    long getCityCrew_Row();
    long getCityCrew_Col();
    long getBudget();
    long getDeposit();
    long getInterest();
    long getMaxDeposit();
    long getRandom();
    Region getCityCrew();
    Map<String, Long> getIdentifiers();

}
