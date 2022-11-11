## Accommodating Changes

We will evaluate the level of difficulty of implementing the following changes.

1. Blank tiles on the board
2. Movable tiles as goals
3. Players pursue individual goals one at a time


### 1
Difficulty: 3  
The level of difficulty of this change is dependent on what a "blank tile" would be considered. If a "blank tile" entails that the tile does not contain any pathways, it would simply mean that we would keep the set of directions within the Tile empty. However, if a "blank tile" means that the Tile is completely empty, then we would have to implement a different implementation that represents a "blank tile".

### 2
Difficulty: 4  
We imagine that implementing this change to be the most challenging given that the Rules of our Game are coupled with the Board component. We would have to redesign our Game such that changes to the Rules of the Game are more simple to implement. Furthermore we would also have to consider that goal Tiles can move during any turn, which would make calculating whether the goal Tile is reachable for each player more complex.

### 3
Difficulty: 2  
This change would be the most simple to implement because it would mean that we only have to adjust a few different aspects relevant to the Players. Firstly, every Player would have to keep track of their individual goals. They would also have to keep track of whether every goal as been reached. Lastly, the Referee and Game would have to change the conditions in which Players would be considered winners.

