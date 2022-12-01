package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Position;
import model.projections.StateProjection;
import model.state.GameResults;
import referee.ITurn;
import referee.Move;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Serialization methods for Labyrinth models.
 */
public class JsonSerializer {
    ObjectMapper mapper;

    public JsonSerializer() {
        this.mapper = new ObjectMapper();
    }

    public String reachablePositionsToJson(List<Position> reachablePositionsList) throws JsonProcessingException {
        return this.mapper.writeValueAsString(reachablePositionsList);
    }

    public static JSONArray moveToJson(Move move) {
        JSONArray resultArray = new JSONArray();
        resultArray.put(move.getSlideIndex());
        resultArray.put(move.getSlideDirection());
        resultArray.put(rotationsToDegrees(move.getSpareTileRotations()));
        resultArray.put(move.getMoveDestination());
        return resultArray;
    }


    public JSONObject stateProjectionToJson(StateProjection game) throws JsonProcessingException {
        JSONObject result = new JSONObject();

        return result;
    }

    public static int rotationsToDegrees(int spareTileRotations) {
        int netClockwiseRotations = spareTileRotations % 4;
        int netCounterclockwiseRotations = (4 - netClockwiseRotations) % 4;
        return 90 * netCounterclockwiseRotations;
    }

    public static JSONArray gameResultsToJSON(GameResults results) throws JsonProcessingException {
        JSONArray winnersJSON = new JSONArray(results.getWinners());
        JSONArray eliminatedJSON = new JSONArray(results.getEliminated());
        JSONArray result = new JSONArray();
        result.put(winnersJSON);
        result.put(eliminatedJSON);
        return result;
    }
}
