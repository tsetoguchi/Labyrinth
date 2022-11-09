package game.referee.client;

import game.model.GameStatus;
import game.model.Position;
import referee.PlayerResult;
import referee.clients.PlayerClient;

/**
 * A mock player client for testing which keeps track of the messages it receives for comparison.
 */
public abstract class TestPlayerClient implements PlayerClient {
    public boolean informedOfGoalReached;
    public GameStatus finalGameResult;
    public PlayerResult finalPlayerResult;

    public TestPlayerClient() {
        this.informedOfGoalReached = false;
        this.finalGameResult = null; // these are null for testing purposes
        this.finalPlayerResult = null;
    }

    /** Record that this client was told to go home. **/
    @Override
    public void returnHome(Position homeTile) {
        this.informedOfGoalReached = true; // mark that this player was told to go home
    }

    /** Record results for comparison. **/
    @Override
    public void informGameEnd(GameStatus status, PlayerResult result) {
        this.finalGameResult = status;
        this.finalPlayerResult = result;
    }

    @Override
    public String getPlayerName() {
        return "Testy Joe";
    }

    @Override
    public boolean updateGoal(Position goal) {
        return true;
    }
}
