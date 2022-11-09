<p style="text-align: center;">Business Memorandum</p>

TO: Ben Lerner

FROM: Kyle Koo and Duncan Vogel

DATE: 09/30/2022

SUBJECT: Outline for Sprints 1-3

We're ready to get started on development for Labyrinth and have the following outline for the first three sprints:

Sprint 1:
Develop the game manager that handles the internal game logic and data representations for the board and game pieces. 

Sprint 2:
Develop the player-referee protocol for game actions and build a limited version of the referee that takes protocol messages and updates the game-state and returns protocol messages with result. This iteration of the referee will not yet manage the communication layer, moderation, or currency.

Sprint 3:
Develop a server that runs the referee and game manager. It will accept game actions over the internet from manually configured players and updates the game accordingly.

After these three sprints we will still have to handle referee moderation, observers, currency and sign-ups (and potential house players).