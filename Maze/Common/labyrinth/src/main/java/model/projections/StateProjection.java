package model.projections;

import model.board.ExperimentationBoard;
import model.board.IBoard;
import model.state.PlayerAvatar;
import model.state.IState;
import model.state.SlideAndInsertRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A read-only projection of the information available to a player about a state.
 */
public class StateProjection {
    private final ExperimentationBoard board;
    private final List<PlayerProjection> players;
    private final Optional<SlideAndInsertRecord> previousSlideAndInsert;

    public StateProjection(IState game, PlayerAvatar viewer, Optional<SlideAndInsertRecord> previousSlideAndInsert) {
        this.board = game.getBoard().getExperimentationBoard();
        List<PlayerProjection> playerViews = new ArrayList<>();
        for (PlayerAvatar player : game.getPlayerList()) {
            playerViews.add(new PlayerProjection(player));
        }
        this.players = playerViews;
        this.previousSlideAndInsert = previousSlideAndInsert;
    }

    public IBoard getBoard() {
        return this.board;
    }

    public List<PlayerProjection> getPlayers() {
        return this.players;
    }

    public PlayerProjection getSelf() {
        return this.players.get(0);
    }

    public Optional<SlideAndInsertRecord> getPreviousSlideAndInsert() {
        return this.previousSlideAndInsert;
    }
}
