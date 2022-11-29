package IntegrationTests;

/**
 * The entry point for all test harnesses. Takes in an argument for which harness to run and executes it.
 */
public class TestRunner {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Tried to run TestRunner without passing in exactly one set of tests.");
        }

        switch (args[0]) {
            case "board":
                StandardBoardIntegrationTest.executeTest();
                break;
            case "model":
                GameStateIntegrationTest.executeTest();
                break;
            case "strategy":
                StrategyIntegrationTest.executeTest();
                break;
                // 6
            case "referee":
                GameRefereeIntegrationTest.executeTest(false);
                break;
                //6
            case "referee-obs":
                GameRefereeIntegrationTest.executeTest(true);
                break;
            case "referee-xbad": // 7
                GameRefereeIntegrationExceptionsTest.executeTest();
                break;
            case "referee-xbaddy": // 8
                XBad2.executeTest();
                break;
            default:
                throw new IllegalArgumentException("test set not recognized");
        }
    }
}
