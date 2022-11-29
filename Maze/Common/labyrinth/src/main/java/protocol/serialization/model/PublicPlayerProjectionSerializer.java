package protocol.serialization.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import game.model.projections.PlayerProjection;
import java.awt.*;
import java.io.IOException;

/**
 * JSON serialization class for PublicPlayerProjection.
 */
public class PublicPlayerProjectionSerializer extends StdSerializer<PlayerProjection> {
    public PublicPlayerProjectionSerializer() {
        this(null);
    }
    public PublicPlayerProjectionSerializer(Class<PlayerProjection> t) {
        super(t);
    }

    @Override
    public void serialize(PlayerProjection playerProjection, JsonGenerator gen,
                          SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("color", this.getColorString(playerProjection.getColor()));
        gen.writeObjectField("home", playerProjection.getHomePosition());
        gen.writeObjectField("avatar", playerProjection.getAvatarPosition());
        gen.writeEndObject();
    }

    /**
     * Get the hexcode String for a Color. For serialization, all colors become hexcode, even if they were
     * created using an enumerated value.
     */
    private String getColorString(Color c) {
        String red = this.getHexPadded(c.getRed());
        String green = this.getHexPadded(c.getGreen());
        String blue = this.getHexPadded(c.getBlue());

        return "#" + red + blue + green;
    }

    /**
     * Gets the hex code for an integer and normalize to two digits.
     */
    private String getHexPadded(int i) {
        String baseHex = Integer.toHexString(i);
        if (baseHex.length() == 1) {
            return "0" + baseHex;
        }
        return baseHex;
    }
}
