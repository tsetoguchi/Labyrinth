Pair: plucky-bees \
Commit: [f99feb6318be0fa2d73b54d6ea7e36206b456693](https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/tree/f99feb6318be0fa2d73b54d6ea7e36206b456693) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/80d20effae8e9f48804037cfb3a3bfda852c9bd0/7/self-7.md \
Score: 134/205 \
Grader: Mike Delmonaco

## git log inspection (70pts)

-10 Did not use `git add remote` to transfer commit history from old repository 

NOTE: You should not have `.class` files in your repository.

https://github.com/github/gitignore/blob/main/Java.gitignore

## Tech debt downpayment (60 pts)

-10 Commit message "first push" is unclear. This is especially bad since you used this message for both loading in your old code and another commit that did other stuff.

These first few commits are very messy. You paste an entire repo instead of transferring history, and then you make another commit with the same message that adds in a bunch of class files _and_ has some actual changes.
Try to keep each commit focused on a single objective or task. If you're adding an interface, do that in one commit. If you're transferring a bunch of files or moving them around, do that in its own commit. If you want to figure out which commit this interface was added in, it would be difficult because it's buried in a commit with a ton of noise and a non-descriptive message.

Each commit should be focused on a single task. Each commit message should briefly describe the changes made, and you may elaborate further in the extended description of a commit message.

It is important to keep you commit history clean and clear. In the industry, other people will need to look at changes you've made and review them. If you give them one big commit that said "this week's changes", you will be making your reviewers' jobs more difficult, and they might even reject your changes outright for this reason. Also, if someone wants to see when a change was made, it'll be very difficult to figure that out if the history is unclear. You also might want to undo a change or edit a change (rebasing). This is common while addressing feedback in a code review. This is much more difficult on a messy history than a clean one.

What does the `Optional<Optional<TurnPlan>>` mean in `PlayerHandler#takeTurn`? Needs a purpose statement.

-10 Commit message "working observer?" is unclear. 

You should either elaborate in the commit message or include more details in the summary of the commit message.

You provided a nice description in your todo item, but your commit message is vague. You should have descriptive commit messages. At least elaborate in the summary of a commit.

-10 The commit for item 5 doesn't seem to have anything to do with your described change. If you make a list of TODO items, you should go one at a time and address each one with a commit or a sequence of commits. Then, you should link all the relevant commits, not just one of them that takes place after the whole thing got done over time.

-10 Same problem as above for item 6.

Item 8 is a better example of tackling a TODO item in a focused commit with a clear message.

## Required revision (40 pts code, 20pts self-eval)

-4 (partial credit for honesty) No unit test that validates the implementation of the relaxation (board is not 7 x 7)

-4 (partial credit for honesty) No unit test that validates the implementation of the relaxation (board is not square)

-4 (partial credit for honesty) No unit test that validates that a board is suitabile for the given number of players 

-4 (partial credit for honesty) No unit that that rejects a board as too small for the given number of players 

## Design (15 pts)

Stick to the memo format.

-5 Explanation for change 2 is unclear. The last sentence makes sense, but everything else is too vague.

Explanation for change 3 is vague. You only talk about what you would have to do, not how much work those changes would be and why.

These explanations are supposed to justify your difficulty estimate to a fomerly-coding higher-up at the company.
