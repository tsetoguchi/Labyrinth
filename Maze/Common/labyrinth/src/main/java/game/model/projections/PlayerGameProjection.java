package game.model.projections;

import game.model.IBoard;
import game.model.PlayerAvatar;
import game.model.IState;
import game.model.SlideAndInsertRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A read-only projection of the information available to a player about a game.
 */
public class PlayerGameProjection {
    private final IBoard board;
    private final List<PublicPlayerProjection> players;
    private final PublicPlayerAvatar self;
    private final Optional<SlideAndInsertRecord> previousSlideAndInsert;

    public PlayerGameProjection(IState game, PlayerAvatar viewer, Optional<SlideAndInsertRecord> previousSlideAndInsert) {
        this.board = game.getBoard().deepCopy();
        List<PublicPlayerProjection> playerViews = new ArrayList<>();
        for (PlayerAvatar player : game.getPlayerList()) {
            playerViews.add(new PublicPlayerProjection(player));
        }
        this.players = playerViews;
        this.self = new PublicPlayerAvatar(viewer);
        this.previousSlideAndInsert = previousSlideAndInsert;
    }

    public IBoard getBoard() {
        return this.board;
    }

    public List<PublicPlayerProjection> getPlayers() {
        return this.players;
    }

    public PublicPlayerAvatar getSelf() {
        return this.self;
    }

    public Optional<SlideAndInsertRecord> getPreviousSlideAndInsert() {
        return this.previousSlideAndInsert;
    }
}
