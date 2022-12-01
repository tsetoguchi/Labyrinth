package IntegrationTests;

import org.json.JSONTokener;

import java.util.Scanner;

public class IntegrationUtils {

  public static JSONTokener getInput() {
    Scanner sc = new Scanner(System.in);
    StringBuilder str = new StringBuilder();
    while (sc.hasNext()) {
      str.append(sc.next());
    }
    return new JSONTokener(str.toString());
  }

}
