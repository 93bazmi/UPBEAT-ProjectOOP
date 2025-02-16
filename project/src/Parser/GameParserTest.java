package Parser;

import static AST.Node.StateNode.*;

import Tokenizer.ExprTokenizer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static Parser.ParserException.*;

public class GameParserTest {
    public GameParser parser;
    public StateNode node;

    @Test
    public void testNoStatement() {
        assertThrows(NeedStatement.class, () -> new GameParser(new ExprTokenizer(null)));
        assertThrows(NeedStatement.class, () -> new GameParser(new ExprTokenizer("")));
    }

    @Test
    public void testExpression() {
        parser = new GameParser(new ExprTokenizer("123+321-213"));
        assertThrows(NoSuchCommand.class, parser::Parse);
    }
    @Test
    public void testUnknownWord() {
        parser = new GameParser(new ExprTokenizer("Watch this! pew pew pew!"));
        assertThrows(NoSuchCommand.class, parser::Parse);
    }
    @Test
    public void testSpecWord() {
        parser = new GameParser(new ExprTokenizer("nearby=10000000"));
        assertThrows(specVarIdentifier.class, parser::Parse);
    }
        //    public void testStatement() {
//        gameParser = new GameParser(new ExprTokenizer("a=1 b=2 c=3 d=4 e=5"));
//        stateNode = gameParser.Parse();
//        assertInstanceOf(AssignmentNode.class, stateNode.get(0));
//        assertInstanceOf(AssignmentNode.class, stateNode.get(1));
//        assertInstanceOf(AssignmentNode.class, stateNode.get(2));
//        assertInstanceOf(AssignmentNode.class, stateNode.get(3));
//        assertInstanceOf(AssignmentNode.class, stateNode.get(4));
//    }
        @Test
        public void testReadFile() {
            String str = """
                            t = t + 1  # keeping track of the turn number
                                 m = 0  # number of random moves
                                 while (deposit) { # still our region
                                   if (deposit - 100)
                                   then collect (deposit / 4)  # collect 1/4 of available deposit
                                   else if (budget - 25) then invest 25
                                   else {}
                                   if (budget - 100) then {} else done  # too poor to do anything else
                                   opponentLoc = opponent
                                   if (opponentLoc / 10 - 1)
                                   then  # opponent afar
                                     if (opponentLoc % 10 - 5) then move downleft
                                     else if (opponentLoc % 10 - 4) then move down
                                     else if (opponentLoc % 10 - 3) then move downright
                                     else if (opponentLoc % 10 - 2) then move right
                                     else if (opponentLoc % 10 - 1) then move upright
                                     else move up
                                   else if (opponentLoc)
                                   then  # opponent adjacent to city crew
                                     if (opponentLoc % 10 - 5) then {
                                       cost = 10 ^ (nearby upleft % 100 + 1)
                                       if (budget - cost) then shoot upleft cost else {}
                                     }
                                     else if (opponentLoc % 10 - 4) then {
                                       cost = 10 ^ (nearby downleft % 100 + 1)
                                       if (budget - cost) then shoot downleft cost else {}
                                     }
                                     else if (opponentLoc % 10 - 3) then {
                                       cost = 10 ^ (nearby down % 100 + 1)
                                       if (budget - cost) then shoot down cost else {}
                                     }
                                 
                    """;
            parser = new GameParser((new ExprTokenizer(str)));
            assertDoesNotThrow(() -> parser.Parse());
        }

    @Test
    public void TestReadFile() throws IOException {
        Path fileName = Path.of("C:\\Users\\Noppol Nunpiang\\IdeaProjects\\UPBEAT-ProjectOOP\\project\\src\\Parser\\constructor_plan.text");
        String str = Files.readString(fileName);
        parser = new GameParser((new ExprTokenizer(str)));
        assertDoesNotThrow(() -> parser.Parse());
    }

}
