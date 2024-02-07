package Parser;

import AST.Expression.BinaryOperateNode;
import AST.Expression.NearbyNode;
import AST.Node.*;
import AST.Statement.*;
import Game.Direction;
import Tokenizer.ExprTokenizer;
import Parser.ParserException.*;

import java.util.Arrays;
import java.util.List;

public class GameParser {

    /* Grammar!
    Plan → Statement+
    Statement → Command | BlockStatement | IfStatement | WhileStatement
    Command → AssignmentStatement | ActionCommand
    AssignmentStatement → <identifier> = Expression
    ActionCommand → done | relocate | MoveCommand | RegionCommand | AttackCommand
    MoveCommand → move Direction
    ***RegionCommand → invest Expression | collect Expression
    ***AttackCommand → shoot Direction Expression
    Direction → up | down | upleft | upright | downleft | downright
    BlockStatement → { Statement* }
    IfStatement → if ( Expression ) then Statement else Statement
    WhileStatement → while ( Expression ) Statement
    Expression → Expression + Term | Expression - Term | Term
    Term → Term * Factor | Term / Factor | Term % Factor | Factor
    Factor → Power ^ Factor | Power
    Power → <number> | <identifier> | ( Expression ) | InfoExpression
    InfoExpression → opponent | nearby Direction
     */

    private final ExprTokenizer tkz ;
    private final List<String> Commands = Arrays.asList("done","relocate","move","invest","collect","shoot");
    private final List<String> Variables = Arrays.asList("if", "while", "done", "relocate", "move", "invest", "shoot", "up", "down", "upleft", "upright", "downleft", "downright"
            , "if", "while", "then", "else", "opponent", "nearby"
            , "rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random"); // Special variables


    public GameParser(ExprTokenizer tkz) {
        if(!tkz.hasNextToken()){
            throw new NeedStatement();
        }
        this.tkz = tkz;
    }

    public StateNode Parse(){
        StateNode nodes = parsePlan();
        if(tkz.hasNextToken()){
            throw new Exception_AST.UnExceptTokenException(tkz.peek());
        }
        return nodes;
    }

    // 1. Plan → Statement+
    private StateNode parsePlan(){
        StateNode current = parseStatement();
        current.nextState = parseManyStatement();
        return current;
    }

    // 2. Statement → Command | BlockStatement | IfStatement | WhileStatement
    private StateNode parseStatement(){
        if(tkz.peek("if")) {
            return parseIfStatement();
        } else if (tkz.peek("while")) {
            return parseWhileStatement();
        } else if (tkz.peek("{")) {
            return parseBlockStatement();
        } else {
            return parseCommand();
        }
    }

    private StateNode parseManyStatement(){
        StateNode root = null , node = null ;
        while (!tkz.peek("}") && tkz.hasNextToken()) {
            StateNode current = parseStatement();
            if(root==null){
                root = current;
            }
            if (root!=null){
                node.nextState = current;
                node = current ;
            }
        }
        return root;
    }

    // 11. IfStatement → if ( Expression ) then Statement else Statement
    private StateNode parseIfStatement(){
        tkz.consume("if");
        tkz.consume("(");
        Expr expr = parseExpression();
        tkz.consume(")");
        tkz.consume("then");
        StateNode trueState = parseStatement();
        tkz.consume("else");
        StateNode flaseState = parseStatement();
        return new If_ElseNode(expr,trueState,flaseState); // IfElseNode on AST!
    }

    // 12. WhileStatement → while ( Expression ) Statement
    private StateNode parseWhileStatement() {
        tkz.consume("While");
        tkz.consume("{");
        Expr expr = parseExpression();
        tkz.consume("}");
        StateNode parse = parseStatement();
        return new WhileNode(expr, parse); // WhileNode on AST!
    }

    // 10. BlockStatement → { Statement* }
    private StateNode parseBlockStatement() {
        tkz.consume("{");
        StateNode parse = parseManyStatement();
        tkz.consume("}");
        return parse;
    }

    // 3. Command → AssignmentStatement | ActionCommand
    private StateNode parseCommand() {
        if (Commands.contains(tkz.peek()))
            return parseActionCommand();
        else
            return parseAssignmentStatement();
    }

    // 4. AssignmentStatement → <identifier> = Expression
    private StateNode parseAssignmentStatement() {
        String identifier = tkz.consume();
        if (Variables.contains(identifier))
            throw new specVarIdentifier(identifier);
        if (tkz.peek("="))
            tkz.consume();
        else
            throw new NoSuchCommand(identifier);
        Expr expr= parseExpression();
        return new AssignmentNode(identifier, expr); // AssignmentNode on AST!
    }

    // 5. ActionCommand → done | relocate | MoveCommand | RegionCommand | AttackCommand
    private StateNode parseActionCommand() {
        String command = tkz.consume();
        return switch (command) {
            case "done" -> new DoneNode();
            case "relocate" -> new RelocateNode();
            case "move" -> parseMoveCommand();
            case "invest" -> parseInvestCommand();
            case "collect" -> parseCollectCommand();
            case "shoot" -> parseShootCommand();
            default -> throw new NotImplemented(command);
        };
    }

    // 9. Direction → up | down | upleft | upright | downleft | downright
    private Direction parseDirection() {
        String direction = tkz.consume();
        return switch (direction) {
            case "up" -> Direction.Up;
            case "down" -> Direction.Down;
            case "upleft" -> Direction.UpLeft;
            case "upright" -> Direction.UpRight;
            case "downleft" -> Direction.DownLeft;
            case "downright" -> Direction.DownRight;
            default -> throw new InvalidDirection(direction);
        };
    }

    // 6. MoveCommand → move Direction
    private StateNode parseMoveCommand() {
        Direction direction = parseDirection();
        return new MoveNode(direction); // MoveNode on AST!
    }

    // 7. ***RegionCommand → invest Expression | collect Expression
    private StateNode parseInvestCommand() {
        Expr expr = parseExpression();
        return new InvestNode(expr); // InvestNode on AST!
    }

    private StateNode parseCollectCommand() {
        Expr expr = parseExpression();
        return new CollectNode(expr); // CollectNode on AST!
    }

    // 8.  ***AttackCommand → shoot Direction Expression
    private StateNode parseShootCommand() {
        Direction dir = parseDirection();
        Expr expr = parseExpression();
        return new AttackNode(expr,dir); // AttackNode on AST!
    }

    // 13. Expression → Expression + Term | Expression - Term | Term
    private Expr parseExpression() {
        Expr left = parseTerm();
        while (tkz.peek("+") || tkz.peek("-")) {
            String operator = tkz.consume();
            Expr right = parseTerm();
            left = new BinaryOperateNode(left, operator, right); // BinaryOptNode on AST!
        }
        return left;
    }

    // 14. Term → Term * Factor | Term / Factor | Term % Factor | Factor
    private Expr parseTerm() {
        Expr left = parseFactor();
        while (tkz.peek("*") || tkz.peek("/") || tkz.peek("%")) {
            String operator = tkz.consume();
            Expr right = parseFactor();
            left = new BinaryOperateNode(left, operator, right); // BinaryOptNode on AST!
        }
        return left;
    }

    // 15. Factor → Power ^ Factor | Power
    private Expr parseFactor() {
        Expr left = parsePower();
        while (tkz.peek("^")) {
            String operator = tkz.consume();
            Expr right = parseFactor();
            left = new BinaryOperateNode(left, operator, right); // BinaryOptNode on AST!
        }
        return left;
    }

    // 16. Power → <number> | <identifier> | ( Expression ) | InfoExpression
    private Expr parsePower() {
        if (Character.isDigit(tkz.peek().charAt(0))) {
            return null;
        } else if (tkz.peek("opponent") || tkz.peek("nearby")) {
            return parseInfoExpression();
        } else if (tkz.peek("(")) {
            tkz.consume("(");
            Expr expr = parseExpression();
            tkz.consume(")");
            return expr;
        }
        return null;
    }

    // 17. InfoExpression → opponent | nearby Direction
    private Expr parseInfoExpression() {
        if (tkz.peek("opponent")) {
            tkz.consume();
            return null; // EnemyNode on AST!
        } else if (tkz.peek("nearby")) {
            tkz.consume();
            Direction direction = parseDirection();
            return new NearbyNode(direction); // NearbyNode on AST
        } else {
            throw new InvalidInfoExpression(tkz.peek());
        }
    }
}
