package referee;

import game.controller.IObserver;
import game.model.GameResults;
import game.model.PrivateGameState;
import referee.clients.RefereePlayerInterface;

import java.util.List;

public interface IReferee {

    GameResults runGame();

    void resume(PrivateGameState game, List<RefereePlayerInterface> players);

    void addObserver(IObserver observer);

}