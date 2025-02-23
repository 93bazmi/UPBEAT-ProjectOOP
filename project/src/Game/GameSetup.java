package Game;

import Game.Player.GamePlayer;
import Game.Player.Player;
import Game.Region.GameRegion;
import Game.Region.Region;

import java.util.*;

public class GameSetup {

    private static long rows = 11;
    private static long cols = 9;

    private static long init_plan_min = 10;
    private static long init_plan_sec = 0;

    private static long init_budget = 5000;
    private static long init_center_dep = 1000;

    private static long plan_rev_min = 5;
    private static long plan_rev_sec = 0;

    private static long rev_Cost = 500;
    private static long max_dep = 2500;
    private static long interest_pct = 5;

    public static long getRows() {return rows;}
    public static long getCols() {return cols;}
    public static long getInit_budget() {return init_budget;}
    public static long getRev_Cost(){return rev_Cost;}
    public static long getMax_dep() {return max_dep;}
    public static long getInterest_pct() {return interest_pct;}
    public static long getInit_center_dep() { return init_center_dep;}
    private static List<Region> territory;


    public static GameState createGame(String p1, String p2){
        List<Region> territory = createMap();
        List<Player> players = new ArrayList<>();
        players.add(createPlayer(p1));
        players.add(createPlayer(p2));
        return new GameState(players , territory);
    }


    public static List<Region> createMap(){
        territory = new ArrayList<>();
        for(int i = 0; i < rows * cols; i++){
            Region region = new GameRegion(i);
            int col = i % (int) cols != 0 ? (i % (int) cols) + 1 : 1;
            int row = (i / (int) cols) + 1;
            region.SetLocation(row, col);
            territory.add(region);
        }
        return territory;
    }

    public static Region randomCityCenter(){
        Region region;
        Random random = new Random(); // random city-cen foreach player
        do {
            int location = random.nextInt(territory.size());
            region = territory.get(location);
        }while (region.getOwner() != null);
        return region;
    }

    public static Player createPlayer(String name){
        if(territory == null || territory.size() == 0) return null;
        Region region = randomCityCenter();
        Player player = new GamePlayer(name, init_budget, region);
        region.updateOwner(player);
        region.updateDeposit(init_center_dep);
        return player;
    }

}