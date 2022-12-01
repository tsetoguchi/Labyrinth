package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;

import model.Position;
import model.state.GameResults;
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

    public String turnPlanToJson(Optional<Move> turnPlan) throws JsonProcessingException {
        if (turnPlan.isEmpty()) {
            return "PASS";
        }
        Move actions = turnPlan.get();

        Object[] resultArray = new Object[4];

        resultArray[0] = actions.getSlideIndex();
        resultArray[1] = actions.getSlideDirection();
        resultArray[2] = this.rotationsToDegrees(actions.getSpareTileRotations());
        resultArray[3] = actions.getMoveDestination();

        return this.mapper.writeValueAsString(resultArray);
    }


    public String namesToJson(List<String> names) throws IOException {
        String[] sortedNames = names.stream()
                .sorted()
                .toArray(String[]::new);
        return this.mapper.writeValueAsString(sortedNames);
    }

    public String observerGameToJson(ObserverGameProjection game) throws JsonProcessingException {
        return this.mapper.writeValueAsString(game);
    }


    /**
     * Convert rotations to degrees. Since JSON degrees are represented in counterclockwise turns, first convert
     * rotations from clockwise to counterclockwise.
     */
    private int rotationsToDegrees(int spareTileRotations) {
        int netClockwiseRotations = spareTileRotations % 4;
        int netCounterclockwiseRotations = (4 - netClockwiseRotations) % 4;
        return 90 * netCounterclockwiseRotations;
    }

    public JSONArray gameResultsToJSON(GameResults results) throws JsonProcessingException {
        JSONArray winnersJSON = new JSONArray(results.getWinners());
        JSONArray eliminatedJSON = new JSONArray(results.getEliminated());
        JSONArray result = new JSONArray();
        result.put(winnersJSON);
        result.put(eliminatedJSON);
        return result;
    }
}
