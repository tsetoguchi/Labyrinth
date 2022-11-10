package game.view;

import game.model.Gem;
import game.model.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import static game.model.Direction.*;

/**
 * The JComponent which displays a single Tile.
 */
class OldTileView extends JPanel {

  private final Tile tile;
  private final int cellSideLen;
  private final int imgSideLen;
  private final Set<Color> avatars;
  private final Set<Color> homes;

  public OldTileView(Tile tile, int cellSideLen) {
    this.setLayout(null);
    this.setBounds(this.getX(), this.getY(), cellSideLen, cellSideLen);
    this.tile = tile;
    this.cellSideLen = cellSideLen;
    this.imgSideLen = cellSideLen / 5;
    this.avatars = new HashSet<>();
    this.homes = new HashSet<>();
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(this.cellSideLen, this.cellSideLen);
  }

  @Override
  public Dimension getMaximumSize() {
    return new Dimension(this.cellSideLen, this.cellSideLen);
  }

  @Override
  public Dimension getMinimumSize() {
    return new Dimension(this.cellSideLen, this.cellSideLen);
  }

  @Override
  public void paintComponent(Graphics g) {
    //Set background color?
    this.drawPathways(g);
    //this.drawGems(g);
    this.drawAvatars(g);
    this.drawHomes(g);
  }

  /**
   * Add an avatar that should be drawn on this TileView.
   */
  void addAvatar(Color c) {
    this.avatars.add(c);
  }

  /**
   * Add a home that should be drawn on this TileView.
   */
  void addHome(Color c) {
    this.homes.add(c);
  }

  private void drawPathways(Graphics g) {
    int lineWidth = 10;
    int offset = 13;
    g.setColor(Color.GRAY);
    if (this.tile.connects(UP)) {
      g.fillRect(this.getX() + this.cellSideLen / 2 - lineWidth / 2, this.getY() - offset,
          lineWidth, this.cellSideLen / 2 + lineWidth / 2);
    }
    if (this.tile.connects(DOWN)) {
      g.fillRect(this.getX() + this.cellSideLen / 2 - lineWidth / 2,
          this.getY() + this.cellSideLen / 2 - lineWidth / 2 - offset, lineWidth,
          this.cellSideLen / 2);
    }
    if (this.tile.connects(LEFT)) {
      g.fillRect(this.getX(), this.getY() + this.cellSideLen / 2 - lineWidth / 2 - offset,
          this.cellSideLen / 2, lineWidth);
    }
    if (this.tile.connects(RIGHT)) {
      g.fillRect(this.getX() + this.cellSideLen / 2,
          this.getY() + this.cellSideLen / 2 - lineWidth / 2 - offset, this.cellSideLen / 2,
          lineWidth);
    }
  }

  private void drawGems(Graphics g) {
    List<Gem> gems = this.tile.getTreasure().getGems();
    if (gems.size() != 2) {
      throw new IllegalStateException(
          "Treasure does not have exactly two gems when trying to display.");
    }
    try {
      this.drawGem(g, gems.get(0).withDashes(), this.getX() + 10, this.getY() + 10);
      this.drawGem(g, gems.get(1).withDashes(),
          this.getX() + this.cellSideLen - 10 - this.imgSideLen,
          this.getY() + this.cellSideLen - 10 - this.imgSideLen);
    } catch (IOException e) {
      throw new IllegalStateException("Could not find an img file for gem.", e);
    }
  }

  private void drawGem(Graphics g, String gemName, int x, int y) throws IOException {
    File gemFile = new File("../Maze/Common/labyrinth/src/main/resources/gems/" + gemName + ".png");
    BufferedImage gemImg = ImageIO.read(gemFile);
    g.drawImage(gemImg, x, y, this.imgSideLen, this.imgSideLen, null);
  }

  private void drawAvatars(Graphics g) {
    int i = 0;
    for (Color avatarColor : this.avatars) {
      g.setColor(avatarColor);
      g.fillOval(this.getX() + this.cellSideLen - 30 - (i * 5), this.getY() + 10 - (i * 5), 20, 20);
      i++;
    }
  }

  private void drawHomes(Graphics g) {
    int i = 0;
    for (Color homeColor : this.homes) {
      g.setColor(homeColor);
      g.fillRect(this.getX() + 10 + (i * 5), this.getY() + this.cellSideLen - 40 - (i * 5), 20, 20);
      i++;
    }
  }
}