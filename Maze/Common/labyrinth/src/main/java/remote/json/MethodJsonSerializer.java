package remote.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.Board;
import game.model.Gem;
import game.model.Position;
import game.model.Tile;
import game.model.projections.ExperimentationBoardProjection;
import game.model.projections.PlayerGameProjection;

import java.util.List;

public class MethodJsonSerializer {

    ObjectMapper mapper;

    public MethodJsonSerializer() {
        this.mapper = new ObjectMapper();
    }

    public String generateWinJson(boolean win) {
        Boolean[] argument = new Boolean[] {
                win
        };

        Object[] output = new Object[]{
                "win",
                argument
        };

        try {
            return mapper.writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateTakeTurnJson(PlayerGameProjection game) {

        ExperimentationBoardProjection board = game.getBoard();
        String[][] connectors = new String[board.getHeight()][board.getWidth()];
        String[][][] treasures = new String[board.getHeight()][board.getWidth()][2];
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTileAt(new Position(row, col));
                connectors[row][col] = tile.toSymbol();
                treasures[row][col] = tile.getTreasure().toStringArray();
            }
        }
        JsonBoard jsonBoard = new JsonBoard(connectors, treasures);
        Tile spare = game.getBoard().getSpareTile();
        JsonTile jsonTile = new JsonTile(spare.toSymbol(),
                spare.getTreasure().getGems().get(0).withDashes(), spare.getTreasure().getGems().get(1).withDashes());


        Object[] output = new Object[]{
                "take-turn",
                argument
        };
    }
}
