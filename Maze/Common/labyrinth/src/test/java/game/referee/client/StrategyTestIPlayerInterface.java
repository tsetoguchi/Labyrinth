//package game.referee.client;
//
//import game.model.IBoard;
//import game.model.Position;
//import game.model.projections.PlayerStateProjection;
//import game.model.projections.PublicPlayerAvatar;
//import player.IStrategy;
//import player.Turn;
//
//import java.util.Optional;
//
///**
// * A test player client which simulates a player which uses a given strategy.
// */
//public class StrategyTestIPlayerInterface extends TestIPlayerInterface {
//    private final IStrategy strategy;
//
//    public StrategyTestIPlayerInterface(IStrategy strategy) {
//        this.strategy = strategy;
//    }
//
//    @Override
//    public Optional<Turn> takeTurn(PlayerStateProjection game) {
//        return this.strategy.createTurnPlan(game.getBoard(), game.getSelf(),
//                game.getPreviousSlideAndInsert(), this.getGoalPosition(game));
//    }
//
//    @Override
//    public IBoard proposeBoard(int rows, int columns) {
//        return null;
//    }
//
//    private Position getGoalPosition(PlayerStateProjection game) {
//        PublicPlayerAvatar self = game.getSelf();
//        if (self.hasReachedGoal()) {
//            return self.getHomePosition();
//        }
//        else {
//            return self.getGoalPosition();
//        }
//    }
//}
