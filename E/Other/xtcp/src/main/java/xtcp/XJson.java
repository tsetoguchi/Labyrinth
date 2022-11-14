package xtcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 */
public class XJson {

  public static void main(String[] args) {
    String input = readInput();
    System.out.println(getJsonFromInput(input));
  }

  public static String getJsonFromInput(String input) {

    List<String> objects = getJsonObjects(input);
    List<String> characters = new ArrayList<String>();

    if (objects == null) {
      System.out.println("Invalid input");
      System.exit(1);
    }

    for (String object : objects) {
      String character = getChar(object);
      if (character == null) {
        System.out.println("Invalid input");
        System.exit(1);
      }
      characters.add(character);
    }
    ObjectMapper mapper = new ObjectMapper();
    String result = null;
    try {
      result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(characters);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  private static String getChar(String json) {

    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode actualObj = mapper.readTree(json);
      String vertical = actualObj.get("vertical").asText();
      String horizontal = actualObj.get("horizontal").asText();
      if (vertical.equals("DOWN") && horizontal.equals("LEFT")) {
        return "┐";
      }
      if (vertical.equals("DOWN") && horizontal.equals("RIGHT")) {
        return "┌";
      }
      if (vertical.equals("UP") && horizontal.equals("LEFT")) {
        return "┘";
      }
      if (vertical.equals("UP") && horizontal.equals("RIGHT")) {
        return "└";
      }
    } catch (IOException e) {
      return null; // JSON incorrectly formed
    }
    return null; // JSON incorrectly formed
  }

  private static String readInput() {
    StringBuilder input = new StringBuilder();
    Scanner inputScanner = new Scanner(System.in);
    while (inputScanner.hasNext()) {
      input.append(inputScanner.next());
    }
    return input.toString();
  }

  private static List<String> getJsonObjects(String input) {
    List<String> objects = new ArrayList<>();
    String remainingInput = "" + input;
    while (remainingInput.length() > 0) {
      if (!remainingInput.contains("{") || !remainingInput.contains("}")) {
        return null;
      }
      objects.add(
          remainingInput.substring(remainingInput.indexOf("{"), remainingInput.indexOf("}") + 1));
      remainingInput = remainingInput.substring(remainingInput.indexOf("}") + 1);
    }
    return objects;
  }
}
