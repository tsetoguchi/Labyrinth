package referee;

import observer.Controller.IObserver;

public interface IReferee {

    GameResults runGame();

    void addObserver(IObserver observer);

}