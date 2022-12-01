package referee;

import java.io.PipedOutputStream;
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

  private final Map<PlayerAvatar, Position> currentGoals;

  // goal count of each player
  private final Map<PlayerAvatar, Integer> goalCount;

  // goals to distribute amongst players
  private final Queue<Position> potentialGoals;

  public GoalHandler(List<PlayerAvatar> players, List<Position> allGoals) {
    this.potentialGoals = new LinkedList<>();
    this.potentialGoals.addAll(allGoals);
    this.goalCount = new HashMap<>();
    this.currentGoals = new HashMap<>();
    this.initializePlayerData(players);
  }


  /**
   * Increments the goal count of the given player and assigns them the appropriate goal. This
   * method is only called when a player reaches their current goal.
   *
   * @param player
   */
  public void nextGoal(PlayerAvatar player) {

    int goalCount = this.goalCount.get(player);
    goalCount++;
    this.goalCount.replace(player, goalCount);

    Position nextGoal;
    if (!this.goalsLeft()) {
      nextGoal = player.getHome();
    } else {
      nextGoal = this.potentialGoals.poll();
    }


    this.currentGoals.replace(player, nextGoal);
  }

  public boolean playerReachedHome() {
    for (PlayerAvatar player : this.currentGoals.keySet()) {
      if (this.playerReachedGoal(player) && player.getCurrentPosition().equals(player.getHome())
          && !this.goalsLeft()) {
        return true;
      }
    }
    return false;
  }

  public boolean playerReachedGoal(PlayerAvatar player) {
    Position goal = this.currentGoals.get(player);
    return player.getCurrentPosition().equals(goal);
  }

  public Position getPlayerCurrentGoal(PlayerAvatar player) {
    return this.currentGoals.get(player);
  }


  public int getPlayerGoalCount(PlayerAvatar player) {
    return this.goalCount.get(player);
  }

  private boolean goalsLeft() {
    return !this.potentialGoals.isEmpty();
  }

  /**
   * Assigns each player an intiial goal and sets their goal counts to 0
   *
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
