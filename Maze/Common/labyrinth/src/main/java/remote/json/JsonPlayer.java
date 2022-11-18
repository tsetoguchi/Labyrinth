package remote.json;

public JsonPlayer(
@JsonProperty("current") JsonCoordinate current,
@JsonProperty("home") JsonCoordinate home,
@JsonProperty("color") String color
        ) {
        // Treasure position is not accessed within this class, thus
        // an invalid position is used on purpose to instantiate the superclass.

        super(JsonUtil.stringToColor(color), home, new Position(-10, -10));
        this.setPosition(current);
        }

protected JsonPlayer(Color color, Position home, Position treasure,
        Position current, boolean visitedTreasure) {
        super(color, home, treasure, current, visitedTreasure);
        }
