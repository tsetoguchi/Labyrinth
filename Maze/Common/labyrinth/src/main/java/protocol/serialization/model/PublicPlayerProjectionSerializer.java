package protocol.serialization.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import game.model.projections.PublicPlayerProjection;

import java.awt.*;
import java.io.IOException;

/**
 * JSON serialization class for PublicPlayerProjection.
 */
public class PublicPlayerProjectionSerializer extends StdSerializer<PublicPlayerProjection> {
    public PublicPlayerProjectionSerializer() {
        this(null);
    }
    public PublicPlayerProjectionSerializer(Class<PublicPlayerProjection> t) {
        super(t);
    }

    @Override
    public void serialize(PublicPlayerProjection publicPlayerProjection, JsonGenerator gen,
                          SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("color", this.getColorString(publicPlayerProjection.getColor()));
        gen.writeObjectField("home", publicPlayerProjection.getHomePosition());
        gen.writeObjectField("avatar", publicPlayerProjection.getAvatarPosition());
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
