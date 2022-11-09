package protocol.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.PlayerAvatar;
import game.model.Position;
import game.model.projections.ObserverGameProjection;
import player.TurnPlan;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serialization methods for Labyrinth models.
 */
public class MazeJsonSerializer {
    ObjectMapper mapper;

    public MazeJsonSerializer() {
        this.mapper = new ObjectMapper();
    }

    public String reachablePositionsToJson(List<Position> reachablePositionsList) throws JsonProcessingException {
        return this.mapper.writeValueAsString(reachablePositionsList);
    }

    public String turnPlanToJson(Optional<TurnPlan> turnPlan) throws JsonProcessingException {
        if (turnPlan.isEmpty()) {
            return "PASS";
        }
        TurnPlan actions = turnPlan.get();

        Object[] resultArray = new Object[4];

        resultArray[0] = actions.getSlideIndex();
        resultArray[1] = actions.getSlideDirection();
        resultArray[2] = this.rotationsToDegrees(actions.getSpareTileRotations());
        resultArray[3] = actions.getMoveDestination();

        return this.mapper.writeValueAsString(resultArray);
    }

    public String winnersToJson(List<String> winners) throws IOException {
        String[] sortedWinners = winners.stream()
                .sorted()
                .toArray(String[]::new);
        return this.mapper.writeValueAsString(sortedWinners);
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
}
