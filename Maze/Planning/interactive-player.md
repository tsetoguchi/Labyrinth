<p style="text-align: center;">Business Memorandum</p>

TO: Ben Lerner

FROM: Kyle Koo and Duncan Vogel

DATE: 10/27/2022

SUBJECT: Graphical user interface design for Labyrinth

Now that we have implemented the core game logic, we have begun to consider the graphical
UI that players can use to interact with the game. The following is a design of
the UI while the game is in progress, and separate considerations will be made for 
signup and prizes when those functionalities are implemented.

The basic display for the entire game until an end is reached consists of a main window
with three heading bars - one which either says "Opponent's Turn" or "Your Turn", to indicate
who is acting. Underneath this banner, there will be an area which displays instructions -
either to wait or a description of the action that needs to be performed. Finally, the last banner
will have a box for each player with text of that player's color and displaying their name. The active
player's box will be highlighted at all times.

In the main area of the window, the board will take up most of the screen, left-justified, leaving
a column of space to the right where the spare tile is shown as well as turn-specific buttons. Each
tile on the board will have a background showing the pathway, the two gems in the upper left and lower
right corners, and each player on the tile in the center (or averaging in the center if multiple players).
Off the pathways, in one of the empty corners, will be shown a player's goal tile if there is one
on the tile (the avatar and the home tile will be the same color as the player). The goal tile will
be highlighted in the player's color, until the player has reached it, in which case the home tile
will be highlighted instead and the instruction banner will preface its text with "Treasure reached - Return home!"

At the beginning of a player's turn, a red "PASS" button will appear that can be clicked if desired. Additionally,
rotate buttons will appear around the spare tile so the player can rotate it to their liking. There will also be arrows that appear next to the ends of each slidable
row and column, pointing in the direction that the slide will take place (except for the slide which
reverses the previous one). Clicking on one of these buttons will insert the spare tile, however
the player has rotated it, into the board after making the indicated slide. This will remove the "PASS"
button.

At this point, the player will be prompted to pick a tile to move to, and each tile which
can be reached will be highlighted. If no tiles are reachable, this consists of a game over for the player,
with a screen displaying "GAME OVER - You got stuck." Otherwise, the player will click on a tile
to select a move and then the turn will advance to the next player.

Once the game is over, the window will cease its previous display and instead show "GAME OVER - xyz",
where xyz is the cause of the game end. Namely, this is one of "You won!", "Player xyz won.", or "Draw by repetition."
Despite the UI not providing a way to cheat besides getting stuck, if the player is kicked for some
reason the display will be "You were kicked."