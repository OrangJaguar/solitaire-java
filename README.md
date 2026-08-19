# Java Solitaire

I made a Klondike solitaire game that runs in the terminal. You compile it with javac, type numbers to make moves, and the board prints out after every turn. Face down cards show up as `[X]`.

It's draw-one, same rules as normal Klondike: tableau goes down in rank and has to alternate red/black, only a king can go on an empty pile, foundations go Ace to King by suit. When the stock is empty, drawing flips the waste back into the stock.

There's a move counter, a help option, and you can restart if the deal is awful. If you type letters instead of a number it just asks again instead of crashing. When you win or quit it appends a little recap to `game-summary.txt` (date, WIN or QUIT, how many moves). I focused on draw-one gameplay and did not implement undo, draw-three, or saved board state.

## Files

- `Main.java` – starts the game
- `Card.java` – suit, rank, color, whether it's face up
- `Deck.java` – 52 cards, shuffled
- `Pile.java` – one stack of cards (stock, waste, a tableau column, or a foundation)
- `SolitaireGame.java` – dealing, legal moves, printing the board, the menu

I split piles into their own class because I kept writing `pile.get(pile.size() - 1)` everywhere. Tableau vs foundation rules are still just methods in `SolitaireGame` because those rules are different.

Collections are typed, like `ArrayList<Card>` and `ArrayList<Pile>`.

## Run it

Needs a JDK (Java 8+). From this folder:

```
javac *.java
java Main
```

## Menu

1. Draw from stock
2. Move cards between tableau piles (position 1 is the leftmost card in that pile)
3. Tableau → foundation
4. Waste → tableau
5. Waste → foundation
6. Help
7. Restart
8. Quit

## What I actually had to figure out

Most of the work was keeping track of where cards live and not letting illegal moves through. Scanner's `nextInt()` was annoying (garbage input killed the program, leftover newlines messed up the next prompt) so I read a whole line and parse it.

The session file is just `FileWriter` in a try/catch. If it can't write, it prints an error and still exits. Restarting does not write a log, only leaving the game does.
