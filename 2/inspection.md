Pair: recursive-koodotk \
Commit: [a8583ecf2bcd71001a4c74c0cb46c5f8163a5732](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/tree/a8583ecf2bcd71001a4c74c0cb46c5f8163a5732) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/a8583ecf2bcd71001a4c74c0cb46c5f8163a5732/2/self-2.md \
Score: 70/80 \
Grader: Rajat Keshri \

Reasons: \

Self eval and Board - \
40/40 pts: an operation that determines the reachable tiles from some spot 
- [10/10] : is there a data def. for "coordinates" (places to go to)? 
- [10/10] : is there a signature/purpose statement? 
- [10/10] : is it clear how the method/function checks for cycles? 
- [10/10] : is there at least one unit test with a non-empty expected result 


20/30 pts: an operation for sliding a row or column in a direction \
- [10/10] sig/purp. 
- [0/10] the method should check that row/column has even index 
- [10/10] unit tests: at least one for rows and one for columns 

10/10: the functinality for inserting a tile into the open space \
- [5/5] : an operation for rotating and inserting the spare tile 
- [5/5] Provide clarity on what happens to the tile that's pushed out 

---------------------------------------------------------------------------
Design and data representation (ungraded)- \

- Need to include more data about what the referee knows about players \
  - their assigned home \
  - their assigned color \
  - their assigned "goal" tile (private to ref and the player) \
  - their current tile (where the avatar is located) \
- Need to include information about the last action performed (so it is not undone by the next player)
\<free text\>
