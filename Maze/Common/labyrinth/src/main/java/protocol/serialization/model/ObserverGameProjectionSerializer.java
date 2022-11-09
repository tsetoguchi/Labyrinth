package protocol.serialization.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import game.model.projections.ObserverGameProjection;
import game.model.projections.PublicPlayerProjection;

import java.io.IOException;

/**
 * JSON serialization class for ObserverGameProjection.
 */
public class ObserverGameProjectionSerializer extends StdSerializer<ObserverGameProjection> {
    public ObserverGameProjectionSerializer() {
        this(null);
    }
    public ObserverGameProjectionSerializer(Class<ObserverGameProjection> t) {
        super(t);
    }

    @Override
    public void serialize(ObserverGameProjection observerGameProjection, JsonGenerator gen,
                          SerializerProvider serializerProvider) throws IOException {

        gen.writeStartObject();
        gen.writeObjectField("board", observerGameProjection.getBoard());
        gen.writeArrayFieldStart("players");
        for (PublicPlayerProjection player : observerGameProjection.getPlayers()) {
            gen.writeObject(player);
        }
        gen.writeEndArray();
        gen.writeNumberField("activePlayer", observerGameProjection.getActivePlayer());
        gen.writeEndObject();
    }
}
