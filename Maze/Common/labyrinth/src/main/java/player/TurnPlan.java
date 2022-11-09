package player;

import game.model.Direction;
import game.model.Position;

/**
 * The set of actions that a player intends to perform on its turn.
 */
public class TurnPlan {
    private final Direction slideDirection;
    private final int slideIndex;
    private final int spareTileRotations;
    /** The position (after sliding and inserting) where the player intends to end up. **/
    private final Position moveDestination;

    public TurnPlan(Direction slideDirection, int slideIndex, int spareTileRotations, Position moveDestination) {
        this.slideDirection = slideDirection;
        this.slideIndex = slideIndex;
        this.spareTileRotations = spareTileRotations;
        this.moveDestination = moveDestination;
    }

    public Direction getSlideDirection() {
        return slideDirection;
    }

    public int getSlideIndex() {
        return slideIndex;
    }

    public int getSpareTileRotations() {
        return spareTileRotations;
    }

    public Position getMoveDestination() {
        return moveDestination;
    }
}
