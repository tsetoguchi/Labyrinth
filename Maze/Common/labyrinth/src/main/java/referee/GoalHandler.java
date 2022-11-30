package referee;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import model.Position;
import model.state.PlayerAvatar;
import player.Player;

/**
 * Distributes and manages goals for the given players
 */
public class GoalHandler {

  private Map<PlayerAvatar, Position> currentGoals;
  private Map<PlayerAvatar, Integer> goalCount;
  private Queue<Position> potentialGoals;

  public GoalHandler(List<PlayerAvatar> players, List<Position> allGoals) {
    this.potentialGoals = new LinkedList<>();
    this.potentialGoals.addAll(allGoals);
    this.goalCount = new HashMap<>();
    this.currentGoals = new HashMap<>();
    this.initializePlayerData(players);
  }

  private Position generatePotentialGoals() {
    for (int i = 0; i < totalGoals; i++) {
      Position currentPosition = Util.
    }
  }

  private int goalCountFormula(int playerCount) {
    return playerCount * 2;
  }

  /**
   * Assigns each player an intiial goal and sets their goal counts to 0
   * @param players
   */
  private void initializePlayerData(List<PlayerAvatar> players) {
    for (PlayerAvatar player : players) {
      Position goal = this.potentialGoals.poll();
      this.currentGoals.put(player, goal);
      this.goalCount.put(player, 0);
    }
  }

}
