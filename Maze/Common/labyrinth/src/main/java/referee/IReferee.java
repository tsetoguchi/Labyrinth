package referee;

import game.Controller.IObserver;
import game.model.GameResults;
import game.model.PrivateState;
import player.IPlayer;

import java.util.List;

public interface IReferee {

    GameResults runGame();

    void resume(PrivateState game, List<IPlayer> players);

    void addObserver(IObserver observer);

}