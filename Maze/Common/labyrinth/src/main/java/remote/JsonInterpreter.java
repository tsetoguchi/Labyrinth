package remote;

import java.io.IOException;

/**
 * Represents a JSON interpreter that takes in a string
 */
public interface JsonInterpreter {

  void interpretJson(String json) throws IOException;

}
