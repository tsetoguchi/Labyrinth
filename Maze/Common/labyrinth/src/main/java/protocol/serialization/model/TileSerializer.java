package protocol.serialization.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import game.model.Gem;
import game.model.Tile;

import java.io.IOException;

/**
 * JSON Serialization class for Tile.
 */
public class TileSerializer extends StdSerializer<Tile> {
    public TileSerializer() {
        this(null);
    }
    public TileSerializer(Class<Tile> t) {
        super(t);
    }

    @Override
    public void serialize(Tile tile, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("pathways", tile.toSymbol());
        gen.writeArrayFieldStart("gems");
        for (Gem gem : tile.getTreasure().getGems()) {
            gen.writeString(gem.toString());
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
