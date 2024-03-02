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
}
