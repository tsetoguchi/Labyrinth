package IntegrationTests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import json.JsonDeserializer;
import json.JsonSerializer;
import model.Position;
import model.Utils;
import model.board.Direction;
import model.board.ExperimentationBoard;
import model.state.GameResults;
import model.state.IState;
import player.IPlayer;
import referee.Move;
import referee.Referee;

public class XState {

  public static void main(String[] args) throws JSONException {

    JSONTokener jsonTokener = IntegrationUtils.getInput();


    JSONObject stateJSON = (JSONObject) jsonTokener.nextValue();
    IState game = JsonDeserializer.state(stateJSON);
    ExperimentationBoard eBoard = game.getBoard().getExperimentationBoard();
    Position current = game.getActivePlayer().getCurrentPosition();

    int index = (int) jsonTokener.nextValue();
    Direction direction = Direction.valueOf((String) jsonTokener.nextValue());
    int degree = (int) jsonTokener.nextValue();
    int rotations = JsonDeserializer.degreesToRotations(degree);

    Move move = new Move(direction, index, rotations, new Position(0,0));

    Set<Position> positions = eBoard.findReachableTilePositionsAfterSlideAndInsert(move, current);
    positions.add(current);

    List<Position> result = Utils.standardizedPositionList(positions);

    System.out.println(result);

  }
}
