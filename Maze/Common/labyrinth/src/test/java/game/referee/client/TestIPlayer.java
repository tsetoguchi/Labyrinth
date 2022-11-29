package model.referee.client;

import model.state.GameStatus;
import model.model.Position;
import model.projections.StateProjection;
import referee.PlayerResult;
import player.IPlayer;

import java.util.Optional;

/**
 * A mock player client for testing which keeps track of the messages it receives for comparison.
 */
public abstract class TestIPlayer implements IPlayer {
    public boolean informedOfGoalReached;
    public GameStatus finalGameResult;
    public PlayerResult finalPlayerResult;

    public TestIPlayer() {
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
    public boolean win(boolean playerWon) {
        return true;
    }

    @Override
    public boolean setup(Optional<StateProjection> game, Position goal) {
        return true;
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
