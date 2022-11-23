package game.IntegrationTests;

import game.IntegrationTests.processing.IntegrationTestUtils;
import protocol.serialization.MazeJsonParser;
import protocol.serialization.MazeJsonSerializer;
import game.model.*;

import java.io.IOException;
import java.util.*;

public class StandardBoardIntegrationTest {

    public static void executeTest() {
        try {
            MazeJsonParser mazeParser = new MazeJsonParser(System.in);
            MazeJsonSerializer mazeSerializer = new MazeJsonSerializer();

            Board board = mazeParser.getBoardNoSpareTile();
            mazeParser.readNext();
            Position startingPosition = mazeParser.getCoordinate();

            Set<Position> reachablePositions = board.getReachablePositions(startingPosition);
            List<Position> reachablePositionsList = IntegrationTestUtils.standardizedPositionList(reachablePositions);

            String reachablePositionsJson = mazeSerializer.reachablePositionsToJson(reachablePositionsList);

            System.out.println(reachablePositionsJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
