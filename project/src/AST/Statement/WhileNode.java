package AST.Statement;

import Game.GameCommand;

public class WhileNode extends If_ElseNode {
    private int executionCount = 0;

    public WhileNode(Expr expression, StateNode statements) {
        super(expression, statements, null);
        if (trueNode == null)
            trueNode = this;
    }

    private StateNode getLastNode(StateNode node) {
        while (node != this && node != null) {
            if (node.nextState == this || node.nextState == null) return node;
            node = node.nextState;
        }
        return this;
    }

    @Override
    public boolean execute(GameCommand gamecmd) {
        if (super.condition.eval(gamecmd) > 0) {
            if (executionCount >= 10000)
                return nextState.execute(gamecmd);
            StateNode last = getLastNode(trueNode);
            if (last != this)
                last.nextState = this;
            executionCount++;
            return trueNode.execute(gamecmd);
        }
        return nextState.execute(gamecmd);
    }
}