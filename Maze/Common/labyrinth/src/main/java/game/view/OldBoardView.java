package game.view;

import game.model.Position;
import game.model.Tile;
import game.model.projections.PublicPlayerProjection;
import game.model.projections.ReadOnlyBoardProjection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The JFrame component which displays a board and the objects populating it. Does not include the spare tile.
 */
public class BoardView extends JPanel {
    private ReadOnlyBoardProjection board;
    private List<PublicPlayerProjection> players;
    private final TileView[][] tileGrid;
    private final int width;
    private final int height;

    private final int cellSideLen; // in pixels

    public BoardView(ReadOnlyBoardProjection board, List<PublicPlayerProjection> players, int cellSideLen) {
        this.board = board;
        this.width = board.getWidth();
        this.height = board.getHeight();
        this.setLayout(null);
        this.setBounds(this.getX(), this.getY(), this.width*cellSideLen, this.height*cellSideLen);
        this.setPreferredSize(new Dimension(this.width*cellSideLen, this.height*cellSideLen));
        this.players = players;
        this.cellSideLen = cellSideLen;

        this.tileGrid = new TileView[this.height][this.width];
        this.setBackground(Color.DARK_GRAY);
        //this.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void update(ReadOnlyBoardProjection board, List<PublicPlayerProjection> players) {
        this.board = board;
        this.players = players;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public void populateBoard() {
        for (int row = 0; row < this.height; row++) {
            this.createRowOfTiles(row);
        }
        this.addPlayersToTiles();
        this.addTilesToPanel();
    }

    /**
     *  Creates a row of TileViews in the tileGrid to be drawn later
     */
    private void createRowOfTiles(int currentRow) {
        for (int tileIndex = 0; tileIndex < this.width; tileIndex++) {
            TileView tileView = this.createTile(new Position(currentRow, tileIndex));
            this.tileGrid[currentRow][tileIndex] = tileView;
        }
    }

    private TileView createTile(Position position) {
        JPanel panel = new JPanel();
        panel.setSize(this.cellSideLen, this.cellSideLen);

        Tile tile = this.board.getTileAt(position);
        return new TileView(tile, this.cellSideLen);
    }

    /**
     * Adds the color of a player's avatar and a player's home for each player to their corresponding tile.
     */
    private void addPlayersToTiles() {
        for (PublicPlayerProjection player : this.players) {
            int avatarRow = player.getAvatarPosition().getRow();
            int avatarCol = player.getAvatarPosition().getColumn();
            int homeRow = player.getHomePosition().getRow();
            int homeCol = player.getHomePosition().getColumn();

            this.tileGrid[avatarRow][avatarCol].addAvatar(player.getColor());
            this.tileGrid[homeRow][homeCol].addHome(player.getColor());
        }
    }

    /**
     * Packs each tile into a panel with a border and adds it to this JPanel.
     */
    private void addTilesToPanel() {
        for (int row = 0; row < this.tileGrid.length; row++) {
            for (int col = 0; col < this.tileGrid[row].length; col++) {
                JPanel panel = new JPanel();
                panel.setBounds(row*this.cellSideLen, col*this.cellSideLen, this.cellSideLen, this.cellSideLen);
                panel.setSize(this.cellSideLen, this.cellSideLen);
                panel.add(this.tileGrid[row][col]);
                panel.setBorder(BorderFactory.createLineBorder(Color.black));
                this.add(panel);
            }
        }
    }
}


