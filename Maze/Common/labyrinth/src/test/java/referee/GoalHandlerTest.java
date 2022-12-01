package java.referee;

import org.junit.Before;

import java.awt.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import model.board.Board;
import model.board.Direction;
import model.board.IBoard;
import model.board.Tile;
import model.state.IState;
import model.state.PlayerAvatar;
import model.state.State;
import player.IPlayer;
import player.Player;
import referee.IReferee;

public class GoalHandlerTest {

  IState state;
  IReferee ref;
  IPlayer player1;
  IPlayer player2;
  IBoard board;



  @Before
  public void setup() {
    Tile[][] tiles = new Tile[7][7];

    Set<Direction> cross = new HashSet<>();
    cross.add(Direction.UP);
    cross.add(Direction.DOWN);
    cross.add(Direction.LEFT);
    cross.add(Direction.RIGHT);

    Set<Direction> line = new HashSet<>();
    cross.add(Direction.LEFT);
    cross.add(Direction.RIGHT);


    for(int i=0;i<7;i++){
      for(int j=0;j<7;j++){
        if(i%2 == 1){
          tiles[i][j] = new Tile(cross, null);
        } else {
          tiles[i][j] = new Tile(line, null);
        }
      }
    }


    /*

    BOARD LOOKS LIKE:

    - - - - - - -
    + + + + + + +
    - - - - - - -
    + + + + + + +
    - - - - - - -
    + + + + + + +
    - - - - - - -

     */

    Tile spare = new Tile(line, null);
    this.board = new Board(7, 7, tiles, spare);


    PlayerAvatar p1 = new PlayerAvatar(Color.RED, new Position());

    this.state = new State(this.mockBoard, this.initialSpare);

    this.AIPlayer1 = new AIPlayer(new RiemannStrategy(), "ryan");
    Coordinate home1 = new Coordinate(1, 3);
    Coordinate goal1 = new Coordinate(3, 3);
    this.AIPlayer1.setup(Optional.empty(), goal1);
    this.AIPlayer2 = new AIPlayer(new RiemannStrategy(), "greg");
    Coordinate home2 = new Coordinate(1, 3);
    Coordinate goal2 = new Coordinate(5, 1);
    Coordinate current2 = new Coordinate(0, 6);
    this.AIPlayer2.setup(Optional.empty(), goal2);

    this.mockState.addPlayer(AIPlayer1, goal1, home1);
    this.mockState.addPlayer(AIPlayer2, goal2, home2, current2);
    this.mockReferee = new Referee();
    this.mockReferee.setupGame(this.mockState);
  }

}
