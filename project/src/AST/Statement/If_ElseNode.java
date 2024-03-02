package AST.Statement;

import AST.Node.*;
import Game.GameCommand;

public class If_ElseNode extends StateNode {
    protected final Expr condition;
    protected StateNode trueNode;
    protected StateNode falseNode;

    public If_ElseNode(Expr condition, StateNode trueNode, StateNode falseNode) {
        this.condition = condition;
        this.trueNode = trueNode;
        this.falseNode = falseNode;
    }

    @Override
    public boolean execute(GameCommand gamecmd) {
        trueNode.nextState = nextState;
        falseNode.nextState = nextState;
        if(condition.eval(gamecmd) > 0){
            return trueNode.execute(gamecmd);
        } else {
            return falseNode.execute(gamecmd);
        }
    }
}