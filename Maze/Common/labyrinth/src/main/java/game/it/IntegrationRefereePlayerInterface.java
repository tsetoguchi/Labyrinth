package game.it;

import game.model.GameStatus;
import game.model.Position;
import game.model.projections.PlayerGameProjection;
import player.Player;
import player.TurnPlan;
import referee.PlayerResult;
import referee.clients.RefereePlayerInterface;

import java.util.Optional;

public class IntegrationRefereePlayerInterface implements RefereePlayerInterface {
    private Player player;
    private PlayerResult result;

    public IntegrationRefereePlayerInterface(Player player) {
        this.player = player;
        this.result = null; // null for testing
    }

    @Override
    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {
        return player.takeTurn(game);
    }

    @Override
    public void returnHome(Position homeTile) {
        this.player.updateGoal(homeTile);
    }

    @Override
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
        return player.getName();
    }

    @Override
    public boolean updateGoal(Position goal) {
        return this.player.updateGoal(goal);
    }

    public PlayerResult getResult() {
        return this.result;
    }

    @Override
    public boolean setup(PlayerGameProjection game, Position goal) {
        return this.player.setup(game, goal);
    }

    @Override
    public boolean win(boolean playerWon) {
        return this.player.win(playerWon);
    }
}