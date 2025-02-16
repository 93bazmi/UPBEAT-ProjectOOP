package Game;

import Game.Player.GamePlayer;
import Game.Player.Player;
import Game.Region.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

final class GameStateTest {
    List<Region> territory = GameSetup.createMap();
    Player p1 = new GamePlayer("Player 1" , GameSetup.getInit_budget() , territory.get(1));
    Player p2 = new GamePlayer("Player 2" , GameSetup.getInit_budget() , territory.get(10));
    public final List<Player> allPlayer = Arrays.asList(p1 , p2); // add Player 1 and 2 into allPlayer list
    GameState game = new GameState(allPlayer , territory); // setup GameState
    long Cost = 100;

    @BeforeEach
    public void addMoneyInCityCenter(){
        territory.get(1).updateOwner(allPlayer.get(0));
        territory.get(1).updateDeposit(GameSetup.getInit_budget());
        territory.get(10).updateOwner(allPlayer.get(1));
        territory.get(10).updateDeposit(GameSetup.getInit_budget());
    }


    @Test
    public void MoveToOpponentRegion(){
        game.startTurn();
        assertTrue(game.move(Direction.Right));
        game.move(Direction.Down);
        assertFalse(game.move(Direction.Down));
    }


    @Test
    public void MoveOutOfMap(){
        game.startTurn();
        int move;
        for(int i = 0; i < 5; i++){
            move = game.cal_newMove(Direction.Up, game.getCityCrew().getLocation(), game.getCityCrew().getCol());
            if(move == -1){
                assertFalse(game.move(Direction.Up));
            }else game.move(Direction.Up);
        }
    }


    @Test
    public void InvestTest(){
        game.startTurn();
        long budget = GameSetup.getInit_budget();
        long inv = 500;
        if(game.move(Direction.Down)) {
            if(game.invest(inv)) {
                assertEquals(game.getCityCrew().getDeposit(), inv);
                assertEquals(allPlayer.get(1).getBudget(), budget - inv - 2 * Cost);
            }
        }
    }


    @Test
    public void RelocateTest( ){
        game.startTurn();
        int location = game.getCityCrew().getLocation();
        for(int i = 0; i < 4; i++) if(game.move(Direction.DownRight)) {
            game.invest(100);
        }
        if(game.relocate()) {
            assertEquals(game.getCityCrew(), allPlayer.get(0).getCityCenter());
            assertNull(territory.get(location).getOwner());
        }
    }


    @Test
    public void CollectCityCenter(){
        game.startTurn();
        long budget = GameSetup.getInit_budget();
        long collect = 100;
        if(game.collect(collect)) {
            assertEquals(game.getCityCrew().getDeposit(), allPlayer.get(0).getCityCenter().getDeposit());
            assertEquals(allPlayer.get(0).getBudget(), collect + budget - Cost);
        }
    }


    @Test
    public void CollectNull(){
        game.startTurn();
        long money = GameSetup.getInit_budget();
        long collect = 0;
        long investment = 100;
        if(game.move(Direction.Up)) {
            game.invest(investment);
            game.collect(collect);
            assertEquals(allPlayer.get(0).getBudget(),  money - investment - 3 * Cost);
            assertEquals(game.getCityCrew().getDeposit(), investment);
        }
    }


    @Test
    public void AttackOwnRegionTest() {
        game.startTurn();
        long budget = GameSetup.getInit_budget();
        long inv = 100;
        if(game.move(Direction.Up)) {
            game.invest(inv);
            assertEquals(game.getCityCrew().getDeposit(), inv);
            assertEquals(allPlayer.get(0).getBudget(),  budget - inv - 2 * Cost);
            game.move(Direction.Down);
            if(game.attack(Direction.Up, inv)) {
                game.move(Direction.Up);
                assertNull(game.getCityCrew().getOwner());
            }
        }
    }


    @Test
    public void OpponentTest(){
        game.startTurn();
        assertEquals(12, game.opponent());
        game.move(Direction.UpRight);
        assertEquals(12, game.opponent());
        game.move(Direction.Down);
        assertEquals(12, game.opponent());
        game.move(Direction.Down);
        assertEquals(12, game.opponent());
        game.move(Direction.UpLeft);
        assertEquals(12, game.opponent());
    }


    @Test
    public void AttackToEnemyRegionTest(){
        game.startTurn();
        long inv = 100;
        long atkVal = 50;
        game.move(Direction.DownRight);
        game.invest(inv);
        game.endTurn();

        game.startTurn();
        if(game.attack(Direction.Down, atkVal)) {
            assertNotNull(territory.get(1).getOwner());
            assertEquals(territory.get(1).getOwner(), allPlayer.get(0));
        }
        if(game.attack(Direction.Down, atkVal))
            assertNull(territory.get(3).getOwner());
    }

    @Test
    public void NearByTest(){
        game.startTurn();
        long dist = game.nearby(Direction.Up);
        assertEquals(0, dist);
        game.move(Direction.Right);
        dist = game.nearby(Direction.Down);
        assertEquals(204, dist);
        dist = game.nearby(Direction.DownLeft);
        assertEquals(0, dist);
        game.move(Direction.Down);
        dist = game.nearby(Direction.UpLeft);
        assertEquals(0, dist);
    }

}