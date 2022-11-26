package referee;

import game.Controller.IObserver;
import game.model.GameResults;
import game.model.PrivateGameState;
import player.IPlayer;

import java.util.List;

public interface IReferee {

    GameResults runGame();

    void resume(PrivateGameState game, List<IPlayer> players);

    void addObserver(IObserver observer);

}