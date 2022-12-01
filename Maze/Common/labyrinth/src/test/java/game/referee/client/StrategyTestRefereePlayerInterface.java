//package model.referee.client;
//
//import model.board.IBoard;
//import model.projections.StateProjection;
//import player.IStrategy;
//import player.Turn;
//
//import java.util.Optional;
//
///**
// * A test player client which simulates a player which uses a given strategy.
// */
//public class StrategyTestRefereePlayerInterface extends TestIPlayer {
//    private final IStrategy strategy;
//
//    public StrategyTestRefereePlayerInterface(IStrategy strategy) {
//        this.strategy = strategy;
//    }
//
//    @Override
//    public Optional<Turn> takeTurn(StateProjection game) {
//        return this.strategy.createTurnPlan(, game.getBoard(),
//            this.getGoalPosition(game));
//    }
//
//    @Override
//    public IBoard proposeBoard(int rows, int columns) {
//        return null;
//    }
//
////    private Position getGoalPosition(PlayerStateProjection game) {
////        PublicPlayerProjection self = game.getSelf();
////        if (self.hasReachedGoal()) {
////            return self.getHomePosition();
////        }
////        else {
////            return self.getGoalPosition();
////        }
////    }
//}
