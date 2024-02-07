package AST.Expression;

import Game.Direction;
import Game.GameCommand;

import static AST.Node.*;

public class NearbyNode extends Expr {
    private final Direction dir;

    public NearbyNode(Direction dir) {
        this.dir = dir;
    }


    public long eval(GameCommand gamecmd) {
        return gamecmd.nearby(dir);
    }


    public String toString() {
        return "nearby " + dir;
    }
}
