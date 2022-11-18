**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 8

Indicate below each bullet which file/unit takes care of each task.

For `Maze/Remote/player`,

- explain how it implements the exact same interface as `Maze/Player/player`\
[Lines in Remote/player implementing setup, takeTurn, win](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/ProxyPlayer.java#L49-L112)\
[Lines in Player/player implementing setup, takeTurn, win](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/player/Player.java#L25-L43)\
These methods naturally all have the exact same signatures. However, they do not share the exact same interface due to an oversight. This can be easily refactored.
- explain how it receives the TCP connection that enables it to communicate with a client\
[The remote player contains a socket](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/ProxyPlayer.java#L27-L47)
that it streams its output through the socket's output stream, utilizing a PrintWriter.\
The remote player then loops until it receives a response from the socket.
- point to unit tests that check whether it writes JSON to a mock output device\
  We do not have unit tests for this case.

For `Maze/Remote/referee`,

- explain how it implements the same interface as `Maze/Referee/referee`
We did not finish our implementation of the [ProxyReferee](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/ProxyReferee.java#L1-L30). Our referee interface contains the methods involved in running a game, which are not methods that ProxyReferee should share.
- explain how it receives the TCP connection that enables it to communicate with a server\
The referee is stored in the [client](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/PlayerClient.java#L46-L47)
and whenever the client receives a message from its socket it immediately passes it on to the referee.
Looking at it again however the way it is implemented is incomplete and would not work.
- point to unit tests that check whether it reads JSON from a mock input device\
  We do not have unit tests for this case.

For `Maze/Client/client`, explain what happens when the client is started _before_ the server is up and running:
Our design assumed that the server would be running before any clients are started. However the client was designed such that it can be [constructed](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/PlayerClient.java#L22-L26) and attempt to connect to a server with another [method call](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/PlayerClient.java#L35-L53).
- does it wait until the server is up (best solution)
It does not wait until the server is up before attempting to connect its socket.
- does it shut down gracefully (acceptable now, but switch to the first option for 9)
The client does not shutdown gracefully if it is ran before the server.

For `Maze/Server/server`, explain how the code implements the two waiting periods:  \
The server runs a beginSignUp method that enters the first waiting period. For the [first waiting period](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/Server.java#L57-L87), the server checks the current time and sets an end time 20 seconds from then. The server then waits for a client to connect, using an executor thread so that [if the 20 seconds pass the accept() call times out](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/Server.java#L144-L156). If a client does connect with the 20 seconds, then a [timeout period of 2 seconds if given](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/Server.java#L165-L178). This timeout is intentionally separate from the 20 seconds of the waiting period as we felt if a client connects at 19 seconds they should still have the full 2 seconds to provide their name.

The second waiting period has the same logic as the initial waiting period, just without the player count checks that the first waiting period has after the time is up.
- is it baked in? (unacceptable after Milestone 7)
[Yes.](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/Server.java#L57-L87)
It's baked in [here](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/cd9666f077d3a4ee7bfdab9f0a25a9597de9f0b3/Maze/Common/labyrinth/src/main/java/remote/Server.java#L173-L180)
as well.
- parameterized by a constant (correct).
It does not parameterize the timeouts.

The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

