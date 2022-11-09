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
public class PublicPlayerProjection {
    PlayerAvatar player;

    public PublicPlayerProjection(PlayerAvatar player) {
        this.player = player;
    }

    public Position getAvatarPosition() {
        return this.player.getCurrentPosition();
    }

    public Position getHomePosition() {
        return this.player.getHomePosition();
    }

    public Color getColor() { return this.player.getColor(); }
}
