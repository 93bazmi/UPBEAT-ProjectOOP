package AST.Expression;

import Game.GameCommand;

import static AST.Node.*;

public class OpponentNode extends Expr {
    @Override
    public long eval(GameCommand gamecmd) {
        return gamecmd.opponent();
    }

    @Override
    public String toString() {
        return "opponent";
    }
}
