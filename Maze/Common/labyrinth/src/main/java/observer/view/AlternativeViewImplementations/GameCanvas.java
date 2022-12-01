//package observer.view.AlternativeViewImplementations;
//
//import static model.board.Direction.DOWN;
//import static model.board.Direction.LEFT;
//import static model.board.Direction.RIGHT;
//import static model.board.Direction.UP;
//
//import model.board.Gem;
//import model.board.IBoard;
//import model.Position;
//import model.board.Tile;
//import model.projections.PlayerAvatar;
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.GridBagLayout;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import javax.imageio.ImageIO;
//import javax.swing.JPanel;
//
//public class GameCanvas extends JPanel {
//
//  private List<ObserverGameProjection> states;
//  private int currentState;
//
//  private final Parameters parameters;
//
//
//  public GameCanvas(List<ObserverGameProjection> states, int cellSize) {
//    this.states = states;
//    this.currentState = 0;
//    this.parameters = new Parameters(cellSize);
//
//    IBoard board = this.getCurrentState().getBoard();
//    this.setBounds(this.getX(), this.getY(),
//        board.getWidth() * cellSize,
//        board.getHeight() * cellSize);
//    this.setLayout(new GridBagLayout());
//
//  }
//
//  private ObserverGameProjection getCurrentState() {
//    return this.states.get(this.currentState);
//  }
//
//  private List<PlayerAvatar> getPlayers() {
//    return this.getCurrentState().getPlayers();
//  }
//
//  @Override
//  public void paint(Graphics g) {
//    super.paint(g);
//
//    List<PlayerAvatar> players = this.getPlayers();
//
//    for (int row = 0; row < this.getCurrentState().getHeight(); row++) {
//      for (int col = 0; col < this.getCurrentState().getWidth(); col++) {
//        Position position = new Position(row, col);
//
//        List<PlayerAvatar> homes = players.stream()
//            .filter(p -> p.getHomePosition().equals(position)).collect(
//                Collectors.toList());
//
//        List<PlayerAvatar> avatarPositions = players.stream()
//            .filter(p -> p.getCurrentPosition().equals(position)).collect(
//                Collectors.toList());
//
//        Tile tile = this.getCurrentState().getBoard().getTileAt(position);
//        List<Color> playersTileIsHome = new ArrayList<>();
//        for (PlayerAvatar player : homes) {
//          playersTileIsHome.add(player.getColor());
//        }
//
//        this.drawTile((Graphics2D) g, this.parameters, tile, homes, playersTileIsHome);
//      }
//
//      // draw spare tile
//
//      this.drawTile((Graphics2D) g, this.parameters, this.getCurrentState().getBoard()
//          .getSpareTile(), List.of(), List.of());
//    }
//  }
//
//  private void drawTile(Graphics2D g, Parameters p, Tile tile,
//                        List<PlayerAvatar> homes, List<Color> homeColors) {
//    this.drawPathways(g, tile, p);
//    this.drawGems(g, tile, p);
//    this.drawAvatars(g, p);
//    this.drawHomes(g, homeColors, p);
//  }
//
//  private void drawPathways(Graphics g, Tile tile, Parameters p) {
//    int lineWidth = 10;
//    int offset = 13;
//    g.setColor(Color.GRAY);
//    if (tile.connects(UP)) {
//      g.fillRect(this.getX() + p.cellSize / 2 - lineWidth / 2, this.getY() - offset,
//          lineWidth, p.cellSize / 2 + lineWidth / 2);
//    }
//    if (tile.connects(DOWN)) {
//      g.fillRect(this.getX() + p.cellSize / 2 - lineWidth / 2,
//          this.getY() + p.cellSize / 2 - lineWidth / 2 - offset, lineWidth,
//          p.cellSize / 2);
//    }
//    if (tile.connects(LEFT)) {
//      g.fillRect(this.getX(), this.getY() + p.cellSize / 2 - lineWidth / 2 - offset,
//          p.cellSize / 2, lineWidth);
//    }
//    if (tile.connects(RIGHT)) {
//      g.fillRect(this.getX() + p.cellSize / 2,
//          this.getY() + p.cellSize / 2 - lineWidth / 2 - offset, p.cellSize / 2,
//          lineWidth);
//    }
//  }
//
//  private void drawGem(Graphics g, String gemName, int x, int y, Parameters p) throws IOException {
//    File gemFile = new File("../Maze/Common/labyrinth/src/main/resources/gems/" + gemName + ".png");
//    BufferedImage gemImg = ImageIO.read(gemFile);
//    g.drawImage(gemImg, x, y, p.imgSize, p.imgSize, null);
//  }
//
//  private void drawGems(Graphics g, Tile tile, Parameters p) {
//    List<Gem> gems = tile.getTreasure().getGems();
//    if (gems.size() != 2) {
//      throw new IllegalStateException(
//          "Treasure does not have exactly two gems when trying to display.");
//    }
//    try {
//      this.drawGem(g, gems.get(0).withDashes(), this.getX() + 10, this.getY() + 10, p);
//      this.drawGem(g, gems.get(1).withDashes(),
//          this.getX() + p.cellSize - 10 - p.imgSize,
//          this.getY() + p.cellSize - 10 - p.imgSize,
//          p);
//    } catch (IOException e) {
//      throw new IllegalStateException("Could not find an img file for gem.", e);
//    }
//  }
//
//  private void drawAvatars(Graphics g, Parameters p) {
//    int i = 0;
//    List<Color> avatars = new ArrayList<>();
//    for (PlayerAvatar player : this.getPlayers()) {
//      avatars.add(player.getColor());
//    }
//    for (Color avatarColor : avatars) {
//      g.setColor(avatarColor);
//      g.fillOval(this.getX() + p.cellSize - 30 - (i * 5), this.getY() + 10 - (i * 5), 20, 20);
//      i++;
//    }
//  }
//
//  private void drawHomes(Graphics g, List<Color> homes, Parameters p) {
//    int i = 0;
//    for (Color homeColor : homes) {
//      g.setColor(homeColor);
//      g.fillRect(this.getX() + 10 + (i * 5), this.getY() + p.cellSize - 40 - (i * 5), 20, 20);
//      i++;
//    }
//  }
//
//  private static class Parameters {
//
//    private final int cellSize;
//
//    private int imgSize;
//
//    private double width;
//
//    private double height;
//
//    private Parameters(int cellSize) {
//      this.cellSize = cellSize;
//      this.imgSize = cellSize / 5;
//    }
//  }
//
//}
