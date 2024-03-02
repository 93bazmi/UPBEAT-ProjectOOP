package AST.Statement;

import Game.GameCommand;
import Game.Direction;

import static AST.Node.*;

public class AttackNode extends StateNode {
    private final Expr expression;
    private final Direction direction;

    public AttackNode(Expr expression, Direction direction) {
        this.expression = expression;
        this.direction = direction;
    }

    @Override
    public boolean execute(GameCommand gamecmd) {
        return gamecmd.attack(
                direction,
                expression.eval(gamecmd)
        );
    }
}