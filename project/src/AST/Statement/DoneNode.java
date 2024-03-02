package AST.Statement;

import Game.GameCommand;

import static AST.Node.*;

public class DoneNode extends StateNode {

    public boolean execute(GameCommand game) {
        return true;
    }
}
