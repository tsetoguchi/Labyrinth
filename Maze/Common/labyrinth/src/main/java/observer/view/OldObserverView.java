//package observer.view;
//
//import model.projections.StateProjection;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.util.Optional;
//
///**
// * Renders the board and the interactive panels for the observer, and detects mouse input.
// */
//public class OldObserverView extends JFrame {
//
//  private StateProjection currentState;
//
//  private OldBoardView boardFrame;
//  private JPanel spareTilePanel;
//
//  private JButton nextButton;
//  private JButton saveButton;
//
//  private static final int CELL_SIDE_LEN = 80;
//
//  public OldObserverView(StateProjection currentState) {
//
//    this.setLayout(new GridBagLayout());
//    this.currentState = currentState;
//
//    this.boardFrame = this.currentBoardView();
//    this.addSpareTile(currentSpareTileView());
//
//    this.add(this.spareTilePanel);
//
//
//    this.nextButton = new JButton("Next");
//    this.nextButton.setActionCommand("Next");
//    this.add(this.nextButton);
//
//    this.saveButton = new JButton("Save");
//    this.saveButton.setActionCommand("Save");
//    this.add(this.saveButton);
//
//    this.initialize();
//    this.drawCurrentState();
//  }
//
//  private void initialize() {
//    this.setLayout(new GridBagLayout());
//    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
//    this.setLocationRelativeTo(null);
//    this.pack();
//    this.setVisible(true);
//  }
//
//  public void updateView(StateProjection newState) {
////    System.out.println("Updating");
////
//    this.remove(this.boardFrame);
//    this.remove(this.spareTilePanel);
//
//
////    System.out.println("Current IState: " + this.currentState.toString());
//    this.currentState = newState;
////    System.out.println("New IState: " + this.currentState.toString());
//
//    this.drawCurrentState();
//  }
//
//  public Optional<String> selectFile() {
//    JFrame saveFrame = new JFrame();
//
//    JFileChooser fileChooser = new JFileChooser();
//    fileChooser.setDialogTitle("Specify a file to save the state in");
//
//    int userSelection = fileChooser.showSaveDialog(saveFrame);
//    File saveFile;
//
//    if (userSelection == JFileChooser.APPROVE_OPTION) {
//      saveFile = fileChooser.getSelectedFile();
//      return Optional.of(saveFile.getAbsolutePath());
//    }
//    return Optional.empty();
//  }
//
//  public void enableNextButton(boolean enabled) {
//    this.nextButton.setEnabled(enabled);
//  }
//
//  private void drawNewState() {
//    this.boardFrame = this.currentBoardView();
//    this.boardFrame.populateBoard();
//    this.repaint();
//  }
//
//  private void drawCurrentState() {
//    this.boardFrame = this.currentBoardView();
//
//    this.boardFrame.populateBoard();
//
//    this.add(this.boardFrame);
//    this.addSpareTile(currentSpareTileView());
//
//    this.pack();
//    this.revalidate();
//    this.repaint();
////    this.boardFrame.repaint();
////    this.spareTile.repaint();
////    this.repaint();
//    this.setVisible(true);
//    System.out.println("");
//  }
//
//  private OldTileView currentSpareTileView() {
//    return new OldTileView(this.currentState.getBoard().getSpareTile(), CELL_SIDE_LEN);
//  }
//
//  private OldBoardView currentBoardView() {
//    return new OldBoardView(this.currentState.getBoard(), this.currentState.getPlayers(),
//        CELL_SIDE_LEN);
//  }
//
//  private void addSpareTile(OldTileView tile) {
//    JPanel panel = new JPanel();
//    panel.setBounds(this.getBounds().height + 300, 700, CELL_SIDE_LEN, CELL_SIDE_LEN);
//    panel.setSize(CELL_SIDE_LEN, CELL_SIDE_LEN);
//    panel.add(tile);
//    panel.setBorder(BorderFactory.createLineBorder(Color.black));
//    this.spareTilePanel = panel;
//  }
//
//  public void addActionListener(ActionListener listener) {
//    this.nextButton.addActionListener(listener);
//    this.saveButton.addActionListener(listener);
//  }
//}
