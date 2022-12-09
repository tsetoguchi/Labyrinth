package referee;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import model.Position;
import model.state.PlayerAvatar;

/**
 * Distributes and manages goals for the given players
 */
public class GoalHandler {

  // the current goals for each player
  private final Map<PlayerAvatar, Position> currentGoals;

  // the count of goals reached for each player
  private final Map<PlayerAvatar, Integer> goalCount;

  // remaining goals to distribute amongst players
  private final Queue<Position> potentialGoals;

  // the players on their way home
  private final Set<PlayerAvatar> playersGoingHome;
  private Optional<PlayerAvatar> playerHome;

  /**
   * Creates a goal handler
   * @param players the players of the game
   * @param allGoals the sequence of goals that the referee will hand out
   */
  public GoalHandler(List<PlayerAvatar> players, List<Position> allGoals) {
    this.potentialGoals = new LinkedList<>();
    this.potentialGoals.addAll(allGoals);
    this.goalCount = new HashMap<>();
    this.currentGoals = new HashMap<>();
    this.playerHome = Optional.empty();
    this.playersGoingHome = new HashSet<>();
    this.initializePlayerData(players);
  }


  /**
   * Increments the goal count of the given player and assigns them the appropriate goal. This
   * method is only called when a player reaches their current goal.
   */
  public void nextGoal(PlayerAvatar player) {

    if(this.potentialGoals.size() > 0){
      this.assignNextGoal(player);
      return;
    }

    Position playerHome = player.getHome();
    this.playersGoingHome.add(player);
    this.currentGoals.replace(player, playerHome);

  }

  /**
   * Assigns the given player the next goal in the queue.
   */
  private void assignNextGoal(PlayerAvatar player){
    Position nextGoal = this.potentialGoals.poll();
    this.goalCount.replace(player, this.goalCount.get(player) + 1);
    this.currentGoals.replace(player, nextGoal);
  }


  /**
   * Returns true if the given player is on their goal
   */
  public boolean playerReachedGoal(PlayerAvatar player) {
    Position goal = this.currentGoals.get(player);
    boolean onGoal = player.getCurrentPosition().equals(goal);

    if(onGoal && this.playersGoingHome.contains(player)){
      this.playerHome = Optional.of(player);
    }

    return onGoal;
  }

  /**
   * Gets the given player's current goal
   */
  public Position getPlayerCurrentGoal(PlayerAvatar player) {
    return this.currentGoals.get(player);
  }

  /**
   * Returns true if any players have returned home after getting all potential goals
   */
  public boolean anyPlayersHome() {
    return this.playerHome.isPresent();
  }

  /**
   * Returns an optional of the player who ended the game by reaching their home.
   * If the optional is empty then no player has reached their home and ended the game.
   */
  public Optional<PlayerAvatar> getPlayerHome(){
    return this.playerHome;
  }

  /**
   * Returns the number of goals this player has reached
   */
  public int getPlayerGoalCount(PlayerAvatar player) {
    return this.goalCount.get(player);
  }


  /**
   * Assigns each player an initial goal and sets their goal counts to 0
   */
  private void initializePlayerData(List<PlayerAvatar> players) {
    for (PlayerAvatar player : players) {
      Position goal = this.potentialGoals.poll();
      this.currentGoals.put(player, goal);
      this.goalCount.put(player, 0);
    }
  }



}
