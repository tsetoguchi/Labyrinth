package model.board;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents an unordered pair of gems.
 */
public class Treasure {

  private final List<Gem> gems;

  /**
   * Creates a new Treasure and enforces that there are exactly 2 gems.
   */
  public Treasure(List<Gem> gems) {
    if (gems.size() != 2) {
      throw new IllegalArgumentException("Tried to create a treasure without exactly 2 gems");
    }
    this.gems = gems;
  }

  /**
   * Convenience constructor for testing which creates a new Treasure with the given two gems.
   */
  public Treasure(Gem gem1, Gem gem2) {
    this.gems = new ArrayList<>();
    this.gems.add(gem1);
    this.gems.add(gem2);
  }

  public List<Gem> getGems() {
    return this.gems;
  }

  @Override
  public boolean equals(Object other) {
      if (this == other) {
          return true;
      }
      if (other == null || this.getClass() != other.getClass()) {
          return false;
      }
    Treasure otherTreasure = (Treasure) other;
    if (otherTreasure.getGems().size() != this.gems.size()) {
      return false;
    }
    return this.gems.containsAll(otherTreasure.getGems()) && otherTreasure.getGems()
        .containsAll(this.gems);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("(");
    for (int i = 0; i < this.gems.size(); i++) {
      builder.append(this.gems.get(i).toString());
      if (i < this.gems.size() - 1) {
        builder.append(", ");
      }
    }
    builder.append(")");
    return builder.toString();
  }

  public String[] toStringArray() {
    String[] gemsStrings = new String[2];
    gemsStrings[0] = this.gems.get(0).withDashes();
    gemsStrings[1] = this.gems.get(1).withDashes();
    return gemsStrings;
  }

}
