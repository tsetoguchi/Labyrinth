### To Do:

1. Fix old broken integration tests after redesign 
2. Fix and create more unit tests
3. Make Server check for player names as they connect

### Completed:
1. Fix *IPlayer* protocol to follow spec guidelines
2. Redesign use of *Projections*
3. Move *IRules* from *Board* to *Referee*
4. Remove arbitrary functionality that does not match the spec in *Server*
   * "message client method"
5. Ensure we enforce unique homes for *Players*
6. Build server-client architecture 
7. Redesign *Referee* such that it handles all goal logic (for this we plan 
to implement a GoalHandler that'll pass out goals to players)
8. Redesign the majority of *State* (the old implementation had many holes)
9. Build *JSON Serializer* and *Deserializer*
10. Remove redundant Player Interfaces
11. Move from using Turn and TurnPlan to just ITurn, Move, and Pass
12. Redesign strategies to use ITurn
13. Built TestPlayer to handle all integration players erroring out
