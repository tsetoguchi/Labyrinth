package referee;

import game.controller.IObserver;
import game.model.PrivateGameState;
import referee.clients.RefereePlayerInterface;

import java.util.List;

public interface IReferee {

    void runGame();

    void resume(PrivateGameState game, List<RefereePlayerInterface> players);

    void addObserver(IObserver observer);

}