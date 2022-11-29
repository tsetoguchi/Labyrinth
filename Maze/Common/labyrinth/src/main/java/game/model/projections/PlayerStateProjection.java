package game.model.projections;

import game.model.IBoard;
import game.model.PlayerAvatar;
import game.model.IState;
import game.model.SlideAndInsertRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A read-only projection of the information available to a player about a state.
 */
public class PlayerStateProjection {
    private final IBoard board;
    private final List<PublicPlayerProjection> players;
    private final Optional<SlideAndInsertRecord> previousSlideAndInsert;

    public PlayerStateProjection(IState game, PlayerAvatar viewer, Optional<SlideAndInsertRecord> previousSlideAndInsert) {
        this.board = game.getBoard().deepCopy();
        List<PublicPlayerProjection> playerViews = new ArrayList<>();
        for (PlayerAvatar player : game.getPlayerList()) {
            playerViews.add(new PublicPlayerProjection(player));
        }
        this.players = playerViews;
        this.previousSlideAndInsert = previousSlideAndInsert;
    }

    public IBoard getBoard() {
        return this.board;
    }

    public List<PublicPlayerProjection> getPlayers() {
        return this.players;
    }

    public PublicPlayerProjection getSelf() {
        return this.players.get(0);
    }

    public Optional<SlideAndInsertRecord> getPreviousSlideAndInsert() {
        return this.previousSlideAndInsert;
    }
}
