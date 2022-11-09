package game.model.projections;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import game.model.Board;
import game.model.Position;
import game.model.Tile;
import protocol.serialization.model.ReadOnlyBoardProjectionSerializer;

/**
 * A read-only projection of a board which can be used to view tiles.
 */
@JsonSerialize(using = ReadOnlyBoardProjectionSerializer.class)
public class ReadOnlyBoardProjection {
    Board board;

    public ReadOnlyBoardProjection(Board board) {
        this.board = board;
    }

    public int getHeight() {
        return this.board.getHeight();
    }

    public int getWidth() {
        return this.board.getWidth();
    }

    public Tile getSpareTile() {
        return this.board.getSpareTile();
    }

    public Tile getTileAt(Position position) {
        return this.board.getTileAt(position);
    }
}
