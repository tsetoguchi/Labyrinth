package player;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Wraps a TurnPlan for the PlayerHandler,
 */
public class TurnWrapper {

    private final Optional<TurnPlan> turnPlan;
    private boolean exception;

    public TurnWrapper(Optional<TurnPlan> turnPlan, boolean exception) {
        this.turnPlan = turnPlan;
        this.exception = exception;
    }

    public Optional<TurnPlan> getTurnPlan() {
        return this.turnPlan;
    }

    public boolean isException() {
        return this.exception;
    }
}
