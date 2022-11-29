package model.projections;

import model.board.IBoard;
import model.Position;
import model.board.Tile;

/**
 * A read-only projection of a board which can be used to view tiles.
 */

public class BoardProjection {
    IBoard board;

    public BoardProjection(IBoard board) {
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
