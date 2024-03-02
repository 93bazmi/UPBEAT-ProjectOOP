package Parser;

import AST.Node.StateNode;

import java.util.List;

public interface Parser {
    List<StateNode> Parse();
}
