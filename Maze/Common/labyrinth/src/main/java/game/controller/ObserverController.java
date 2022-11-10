package game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.model.projections.ObserverGameProjection;
import game.view.OldObserverView;
import protocol.serialization.MazeJsonSerializer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 * The controller which accepts updates from the referee and draws the board state, using interactive buttons
 * to switch between states shown.
 */
public class ObserverController implements ActionListener, IObserver {

    private final OldObserverView view;

    private ObserverGameProjection currentState;
    private Queue<ObserverGameProjection> nextStates;
    private boolean gameOver;

    public ObserverController(ObserverGameProjection currentState) {
        this.currentState = currentState;
        this.gameOver = false;

        this.view = new OldObserverView(currentState);
        this.nextStates = new LinkedList<>();
        this.view.addActionListener(this);
    }

    /**
     * Accept a new state from the referee and add it to the queue of states that can be displayed.
     */
    @Override
    public void update(ObserverGameProjection newState) {
        this.nextStates.add(newState);
        this.view.enableNextButton(true);
    }


    /**
     * Accept an update from the referee informing the observer that the game is over.
     */
    @Override
    public void gameOver() {
        this.gameOver = true;
    }

//    public void initialize(ObserverGameProjection initialState) {
//        this.acceptState(initialState);
//        this.advanceState();
//        this.view.setVisible(true);
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Next":
                int i = 0;
                this.advanceState();
                break;
            case "Save":
                String encodedState = this.encodeState();
                Optional<String> selectedFilePath = this.view.selectFile();
                if (selectedFilePath.isPresent()) {
                    this.saveToFile(selectedFilePath.get(), encodedState);
                }
                break;
        }
    }

    /**
     * Advance the current state view to the next state in the queue.
     */
    private void advanceState() {
        if (!this.nextStates.isEmpty()) {
            this.currentState = this.nextStates.remove();
            this.updateView();
        }
    }

    private void updateView() {
        if (this.nextStates.isEmpty() && this.gameOver) {
            System.out.println("game over*************************");
//            this.view.displayGameOver();
        } else {
            this.view.updateView(this.currentState);
        }
    }

    private String encodeState() {
        MazeJsonSerializer serializer = new MazeJsonSerializer();
        try {
            return serializer.observerGameToJson(this.currentState);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private void saveToFile(String path, String value) {
        System.out.println("Trying to save to " + path);
        try {
            File newFile = new File(path);
            newFile.createNewFile();
            FileWriter writer = new FileWriter(path);
            writer.write(value);
            writer.close();
            System.out.println("Successfully saved to file " + path);
        } catch (IOException e) {
            System.out.println("Failed to write to file" + path);
        }
    }
}
