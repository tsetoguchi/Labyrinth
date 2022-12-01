package referee;

import java.util.Optional;

/**
 * Wraps a Move for the PlayerHandler,
 */
public class TurnWrapper {

    private final Optional<Move> turnPlan;
    private final boolean exception;

    public TurnWrapper(Optional<Move> turnPlan, boolean exception) {
        this.turnPlan = turnPlan;
        this.exception = exception;
    }

    public Optional<Move> getTurnPlan() {
        return this.turnPlan;
    }

    public boolean isException() {
        return this.exception;
    }
}
