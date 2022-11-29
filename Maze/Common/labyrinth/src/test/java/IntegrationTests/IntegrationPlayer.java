package IntegrationTests;

import game.model.IBoard;
import game.model.GameStatus;
import game.model.Position;
import game.model.projections.StateProjection;
import player.Turn;
import referee.PlayerResult;

import java.util.Optional;
import player.IPlayer;

public class IntegrationPlayer implements IPlayer {
    private IPlayer player;
    private PlayerResult result;

    public IntegrationPlayer(IPlayer player) {
        this.player = player;
        this.result = null; // null for testing
    }

    @Override
    public Optional<Turn> takeTurn(StateProjection game) {
        return player.takeTurn(game);
    }


    public void returnHome(Position homeTile) {
        this.player.updateGoal(homeTile);
    }


    public void informGameEnd(GameStatus status, PlayerResult result) {
        this.result = result;
//        if (result.equals(PlayerResult.WINNER)) {
//            this.player.win(true);
//        }
//        else {
//            this.player.win(false);
//        }
    }


    @Override
    public String getPlayerName() {
        return player.getPlayerName();
    }

    @Override
    public boolean updateGoal(Position goal) {
        return this.player.updateGoal(goal);
    }

    public PlayerResult getResult() {
        return this.result;
    }

    @Override
    public boolean setup(Optional<StateProjection> game, Position goal) {
        return this.player.setup(game, goal);
    }

    @Override
    public boolean win(boolean playerWon) {
        return this.player.win(playerWon);
    }

    @Override
    public IBoard proposeBoard(int rows, int columns) {
        return null;
    }
}
