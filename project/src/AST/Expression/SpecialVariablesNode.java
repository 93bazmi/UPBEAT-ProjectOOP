package AST.Expression;

import AST.Node.Expr;
import AST.Statement.Exception_AST;
import Game.GameCommand;

public class SpecialVariablesNode extends Expr {
    private final String name;

    public SpecialVariablesNode(String name) {
        this.name = name;
    }

    public long eval(GameCommand gamecmd) {
        return  switch (name) {
            case "rows" -> gamecmd.getRow();
            case "cols" -> gamecmd.getCol();
            case "currow" -> gamecmd.getCityCrew_Row();
            case "curcul" -> gamecmd.getCityCrew_Col();
            case "budget" -> gamecmd.getBudget();
            case "deposit" -> gamecmd.getDeposit();
            case "int" -> gamecmd.getInterest();
            case "maxdeposit" -> gamecmd.getMaxDeposit();
            case "random" -> gamecmd.getRandom();
            default -> throw new Exception_AST.UnknownOperator(name);
        };
    }

    @Override
    public String toString() {
        return null;
    }
}
