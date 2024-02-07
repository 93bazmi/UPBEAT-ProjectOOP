package AST.Statement;

import Game.GameCommand;
import AST.Node.*;

public class InvestNode extends StateNode {
    private final Expr expr;

    public InvestNode(Expr expr) {
        this.expr = expr;
    }

    @Override
    public StateNode execute(GameCommand gamecmd) {
        gamecmd.collect(expr.eval(gamecmd));
        return nextState;
    }
}
