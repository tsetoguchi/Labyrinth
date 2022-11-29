package game.model.projections;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import game.model.PlayerAvatar;
import game.model.Position;
import protocol.serialization.model.PublicPlayerProjectionSerializer;

import java.awt.*;

/**
 * A read-only projection of a player which allows access to public information.
 */
@JsonSerialize(using = PublicPlayerProjectionSerializer.class)
public class PlayerProjection {

    private final Position avatarPosition;

    private final Position homePosition;

    private final Color color;

    public PlayerProjection(PlayerAvatar player) {
        this.avatarPosition = player.getCurrentPosition();
        this.homePosition = player.getHome();
        this.color = player.getColor();
    }

    public Position getAvatarPosition() {
        return this.avatarPosition;
    }

    public Position getHomePosition() {
        return this.homePosition;
    }

    public Color getColor() { return this.color; }
}
