package referee;

import game.Controller.IObserver;
import game.model.GameResults;
import game.model.PrivateGameState;
import referee.clients.IPlayerInterface;

import java.util.List;

public interface IReferee {

    GameResults runGame();

    void resume(PrivateGameState game, List<IPlayerInterface> players);

    void addObserver(IObserver observer);

}