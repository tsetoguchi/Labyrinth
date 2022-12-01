package referee;

import observer.Controller.IObserver;
import model.state.GameResults;
import model.state.IState;
import player.IPlayer;

import java.util.List;

public interface IReferee {

    GameResults runGame();

    void addObserver(IObserver observer);

}