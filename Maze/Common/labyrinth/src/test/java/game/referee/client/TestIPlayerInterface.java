//package game.referee.client;
//
//import game.state.GameStatus;
//import game.Position;
//import game.projections.PlayerStateProjection;
//import referee.PlayerResult;
//
//
//import java.util.Optional;
//import player.IPlayer;
//
///**
// * A mock player client for testing which keeps track of the messages it receives for comparison.
// */
//public abstract class TestIPlayerInterface implements IPlayer {
//    public boolean informedOfGoalReached;
//    public GameStatus finalGameResult;
//    public PlayerResult finalPlayerResult;
//
//    public TestIPlayerInterface() {
//        this.informedOfGoalReached = false;
//        this.finalGameResult = null; // these are null for testing purposes
//        this.finalPlayerResult = null;
//    }
//
//    /** Record that this client was told to go home. **/
//    public void returnHome(Position homeTile) {
//        this.informedOfGoalReached = true; // mark that this player was told to go home
//    }
//
//    /** Record results for comparison. **/
//    public void informGameEnd(GameStatus status, PlayerResult result) {
//        this.finalGameResult = status;
//        this.finalPlayerResult = result;
//    }
//
//    @Override
//    public boolean win(boolean playerWon) {
//        return true;
//    }
//
//    @Override
//    public boolean setup(Optional<PlayerStateProjection> game, Position goal) {
//        return true;
//    }
//
//    @Override
//    public String getPlayerName() {
//        return "Testy Joe";
//    }
//
//    @Override
//    public boolean updateGoal(Position goal) {
//        return true;
//    }
//}
