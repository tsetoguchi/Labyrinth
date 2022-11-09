package game.referee;

import game.TestUtils;
import game.model.*;
import game.referee.client.*;
import org.junit.jupiter.api.Test;
import player.EuclideanStrategy;
import player.RiemannStrategy;
import player.TurnPlan;
import referee.PlayerResult;
import referee.Referee;
import referee.clients.PlayerClient;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RefereeTest {
    @Test
    public void testGameWithOnlyBoringPlayersEndsAfterOneRound() {
        BoredTestPlayerClient bored1 = new BoredTestPlayerClient();
        BoredTestPlayerClient bored2 = new BoredTestPlayerClient();
        BoredTestPlayerClient bored3 = new BoredTestPlayerClient();


        PlayerAvatar avatar1 = new PlayerAvatar(Color.BLUE, new Position(5, 5),
                new Position(1, 1));
        PlayerAvatar avatar2 = new PlayerAvatar(Color.GREEN, new Position(5, 5),
                new Position(1, 3));
        PlayerAvatar avatar3 = new PlayerAvatar(Color.RED, new Position(5, 5),
                new Position(1, 3));

        List<PlayerClient> clients = List.of(bored1, bored2, bored3);
        List<PlayerAvatar> avatars = List.of(avatar1, avatar2, avatar3);

        Game game = new Game(
                TestUtils.createUniformBoard(true, true, false, false),
                avatars
        );

        Referee referee = new Referee(game, clients);

        referee.runGame();

        assertEquals(GameStatus.ALL_SKIPPED, bored1.finalGameResult);
        assertEquals(GameStatus.ALL_SKIPPED, bored2.finalGameResult);
        assertEquals(GameStatus.ALL_SKIPPED, bored3.finalGameResult);

        assertEquals(PlayerResult.LOSER, bored1.finalPlayerResult);
        assertEquals(PlayerResult.WINNER, bored2.finalPlayerResult);
        assertEquals(PlayerResult.WINNER, bored3.finalPlayerResult);
    }

    @Test
    public void testGameWithObsessivePlayersWhoLoopEndsAfter1000Rounds() {
        ObsessiveTestPlayerClient ob1 = new ObsessiveTestPlayerClient(new TurnPlan(Direction.DOWN, 0, 0, new Position(0, 0)));
        ObsessiveTestPlayerClient ob2 = new ObsessiveTestPlayerClient(new TurnPlan(Direction.DOWN, 2, 0, new Position(0, 2)));

        PlayerAvatar avatar1 = new PlayerAvatar(Color.BLUE, new Position(5, 5),
                new Position(1, 1));
        avatar1.setCurrentPosition(new Position(0, 0));
        PlayerAvatar avatar2 = new PlayerAvatar(Color.RED, new Position(5, 5),
                new Position(1, 3));
        avatar2.setCurrentPosition(new Position(0, 2));


        List<PlayerClient> clients = List.of(ob1, ob2);
        List<PlayerAvatar> avatars = List.of(avatar1, avatar2);

        Game game = new Game(
                TestUtils.createUniformBoard(true, true, false, false),
                avatars
        );

        Referee referee = new Referee(game, clients);

        referee.runGame();

        assertEquals(GameStatus.ROUND_LIMIT_REACHED, ob1.finalGameResult);
        assertEquals(GameStatus.ROUND_LIMIT_REACHED, ob2.finalGameResult);

        assertEquals(PlayerResult.LOSER, ob1.finalPlayerResult);
        assertEquals(PlayerResult.WINNER, ob2.finalPlayerResult);
    }

    @Test
    public void testGameWithOnlyCheatersResultsInNoRemainingPlayersGameOver() {
        List<TestPlayerClient> testClients = new ArrayList<>();
        List<PlayerClient> clients = new ArrayList<>();
        List<PlayerAvatar> avatars = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            TestPlayerClient client = new NaughtyTestPlayerClient();
            testClients.add(client);
            clients.add(client);
            avatars.add(new PlayerAvatar(this.getDifferentColor(i),
                    new Position(5, 5), new Position(1, 1)));
        }

        Game game = new Game(
                TestUtils.createUniformBoard(true, true, true, true),
                avatars
        );

        Referee referee = new Referee(game, clients);

        referee.runGame();

        for (TestPlayerClient client : testClients) {
            assertNull(client.finalGameResult);
        }

        for (int i = 1; i < testClients.size(); i++) {
            assertNull(testClients.get(i).finalPlayerResult);
        }

        assertEquals(GameStatus.NO_REMAINING_PLAYERS, game.getGameStatus());
    }

    @Test
    public void testGameWithManyPlayersAndFullMobilityResultsInFirstPlayerWinning() {
        StrategyTestPlayerClient ob1 = new StrategyTestPlayerClient(new EuclideanStrategy());

        PlayerAvatar avatar1 = new PlayerAvatar(Color.BLUE, new Position(5, 5),
                new Position(1, 1));

        List<TestPlayerClient> testClients = new ArrayList<>();
        List<PlayerClient> clients = new ArrayList<>();
        testClients.add(ob1);
        clients.add(ob1);
        List<PlayerAvatar> avatars = new ArrayList<>();
        avatars.add(avatar1);

        for (int i = 0; i < 500; i++) {
            TestPlayerClient client = new StrategyTestPlayerClient(new EuclideanStrategy());
            testClients.add(client);
            clients.add(client);
            avatars.add(new PlayerAvatar(this.getDifferentColor(i),
                    new Position(5, 5), new Position(1, 1)));
        }
        for (int i = 500; i < 1000; i++) {
            TestPlayerClient client = new StrategyTestPlayerClient(new RiemannStrategy());
            testClients.add(client);
            clients.add(client);
            avatars.add(new PlayerAvatar(this.getDifferentColor(i),
                    new Position(5, 5), new Position(1, 1)));
        }

        Game game = new Game(
                TestUtils.createUniformBoard(true, true, true, true),
                avatars
        );

        Referee referee = new Referee(game, clients);

        referee.runGame();

        for (TestPlayerClient client : testClients) {
            assertEquals(GameStatus.TREASURE_RETURNED, client.finalGameResult);
        }

        assertEquals(PlayerResult.WINNER, ob1.finalPlayerResult);
        for (int i = 1; i < testClients.size(); i++) {
            assertEquals(PlayerResult.LOSER, testClients.get(i).finalPlayerResult);
        }
    }

    @Test
    public void testGameWithMixOfBoredPlayersAndNaughtyPlayersResultsInOnlyBoredPlayersAtEnd() {
        List<TestPlayerClient> goodClients = new ArrayList<>();
        List<TestPlayerClient> badClients = new ArrayList<>();
        List<PlayerClient> clients = new ArrayList<>();
        List<PlayerAvatar> avatars = new ArrayList<>();

        int numGoodPlayers = 5;

        for (int i = 0; i < 5; i++) {
            TestPlayerClient client = new NaughtyTestPlayerClient();
            badClients.add(client);
            clients.add(client);
            avatars.add(new PlayerAvatar(this.getDifferentColor(i),
                    new Position(5, 5), new Position(1, 1)));
        }
        for (int i = 5; i < 5 + numGoodPlayers; i++) {
            TestPlayerClient client = new BoredTestPlayerClient();
            goodClients.add(client);
            clients.add(client);
            avatars.add(new PlayerAvatar(this.getDifferentColor(i),
                    new Position(5, 5), new Position(1, 1)));
        }

        Game game = new Game(
                TestUtils.createUniformBoard(true, true, true, true),
                avatars
        );

        Referee referee = new Referee(game, clients);

        referee.runGame();

        for (TestPlayerClient client : goodClients) {
            assertEquals(GameStatus.ALL_SKIPPED, client.finalGameResult);
        }
        for (TestPlayerClient client : badClients) {
            assertNull(client.finalGameResult);
        }

        for (TestPlayerClient client : goodClients) {
            assertEquals(PlayerResult.WINNER, client.finalPlayerResult);
        }
        for (TestPlayerClient client : badClients) {
            assertNull(client.finalPlayerResult);
        }
    }

    private Color getDifferentColor(int i) {
        return new Color(i % 256, (i/256) % 256, 0);
    };
}
