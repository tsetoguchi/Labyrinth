package model.projections;

import model.state.PlayerAvatar;
import model.Position;

import java.awt.*;

/**
 * A read-only projection of a player which allows access to public information.
 */
public class PlayerProjection {

    private final Position currentPosition;

    private final Position homePosition;

    private final Color color;

    public PlayerProjection(PlayerAvatar player) {
        this.currentPosition = player.getCurrentPosition();
        this.homePosition = player.getHome();
        this.color = player.getColor();
    }

    public PlayerProjection(Color color, Position current, Position home) {
        this.currentPosition = current;
        this.homePosition = home;
        this.color = color;
    }

    public Position getCurrentPosition() {
        return this.currentPosition;
    }

    public Position getHomePosition() {
        return this.homePosition;
    }

    public Color getColor() { return this.color; }
}
