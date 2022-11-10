package game.view;

import game.model.projections.ObserverGameProjection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Optional;

/**
 * Renders the board and the interactive panels for the observer, and detects mouse input.
 */
public class ObserverView extends JFrame {

  private ObserverGameProjection currentState;

  private BoardView boardFrame;
  private JPanel spareTilePanel;

  private JButton nextButton;
  private JButton saveButton;

  private static final int CELL_SIDE_LEN = 80;

  public ObserverView(ObserverGameProjection currentState) {

//    getContentPane(), BoxLayout.Y_AXIS)
    this.setLayout(new GridBagLayout());
    this.currentState = currentState;
//        this.setPreferredSize(new Dimension(1000, 800));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.boardFrame = this.currentBoardView();
    this.addSpareTile(currentSpareTileView());
    this.add(this.spareTilePanel);


    this.nextButton = new JButton("Next");
    this.nextButton.setActionCommand("Next");
//        this.nextButton.setBounds(30, 600, 100, 40);
    this.add(this.nextButton);

    this.saveButton = new JButton("Save");
    this.saveButton.setActionCommand("Save");
//        this.saveButton.setBounds(160, 600, 100, 40);
    this.add(this.saveButton);

    this.drawCurrentState();
  }

  public void updateView(ObserverGameProjection newState) {
    System.out.println("Updating");

    this.getContentPane().remove(this.boardFrame);
    this.getContentPane().remove(this.spareTilePanel);

    System.out.println("Current State: " + this.currentState.toString());
    this.currentState = newState;
    System.out.println("New State: " + this.currentState.toString());

    this.drawCurrentState();
  }

  public Optional<String> selectFile() {
    JFrame saveFrame = new JFrame();

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Specify a file to save the state in");

    int userSelection = fileChooser.showSaveDialog(saveFrame);
    File saveFile;

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      saveFile = fileChooser.getSelectedFile();
      return Optional.of(saveFile.getAbsolutePath());
    }
    return Optional.empty();
  }

  public void enableNextButton(boolean enabled) {
    this.nextButton.setEnabled(enabled);
  }

  private void drawNewState() {
    this.boardFrame = this.currentBoardView();
    this.boardFrame.populateBoard();
    this.repaint();
  }

  private void drawCurrentState() {
    this.boardFrame = this.currentBoardView();

    this.boardFrame.populateBoard();

    this.add(this.boardFrame);
    this.addSpareTile(currentSpareTileView());

    this.pack();
    this.revalidate();
    this.repaint();
//    this.boardFrame.repaint();
//    this.spareTile.repaint();
//    this.repaint();
    this.setVisible(true);
    System.out.println("");
  }

  private TileView currentSpareTileView() {
    return new TileView(this.currentState.getBoard().getSpareTile(), CELL_SIDE_LEN);
  }

  private BoardView currentBoardView() {
    return new BoardView(this.currentState.getBoard(), this.currentState.getPlayers(),
        CELL_SIDE_LEN);
  }

  private void addSpareTile(TileView tile) {
    JPanel panel = new JPanel();
    panel.setBounds(this.getBounds().height + 300, 700, CELL_SIDE_LEN, CELL_SIDE_LEN);
    panel.setSize(CELL_SIDE_LEN, CELL_SIDE_LEN);
    panel.add(tile);
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    this.spareTilePanel = panel;
  }

  public void addActionListener(ActionListener listener) {
    this.nextButton.addActionListener(listener);
    this.saveButton.addActionListener(listener);
  }
}
