package remote;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {

  Server server;
  Client c1;
  Client c2;

  @Before
  public void setUp() throws Exception {
    this.server = new Server();
    this.c1 = new Client();
    this.c2 = new Client();

  }

  @Test
  public void main() throws IOException {

    this.server.main(new String[0]);

  }
}