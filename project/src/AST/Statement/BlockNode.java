package AST.Statement;

import AST.Node.StateNode;
import Game.GameCommand;

import java.util.List;

public class BlockNode extends StateNode {
    private final List<StateNode> nodes;

    public BlockNode(List<StateNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public boolean execute(GameCommand command) {
        for(StateNode node : nodes){
            if(!node.execute(command)) return false;
        }
        return true;
    }
}