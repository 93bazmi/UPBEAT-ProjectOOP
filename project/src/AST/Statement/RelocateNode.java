package AST.Statement;

import Game.GameCommand;

import static AST.Node.*;

public class RelocateNode extends StateNode {
    @Override
    public StateNode execute(GameCommand gamecmd) {
        gamecmd.relocate();
        return null;
    }
}