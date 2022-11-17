package protocol.serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.it.BadFM;
import game.it.BadTestPlayer;
import game.it.TestPlayer;
import game.it.processing.IntegrationTestUtils;
import game.model.*;
import player.EuclideanStrategy;
import player.Player;
import player.RiemannStrategy;
import player.IStrategy;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Deserialization methods for Labyrinth models. Created with a parser and each public method reads
 * a specific kind of data from the head of the parser, moving the head as it reads.
 */
public class MazeJsonParser {

  private final int boardWidth;
  private final int boardHeight;
  private final JsonParser parser;
  private final ObjectMapper mapper;

  private static final Map<String, Color> colors;

  static {
    colors = new HashMap<>();
    colors.put("purple", new Color(218, 112, 214));
    colors.put("orange", Color.ORANGE);
    colors.put("pink", Color.PINK);
    colors.put("red", Color.RED);
    colors.put("blue", Color.BLUE);
    colors.put("green", Color.GREEN);
    colors.put("yellow", Color.YELLOW);
    colors.put("white", Color.WHITE);
    colors.put("black", Color.BLACK);
  }

  /**
   * Creates a new parser from the given input for a board of the specified dimensions.
   */
  public MazeJsonParser(InputStream in, int boardHeight, int boardWidth) {
    this.boardHeight = boardHeight;
    this.boardWidth = boardWidth;
    JsonFactory jsonFactory = new JsonFactory();
    this.mapper = new ObjectMapper();
    try {
      this.parser = jsonFactory.createParser(in);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public MazeJsonParser(InputStream in) {

  }

  /**
   * Move the parser to the next object to read.
   */
  public void readNext() throws IOException {
    this.parser.nextToken();
  }

  /**
   * Read a game state from the current parser. Json players do not include goal tiles, so they are
   * arbitrarily assigned.
   */
  public PrivateGameState getGame() throws IOException {
    return this.getGameFromState(false);
  }

  /**
   * Read a game state (including goal tiles) from the current parser.
   *
   * @return
   * @throws IOException
   */
  public PrivateGameState getGameWithGoals() throws IOException {
    return this.getGameFromState(true);
  }

  /**
   * Read a Position from the current parser.
   */
  public Position getCoordinate() throws IOException {
    return this.mapper.readValue(this.parser, Position.class);
  }

  /**
   * Read an int from the current parser.
   */
  public int getInt() throws IOException {
    return this.parser.getValueAsInt();
  }

  /**
   * Read a Direction from the current parser.
   */
  public Direction getDirection() throws IOException {
    return Direction.valueOf(this.parser.getText());
  }

  /**
   * Read a rotation count from the current parser. Assumes JSON uses counterclockwise rotations, so
   * converts to clockwise for use in Labyrinth methods.
   */
  public int getRotations() throws IOException {
    int degrees = this.parser.getValueAsInt();
    if (degrees % 90 != 0) {
      throw new MazeJsonProcessingException(
          "Parsed a degree rotation that was not a multiple of 90.");
    }
    int counterClockwiseRotations = degrees / 90;
    int clockwiseRotations = 4 - counterClockwiseRotations;
    return clockwiseRotations;
  }

  /**
   * Read a Strategy from the current parser.
   */
  public IStrategy getStrategy() throws IOException {
    switch (this.parser.getValueAsString()) {
      case "Euclid":
        return new EuclideanStrategy();
      case "Riemann":
        return new RiemannStrategy();
      default:
        throw new MazeJsonProcessingException("Strategy was not a valid Strategy Designation.");
    }
  }

  /**
   * Read a Board from the current parser, assigning an arbitrary spare tile.
   */
  public Board getBoardNoSpareTile() throws IOException {
    Tile[][] tileGrid = this.getTileGrid();
    return new StandardBoard(tileGrid, IntegrationTestUtils.createTileFromShape("├"));
  }

  /**
   * Read a matrix of Tiles, including pathways and gems, from the current parser.
   */
  public Tile[][] getTileGrid() throws IOException {
    List<List<String>> rawTileGrid = new ArrayList<>();
    List<List<List<String>>> rawTreasures = new ArrayList<>();

    while (this.parser.nextToken() != JsonToken.END_OBJECT) {
      if (this.parser.getCurrentToken().equals(JsonToken.FIELD_NAME)) {
        String field = this.parser.getCurrentName();

        if (field.equals("connectors")) {
          this.readNext();
          rawTileGrid = this.getRawTileGrid();
        }
        if (field.equals("treasures")) {
          this.readNext();
          rawTreasures = this.getRawTreasuresGrid();
        }
      }
    }

    return this.getBoardGrid(rawTileGrid, rawTreasures);
  }

  /**
   * Read a list of players from the current parser.
   */
  public List<Player> getPlayers() throws IOException {
    List<Player> players = new ArrayList<>();
    while (this.parser.nextToken() != JsonToken.END_ARRAY) {
      // read PS
      while (this.parser.nextToken() != JsonToken.END_ARRAY) {
        String name = this.parser.getText();
        this.readNext();
        IStrategy strategy = this.getStrategy();
        Player player = new TestPlayer(name, strategy);
        players.add(player);
      }
    }
    return players;
  }

  public List<Player> getPSAndBadPS() throws IOException {
    List<Player> players = new ArrayList<>();
    while (this.parser.nextToken() != JsonToken.END_ARRAY) {
      // read PS
      while (this.parser.nextToken() != JsonToken.END_ARRAY) {
        String name = this.parser.getText();
        //System.out.println(name);
        this.readNext();
        IStrategy strategy = this.getStrategy();

        Player player = null;
        if (this.parser.nextToken() == JsonToken.END_ARRAY)  {
          player = new TestPlayer(name, strategy);
          players.add(player);
          break;
        }
        else {
          BadFM badFM = this.getBadFM();
          player = new BadTestPlayer(name, strategy, badFM);
        }

        players.add(player);
      }
    }
    return players;
  }

  private BadFM getBadFM() throws IOException {
    switch (this.parser.getValueAsString()) {
      case "setUp":
        return BadFM.SETUP;
      case "takeTurn":
        return BadFM.TAKETURN;
      case "win":
        return BadFM.WIN;
      default:
        throw new MazeJsonProcessingException("Strategy was not a valid Strategy Designation.");
    }
  }

  /**
   * Read a Game from the current parser. Players will be parsed according to whether or not they
   * include goal tiles.
   */
  private PrivateGameState getGameFromState(boolean playersIncludeGoals) throws IOException {
    // we use null here to hold the variables right before assigning them, there will never persist a null value
    // as we check if any remain null afterwards
    Tile[][] tileGrid = null;
    Tile spareTile = null;
    List<PlayerAvatar> playerList = null;
    Optional<SlideAndInsertRecord> previousSlideAndInsert = null;

    while (this.parser.nextToken() != JsonToken.END_OBJECT) {
      String field = this.parser.getCurrentName();
      switch (field) {
        case "board":
          this.readNext();
          tileGrid = this.getTileGrid();
          break;
        case "spare":
          this.readNext();
          spareTile = this.getTile();
          //this.readNext(); // read past the nested END_OBJECT
          break;
        case "plmt":
          this.readNext();
          playerList = this.getPlayerList(playersIncludeGoals);
          break;
        case "last":
          this.readNext();
          previousSlideAndInsert = this.getSlideAndInsert();
          break;
        default:
          throw new MazeJsonProcessingException("Found invalid field" + field + " in State.");
      }
    }
    if (tileGrid == null || spareTile == null || playerList == null
        || previousSlideAndInsert == null) {
      throw new MazeJsonProcessingException("Did not find all four expected values for State.");
    }
    StandardBoard board = new StandardBoard(tileGrid, spareTile);
    if (!playersIncludeGoals) {
      // assign arbitrary goals
      List<PlayerAvatar> finalPlayerList = playerList.stream().map(
              (PlayerAvatar player) -> this.createPlayerWithUpdatedGoal(player, new Position(1, 1)))
          .collect(Collectors.toList());
      return new Game(board, finalPlayerList, 0, previousSlideAndInsert);
    }
    return new Game(board, playerList, 0, previousSlideAndInsert);
  }

  /**
   * Read the shape symbols of Tiles into a matrix of Strings.
   */
  private List<List<String>> getRawTileGrid() throws IOException {
    List<List<String>> board = new ArrayList<>();
    while (this.parser.nextToken() != JsonToken.END_ARRAY) {
      List<String> row = new ArrayList<>();
      while (this.parser.nextToken() != JsonToken.END_ARRAY) {
        row.add(this.parser.getText());
      }
      board.add(row);
    }
    return board;
  }

  /**
   * Read treasures into a matrix of Strings[]s, which are the gems in a treasure.
   */
  private List<List<List<String>>> getRawTreasuresGrid() throws IOException {
    List<List<List<String>>> treasures = new ArrayList<>();
    while (this.parser.nextToken() != JsonToken.END_ARRAY) {
      List<List<String>> row = new ArrayList<>();
      while (this.parser.nextToken() != JsonToken.END_ARRAY) {
        List<String> gems = new ArrayList<>();
        while (this.parser.nextToken() != JsonToken.END_ARRAY) {
          gems.add(this.parser.getText());
        }
        row.add(gems);
      }
      treasures.add(row);
    }
    return treasures;
  }

  /**
   * Construct the Tile grid for a board from the raw board and treasure matrices.
   */
  private Tile[][] getBoardGrid(List<List<String>> rawBoard, List<List<List<String>>> rawTreasures)
      throws JsonProcessingException {
    Tile[][] grid = new Tile[this.boardHeight][this.boardWidth];
    for (int row = 0; row < this.boardHeight; row++) {
      for (int col = 0; col < this.boardWidth; col++) {
        String shape = rawBoard.get(row).get(col);
        Tile tile = IntegrationTestUtils.createTileFromShape(shape,
            this.createTreasureFromRaw(rawTreasures.get(row).get(col)));
        grid[row][col] = tile;
      }
    }
    return grid;
  }

  /**
   * Updates the goal position of a player. Used when JSON data which constructs the player does not
   * include the goal, but the goal is read later on.
   */
  public PlayerAvatar createPlayerWithUpdatedGoal(PlayerAvatar initialPlayer, Position newGoal) {
    PlayerAvatar newPlayer = new PlayerAvatar(
        initialPlayer.getColor(),
        newGoal,
        initialPlayer.getHomePosition()
    );
    newPlayer.setCurrentPosition(initialPlayer.getCurrentPosition());
    return newPlayer;
  }

  /**
   * Parse a Treasure from raw data.
   */
  private Treasure createTreasureFromRaw(List<String> rawTreasure) throws JsonProcessingException {
    if (rawTreasure.size() != 2) {
      throw new MazeJsonProcessingException("Treasure did not have exactly 2 gems.");
    }
    return IntegrationTestUtils.createTreasure(this.stringToGem(rawTreasure.get(0)),
        this.stringToGem(rawTreasure.get(1)));
  }

  /**
   * Read a Tile from the current parser.
   **/
  private Tile getTile() throws IOException {
    Gem gem1 = null;
    Gem gem2 = null;
    Set<Direction> directions = null;
    while (this.parser.nextToken() != JsonToken.END_OBJECT) {
      String field = this.parser.getCurrentName();

      if (field.equals("tilekey")) {
        this.readNext();
        directions = IntegrationTestUtils.getDirectionsForSymbol(this.parser.getText());
      }
      if (field.equals("1-image")) {
        this.readNext();
        gem1 = this.stringToGem(this.parser.getText());
      }
      if (field.equals("2-image")) {
        this.readNext();
        gem2 = this.stringToGem(this.parser.getText());
      }
    }
    if (directions == null || gem1 == null || gem2 == null) {
      throw new IllegalArgumentException("Did not find required field while reading Tile");
    }
    return new Tile(directions, new Treasure(List.of(gem1, gem2)));
  }

  /**
   * Read a list of players from the current parser. If includeGoals is false, assign an arbitrary
   * goal.
   */
  private List<PlayerAvatar> getPlayerList(boolean includeGoals) throws IOException {
    List<PlayerAvatar> players = new ArrayList<>();

    while (this.parser.nextToken() != JsonToken.END_ARRAY) {
      players.add(this.getPlayer(includeGoals));
    }

    return players;
  }

  /**
   * Read a Player from the current parser. If includeGoals is false, assign an arbitrary goal.
   */
  private PlayerAvatar getPlayer(boolean includeGoals) throws IOException {
    Position avatar = null;
    Position home = null;
    Color color = null;
    Position goal = null;
    while (this.parser.nextToken() != JsonToken.END_OBJECT) {
      String field = this.parser.getCurrentName();

      if (field.equals("current")) {
        this.readNext();
        avatar = this.getCoordinate();
      }
      if (field.equals("home")) {
        this.readNext();
        home = this.getCoordinate();
      }
      if (field.equals("color")) {
        this.readNext();
        String value = this.parser.getText();
        if (value.matches("^[A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d]$")) {
          color = Color.decode("#" + value);
        } else {
          if (!colors.containsKey(value)) {
            throw new MazeJsonProcessingException("Illegal color");
          }
          color = colors.get(value);
        }
      }
      if (includeGoals && field.equals("goto")) {
        this.readNext();
        goal = this.getCoordinate();
      }
    }
    if (color == null || home == null || avatar == null) {
      throw new MazeJsonProcessingException("Null or missing field when parsing player.");
    }
    if (goal == null) {
      goal = new Position(1, 1);
    }
    PlayerAvatar player = new PlayerAvatar(color, goal, home);
    player.setCurrentPosition(avatar);
    return player;
  }

  /**
   * Read a SlideAndInsert record from the current parser. Since it is reading a record used only
   * for checking reversal, JSON does not include rotations, so they are assigned to 0.
   */
  private Optional<SlideAndInsertRecord> getSlideAndInsert() throws IOException {
    if (this.parser.getCurrentToken().equals(JsonToken.VALUE_NULL)) {
      return Optional.empty();
    }
    Direction direction;
    int index;
    int rotations = 0;
    this.readNext();
    index = this.parser.getIntValue();
    this.readNext();
    direction = this.getDirection();
    if (this.parser.nextToken() != JsonToken.END_ARRAY) {
      throw new MazeJsonProcessingException("Action array had more than two elements");
    }
    return Optional.of(new SlideAndInsertRecord(direction, index, rotations));
  }

  /**
   * Format a raw JSON gem String to match the name of its corresponding Gem.
   */
  private Gem stringToGem(String gemRaw) {
    String formattedGem = gemRaw.replaceAll("-", "_");
    return Gem.valueOf(formattedGem);
  }

  /**
   * An exception to be thrown when there is a problem parsing JSON into Labyrinth data.
   */
  public static class MazeJsonProcessingException extends JsonProcessingException {

    public MazeJsonProcessingException(String msg) {
      super(msg);
    }
  }
}
