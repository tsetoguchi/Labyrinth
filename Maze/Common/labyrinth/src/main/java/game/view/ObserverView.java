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
    ObserverGameProjection currentState;
    BoardView boardFrame;
    TileView spareTile;

    JButton nextButton;
    JButton saveButton;

    private static final int CELL_SIDE_LEN = 80;

    public ObserverView(ObserverGameProjection currentState) {
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.currentState = currentState;
//        this.setPreferredSize(new Dimension(1000, 800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        this.currentState = newState;
        this.setVisible(false);
        this.remove(this.boardFrame);
        this.remove(this.spareTile);
        this.revalidate();
        this.repaint();
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
        this.boardFrame = new BoardView(this.currentState.getBoard(), this.currentState.getPlayers(), CELL_SIDE_LEN);
        this.spareTile = new TileView(this.currentState.getBoard().getSpareTile(), CELL_SIDE_LEN);
        this.boardFrame.populateBoard();
        this.repaint();
    }

    private void drawCurrentState() {
        this.boardFrame = new BoardView(this.currentState.getBoard(), this.currentState.getPlayers(), CELL_SIDE_LEN);
        this.spareTile = new TileView(this.currentState.getBoard().getSpareTile(), CELL_SIDE_LEN);
        this.boardFrame.populateBoard();
        this.add(this.boardFrame);
        this.addSpareTile(this.spareTile);
        this.pack();
        this.boardFrame.repaint();
        this.spareTile.repaint();
        this.repaint();
        this.revalidate();
        this.setVisible(true);
        System.out.println("");
    }

    private void addSpareTile(TileView tile) {
        JPanel panel = new JPanel();
        panel.setBounds(30, 700, CELL_SIDE_LEN, CELL_SIDE_LEN);
        panel.setSize(CELL_SIDE_LEN, CELL_SIDE_LEN);
        panel.add(tile);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(panel);
    }

    public void addActionListener(ActionListener listener) {
        this.nextButton.addActionListener(listener);
        this.saveButton.addActionListener(listener);
    }
}
