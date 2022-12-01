package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
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

    public static JSONArray move(Move move) {
        JSONArray resultArray = new JSONArray();
        resultArray.put(move.getSlideIndex());
        resultArray.put(move.getSlideDirection());
        resultArray.put(rotationsToDegrees(move.getSpareTileRotations()));
        resultArray.put(move.getMoveDestination());
        return resultArray;
    }


    public static JSONObject stateProjection(StateProjection game) throws JSONException{
        JSONObject result = new JSONObject();

        return result;
    }

    public static int rotationsToDegrees(int spareTileRotations) {
        int netClockwiseRotations = spareTileRotations % 4;
        int netCounterclockwiseRotations = (4 - netClockwiseRotations) % 4;
        return 90 * netCounterclockwiseRotations;
    }

    public static JSONArray gameResults(GameResults results) {
        JSONArray winnersJSON = new JSONArray(results.getWinners());
        JSONArray eliminatedJSON = new JSONArray(results.getEliminated());
        JSONArray result = new JSONArray();
        result.put(winnersJSON);
        result.put(eliminatedJSON);
        return result;
    }

    public static JSONObject position(Position goal) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("row#", goal.getRow());
        result.put("column#", goal.getColumn());
        return result;
    }




    public static JSONArray takeTurn(StateProjection game) throws JSONException {
        JSONArray result = new JSONArray();
        result.put("take-turn");
        JSONArray args = new JSONArray();
        args.put(JsonSerializer.stateProjection(game));
        result.put(args);
        return result;
    }

    public static JSONArray win(boolean won) {
        JSONArray result = new JSONArray();
        result.put("win");
        JSONArray args = new JSONArray();
        args.put(won);
        result.put(args);
        return result;
    }

    public static JSONArray setup(Optional<StateProjection> game, Position goal) throws JSONException{
        JSONArray result = new JSONArray();
        result.put("setup");
        JSONArray args = new JSONArray();


        if(game.isPresent()){
            args.put(JsonSerializer.stateProjection(game.get()));
        } else{
            args.put(false);
        }

        args.put(JsonSerializer.position(goal));

        result.put(args);
        return result;
    }

}
