package remote.JSON;


import com.fasterxml.jackson.annotation.JsonProperty;
import game.model.Position;
import game.model.Tile;
import game.model.projections.ExperimentationBoardProjection;

public class JsonBoard {

    @JsonProperty("connectors")
    private final String[][] connectors;

    @JsonProperty("treasures")
    private final String[][][] treasures;

    public JsonBoard(@JsonProperty("connectors") String[][] connectors,
                     @JsonProperty("treasures") String[][][] treasures) {
        this.connectors = connectors;
        this.treasures = treasures;
    }

    public JsonBoard(ExperimentationBoardProjection board) {
        String[][] connectors = new String[board.getHeight()][board.getWidth()];
        String[][][] treasures = new String[board.getHeight()][board.getWidth()][2];
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTileAt(new Position(row, col));
                connectors[row][col] = tile.toSymbol();
                treasures[row][col] = tile.getTreasure().toStringArray();
            }
        }
        this.connectors = connectors;
        this.treasures = treasures;
    }

    public String[][] getConnectors() {
        return this.connectors;
    }

    public String[][][] getTreasures() {
        return this.treasures;
    }

}
