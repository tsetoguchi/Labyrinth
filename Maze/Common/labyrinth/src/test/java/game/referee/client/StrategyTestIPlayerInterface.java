//package game.referee.client;
//
//import game.board.IBoard;
//import game.Position;
//import game.projections.PlayerStateProjection;
//import game.projections.PublicPlayerAvatar;
//import player.IStrategy;
//import referee.Move;
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
//    public Optional<Move> takeTurn(PlayerStateProjection game) {
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
