package game.model.projections;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import game.model.Game;
import game.model.PlayerAvatar;
import game.model.PrivateGameState;
import game.model.SlideAndInsertRecord;
import protocol.serialization.model.ObserverGameProjectionSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A read-only projection of the information available to a spectator about a game.
 */
@JsonSerialize(using = ObserverGameProjectionSerializer.class)
public class ObserverGameProjection {
    private final ReadOnlyBoardProjection board;
    private final List<PublicPlayerProjection> players;
    private final Optional<SlideAndInsertRecord> previousSlide;
    private int activePlayer;

    public ObserverGameProjection(PrivateGameState game) {
        this.board = new ReadOnlyBoardProjection(game.getBoard().deepCopy());
        List<PublicPlayerProjection> playerViews = new ArrayList<>();
        for (PlayerAvatar player : game.getPlayerList()) {
            playerViews.add(new PublicPlayerProjection(player.deepCopy()));
        }
        this.players = playerViews;
        this.previousSlide = game.getPreviousSlideAndInsert();
        PlayerAvatar activeAvatar = game.getActivePlayer();
        this.activePlayer = -1;
        for (int playerInd = 0; playerInd < game.getPlayerList().size(); playerInd++) {
            if (game.getPlayerList().get(playerInd).equals(activeAvatar)) {
                this.activePlayer = playerInd;
            }
        }
        if (this.activePlayer == -1) {
            throw new IllegalStateException("getActivePlayer returned a PlayerAvatar which was not in playerList.");
        }
    }

    public ReadOnlyBoardProjection getBoard() {
        return this.board;
    }

    public List<PublicPlayerProjection> getPlayers() {
        return this.players;
    }

    public int getHeight() { return this.board.getHeight(); }

    public int getWidth() { return this.board.getWidth(); }

    public Optional<SlideAndInsertRecord> getPreviousSlide() {
        return this.previousSlide;
    }

    public int getActivePlayer() {
        return this.activePlayer;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(this.activePlayer);
        string.append(" ");
        for (PublicPlayerProjection player : this.getPlayers()) {
            string.append(player.getColor().toString());
        }
        return string.toString();
    }
}
