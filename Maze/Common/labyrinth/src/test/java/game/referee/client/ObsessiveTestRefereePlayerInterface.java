//package model.referee.client;
//
//import model.board.IBoard;
//import model.state.StateProjection;
//import player.Turn;
//
//import java.util.Optional;
//
///**
// * A test player client which always tries to take the same action.
// */
//public class ObsessiveTestRefereePlayerInterface extends TestIPlayer {
//    private final Turn plan;
//
//    public ObsessiveTestRefereePlayerInterface(Turn plan) {
//        this.plan = plan;
//    }
//
//    @Override
//    public Optional<Turn> takeTurn(StateProjection game) {
//        return Optional.of(this.plan);
//    }
//
//    @Override
//    public IBoard proposeBoard(int rows, int columns) {
//        return null;
//    }
//}
