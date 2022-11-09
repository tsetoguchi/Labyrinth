package game.it;

import game.model.GameStatus;
import game.model.Position;
import game.model.projections.PlayerGameProjection;
import player.Player;
import player.TurnPlan;
import referee.PlayerResult;
import referee.clients.PlayerClient;

import java.util.Optional;

public class IntegrationPlayerClient implements PlayerClient {
    Player player;
    PlayerResult result;

    public IntegrationPlayerClient(Player player) {
        this.player = player;
        this.result = null; // null for testing
    }

    @Override
    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {
        return player.createTurnPlan(game);
    }

    @Override
    public void returnHome(Position homeTile) {
        this.player.updateGoal(homeTile);
    }

    @Override
    public void informGameEnd(GameStatus status, PlayerResult result) {
        this.result = result;
    }

    @Override
    public String getPlayerName() {
        return player.getName();
    }

    @Override
    public boolean updateGoal(Position goal) {
        return this.player.updateGoal(goal);
    }
}
