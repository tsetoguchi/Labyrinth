package player;

import java.util.Optional;

/**
 * Wraps a Turn for the PlayerHandler,
 */
public class TurnWrapper {

    private final Optional<Turn> turnPlan;
    private final boolean exception;

    public TurnWrapper(Optional<Turn> turnPlan, boolean exception) {
        this.turnPlan = turnPlan;
        this.exception = exception;
    }

    public Optional<Turn> getTurnPlan() {
        return this.turnPlan;
    }

    public boolean isException() {
        return this.exception;
    }
}
