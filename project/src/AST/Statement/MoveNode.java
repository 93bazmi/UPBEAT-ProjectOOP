package AST.Statement;

import Game.GameCommand;
import Game.Direction;

import static AST.Node.*;

public class MoveNode extends StateNode {
    private final Direction direction;

    public MoveNode(Direction direction) {
        this.direction = direction;
    }

    @Override
    public StateNode execute(GameCommand gamecmd) {
        gamecmd.move(direction);
        return nextState;
    }
}