package game.model.projections;

import game.model.PlayerAvatar;
import game.model.PrivateState;
import game.model.SlideAndInsertRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A read-only projection of the information available to a player about a game.
 */
public class PlayerGameProjection {
    private final ExperimentationBoardProjection board;
    private final List<PublicPlayerProjection> players;
    private final SelfPlayerProjection self;
    private final Optional<SlideAndInsertRecord> previousSlideAndInsert;

    public PlayerGameProjection(PrivateState game, PlayerAvatar viewer, Optional<SlideAndInsertRecord> previousSlideAndInsert) {
        this.board = new ExperimentationBoardProjection(game.getBoard().getExperimentationBoard());
        List<PublicPlayerProjection> playerViews = new ArrayList<>();
        for (PlayerAvatar player : game.getPlayerList()) {
            playerViews.add(new PublicPlayerProjection(player));
        }
        this.players = playerViews;
        this.self = new SelfPlayerProjection(viewer);
        this.previousSlideAndInsert = previousSlideAndInsert;
    }

    public ExperimentationBoardProjection getBoard() {
        return this.board;
    }

    public List<PublicPlayerProjection> getPlayers() {
        return this.players;
    }

    public SelfPlayerProjection getSelf() {
        return this.self;
    }

    public Optional<SlideAndInsertRecord> getPreviousSlideAndInsert() {
        return this.previousSlideAndInsert;
    }
}
