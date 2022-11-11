# TODO:

3. Handle timeouts for Player moves and Observer within Referee
4. Update the Observer so it displays Gem images
5. Add missing elements Logical Interactions to the Player and PlayerClient interfaces so that it's up to spec
6. Add missing documentation for PlayerClient
7. Add getSlidable to the Board interface to better facilitate different board sizes
8. Add a check to enforce the distinctness of home tiles

# Completed

1. [Create Observer interface](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/commit/602946b14bdbd7eecabda19dc4778ea4e4c74509#diff-2f7651fc581a46024b4dfa77723c9cc8174a11c08b079acee0c2ffe8b9f3c85e)
2. [Fix the Observer so that it updates the state in the GUI](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/commit/6caa4fd9d51517f2d37257499a764d8666eb18b4) - The fix was implementing a deepcopy for each state that was passed from the referee to the observer. Some minor changes were made to the ObserverView as well.