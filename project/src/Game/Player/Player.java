package Game.Player;

import Game.Region.Region;

import java.util.Map;

public interface Player {

    String getName();
    long getBudget();
    void updateBudget(long money);
    void updateCityCenter(Region to);
    int getCityCenterLocation();
    Region getCityCenter();
    boolean life();
    Map<String,Long> getIdentifiers();
}
