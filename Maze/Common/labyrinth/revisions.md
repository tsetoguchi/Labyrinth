### To Do:

1. Move *IRules* form *Board* to *Referee*
2. Remove redundant Player Interfaces
3. Implement Goal logic in *Referee*
4. Build *JSON Serializer* and *Deserializer*
5. Redesign the majority of *State*
6. Redesign *Referee* such that it handles all goal logic
7. Build server-client architecture
8. Fix broken integration tests after redesign
9. Remove arbitrary funcitonality that does not match the spec in *Server*
   * "message client method"
10. Fix and create unit tests
11. Make ProxyReferee parse JSON scattered JSON
12. Ensure we enforce unique homes for *Players*

### Completed:
1. Fix *IPlayer* protocol to use *setUp*
2. Redesign use of *Projections*