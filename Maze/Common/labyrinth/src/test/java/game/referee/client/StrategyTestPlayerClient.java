package game.referee.client;

import game.model.Position;
import game.model.projections.PlayerGameProjection;
import game.model.projections.SelfPlayerProjection;
import player.Strategy;
import player.TurnPlan;

import java.util.Optional;

/**
 * A test player client which simulates a player which uses a given strategy.
 */
public class StrategyTestPlayerClient extends TestPlayerClient{
    private final Strategy strategy;

    public StrategyTestPlayerClient(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {
        return this.strategy.createTurnPlan(game.getBoard(), game.getSelf(),
                game.getPreviousSlideAndInsert(), this.getGoalPosition(game));
    }

    private Position getGoalPosition(PlayerGameProjection game) {
        SelfPlayerProjection self = game.getSelf();
        if (self.hasReachedGoal()) {
            return self.getHomePosition();
        }
        else {
            return self.getGoalPosition();
        }
    }
}
