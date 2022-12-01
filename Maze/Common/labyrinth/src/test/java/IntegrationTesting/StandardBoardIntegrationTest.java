//package IntegrationTests;
//
//import model.Utils;
//import model.board.IBoard;
//import protocol.serialization.JsonSerializer;
//import model.model.*;
//
//import java.io.IOException;
//import java.util.*;
//
//public class StandardBoardIntegrationTest {
//
//    public static void executeTest() {
//        try {
//            MazeJsonParser mazeParser = new MazeJsonParser(System.in);
//            JsonSerializer mazeSerializer = new JsonSerializer();
//
//            IBoard board = mazeParser.getBoardNoSpareTile();
//            mazeParser.readNext();
//            Position startingPosition = mazeParser.getCoordinate();
//
//            Set<Position> reachablePositions = board.getReachablePositions(startingPosition);
//            List<Position> reachablePositionsList = Utils.standardizedPositionList(reachablePositions);
//
//            String reachablePositionsJson = mazeSerializer.reachablePositionsToJson(reachablePositionsList);
//
//            System.out.println(reachablePositionsJson);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
