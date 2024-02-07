package AST;

import Game.GameCommand;

public abstract class Node {
    public abstract static class Expr extends Node {


        public abstract long eval(GameCommand command);
        public abstract String toString();
    }

    public abstract static class StateNode extends Node {

        public StateNode nextState;
        public abstract StateNode execute(GameCommand command);
    }
}
