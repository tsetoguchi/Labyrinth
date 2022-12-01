package referee;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import model.Position;
import model.state.PlayerAvatar;

/**
 * Distributes and manages goals for the given players
 */
public class GoalHandler {

  private final Map<PlayerAvatar, Position> currentGoals;
  private final Map<PlayerAvatar, Integer> goalCount;

  // goals to distribute amongst players
  private final Queue<Position> potentialGoals;

  private final Set<PlayerAvatar> playersGoingHome;
  private boolean anyPlayersHome;

  public GoalHandler(List<PlayerAvatar> players, List<Position> allGoals) {
    this.potentialGoals = new LinkedList<>();
    this.potentialGoals.addAll(allGoals);
    this.goalCount = new HashMap<>();
    this.currentGoals = new HashMap<>();
    this.anyPlayersHome = false;
    this.playersGoingHome = new HashSet<>();
    this.initializePlayerData(players);
  }


  /**
   * Increments the goal count of the given player and assigns them the appropriate goal. This
   * method is only called when a player reaches their current goal.
   */
  public void nextGoal(PlayerAvatar player) {

    Position nextGoal;
    if (!this.goalsLeft()) {
      nextGoal = player.getHome();
      this.playersGoingHome.add(player);
    } else {
      nextGoal = this.potentialGoals.poll();
      this.goalCount.replace(player, this.goalCount.get(player) + 1);
    }

    this.currentGoals.replace(player, nextGoal);
  }


  public boolean playerReachedGoal(PlayerAvatar player) {
    Position goal = this.currentGoals.get(player);
    boolean onGoal = player.getCurrentPosition().equals(goal);

    if(onGoal && this.playersGoingHome.contains(player)){
      this.anyPlayersHome = true;
    }

    return onGoal;
  }

  public Position getPlayerCurrentGoal(PlayerAvatar player) {
    return this.currentGoals.get(player);
  }

  public boolean anyPlayersHome() {
    return this.anyPlayersHome;
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
