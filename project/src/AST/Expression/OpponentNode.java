package AST.Expression;

import Game.GameCommand;

import static AST.Node.*;

public class OpponentNode extends Expr {

    public long eval(GameCommand gamecmd) {
        return gamecmd.opponent();
    }


    public String toString() {
        return "opponent";
    }
}
