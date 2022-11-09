package game.model.projections;

import game.model.PlayerAvatar;
import game.model.Position;

/**
 * A read-only projection of a player which provides access to public and player-private information.
 */
public class SelfPlayerProjection {

    private final PlayerAvatar player;

    public SelfPlayerProjection(PlayerAvatar player) {
        this.player = player;
    }

    public Position getAvatarPosition() {
        return this.player.getCurrentPosition();
    }

    public Position getHomePosition() {
        return this.player.getHomePosition();
    }

    public Position getGoalPosition() {
        return this.player.getGoalPosition();
    }

    public boolean hasReachedGoal() {
        return this.player.hasReachedGoal();
    }
}
