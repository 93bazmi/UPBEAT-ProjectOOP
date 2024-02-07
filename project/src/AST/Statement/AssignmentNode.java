package AST.Statement;

import Game.GameCommand;

import java.util.Map;

import static AST.Node.*;

public class AssignmentNode extends StateNode {
    private final String identifier;
    private final Expr expression;

    public AssignmentNode(String identifier, Expr expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    public StateNode execute(GameCommand gamecmd) {
        Map<String, Long> memory = gamecmd.getIdentifiers();
        memory.put(identifier, expression.eval(gamecmd));
        return nextState;
    }
}