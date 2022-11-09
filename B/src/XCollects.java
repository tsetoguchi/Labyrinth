import java.util.Scanner;
import java.util.Set;

public class XCollects {
    private static final Set<String> ACCEPTABLE_INPUTS = Set.of("\"┘\"", "\"┐\"", "\"└\"", "\"┌\"");
    private static final String UNACCEPTABLE_INPUT_MSG = "unacceptable input";

    public static void main(String[] args) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("\"");

        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            String inputLine = input.nextLine();
            if (!ACCEPTABLE_INPUTS.contains(inputLine)) {
                System.out.println(UNACCEPTABLE_INPUT_MSG);
                System.exit(1);
            }
            else {
                char inputChar = inputLine.charAt(1); // always at index 1, after quotation mark
                resultBuilder.append(inputChar);
            }
        }

        resultBuilder.append("\"");
        String result = resultBuilder.toString();

        System.out.println(result);
    }
}
