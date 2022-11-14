package referee;

import game.controller.IObserver;
import game.model.Game;
import game.model.PlayerAvatar;
import game.model.PrivateGameState;
import referee.clients.PlayerClient;

import java.util.List;
import java.util.Map;

public interface IReferee {

    void runGame();

    void resume(PrivateGameState game, List<PlayerClient> players);

    void addObserver(IObserver observer);

}