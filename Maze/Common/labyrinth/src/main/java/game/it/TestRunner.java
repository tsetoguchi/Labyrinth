package game.it;

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
            case "game":
                GameStateIntegrationTest.executeTest();
                break;
            case "strategy":
                StrategyIntegrationTest.executeTest();
                break;
            case "referee":
                GameRefereeIntegrationTest.executeTest(false);
                break;
            case "referee-obs":
                GameRefereeIntegrationTest.executeTest(true);
                break;
            default:
                throw new IllegalArgumentException("test set not recognized");
        }
    }
}
