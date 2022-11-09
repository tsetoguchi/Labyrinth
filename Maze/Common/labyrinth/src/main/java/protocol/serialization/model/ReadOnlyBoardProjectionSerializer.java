package protocol.serialization.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import game.model.Position;
import game.model.projections.ReadOnlyBoardProjection;

import java.io.IOException;

/**
 * JSON serialization class for ReadOnlyBoardProjection.
 */
public class ReadOnlyBoardProjectionSerializer extends StdSerializer<ReadOnlyBoardProjection> {
    public ReadOnlyBoardProjectionSerializer() {
        this(null);
    }
    public ReadOnlyBoardProjectionSerializer(Class<ReadOnlyBoardProjection> t) {
        super(t);
    }

    @Override
    public void serialize(ReadOnlyBoardProjection readOnlyBoardProjection, JsonGenerator gen,
                          SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeArrayFieldStart("tileGrid");
        for (int row = 0; row < readOnlyBoardProjection.getHeight(); row++) {
            gen.writeStartArray();
            for (int col = 0; col < readOnlyBoardProjection.getWidth(); col++) {
                gen.writeObject(readOnlyBoardProjection.getTileAt(new Position(row, col)));
            }
            gen.writeEndArray();
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
