# Labyrinth Game

## Directory Structure

Common: This folder contains the components shared by the referee and the players.
Planning: This folder contains memos which outline the plan for the project and its design.

## Roadmap

This project uses standard Java (Maven) directory structure. For each kind of component (common, referee, player, etc),
there is a Maven project (lowercase named folder) that contains a normal Java source code structure `src/main/...` and
`src/test/...`
The `Common/labyrinth` project contains common components, a `Board` interface, and all the related and subordinate
data types, as well as a standard implementation.
General structure: \
Board \
&nbsp;&nbsp;&nbsp;⤷ Tile \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;⤷ Treasure \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;⤷ Gem \
Using Position and Direction as shared data representations

## Testing
Run `sh ./xtest` to run all tests and see the number of test cases passed/failed.
From inside `Maze/Common/labyrinth`, run `mvn clean test` to run all tests with verbose output or run `mvn clean test -Dtest=TestClassName` to run a specific
test.

### Integration Tests
The `src` directory contains `game.it`, which are the integration test classes. Normally, integration tests would
be a separate project entirely, but for Labyrinth, we need everything in the same project. Therefore, we have placed
them in their own source directory so they can be packaged and run as an executable.