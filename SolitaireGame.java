import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

class SolitaireGame {
    private ArrayList<Pile> tableau;
    private Pile stock;
    private Pile waste;
    private ArrayList<Pile> foundations;
    private Deck deck;
    private int moveCount;
    private static final String[] SUIT_SYMBOLS = {
        "\u2660\uFE0F", "\u2665\uFE0F", "\u2666\uFE0F", "\u2663\uFE0F"
    };

    public SolitaireGame() {
        tableau = new ArrayList<>();
        stock = new Pile();
        waste = new Pile();
        foundations = new ArrayList<>();
        setupGame();
    }

    private void setupGame() {
        deck = new Deck();
        tableau.clear();
        stock.clear();
        waste.clear();
        foundations.clear();
        moveCount = 0;

        for (int i = 0; i < 7; i++) {
            Pile pile = new Pile();
            for (int j = 0; j <= i; j++) {
                Card card = deck.drawCard();
                if (j == i) {
                    card.flip();
                }
                pile.add(card);
            }
            tableau.add(pile);
        }

        while (!deck.isEmpty()) {
            stock.add(deck.drawCard());
        }

        for (int i = 0; i < 4; i++) {
            foundations.add(new Pile());
        }
    }

    private boolean canPlaceOnTableau(Card moving, Pile dest) {
        if (dest.isEmpty()) {
            return moving.getRankIndex() == 12;
        }
        Card top = dest.peek();
        return top.getRankIndex() == moving.getRankIndex() + 1
                && !top.getColor().equals(moving.getColor());
    }

    private boolean canPlaceOnFoundation(Card card, Pile foundation) {
        if (foundation.isEmpty()) {
            return card.getRankIndex() == 0;
        }
        Card top = foundation.peek();
        return top.getRankIndex() + 1 == card.getRankIndex();
    }

    public void drawFromStock() {
        if (stock.isEmpty()) {
            if (waste.isEmpty()) {
                System.out.println("Stock and waste are both empty.");
                return;
            }
            stock.transferAllFrom(waste);
            stock.reverse();
        }
        Card card = stock.removeTop();
        card.flip();
        waste.add(card);
        moveCount++;
    }

    public void moveTableau(int fromPile, int cardPosition, int toPile) {
        if (fromPile < 0 || fromPile >= 7 || toPile < 0 || toPile >= 7) {
            System.out.println("Pile numbers must be between 1 and 7.");
            return;
        }
        if (fromPile == toPile) {
            System.out.println("Choose two different tableau piles.");
            return;
        }

        Pile from = tableau.get(fromPile);
        Pile to = tableau.get(toPile);

        if (from.isEmpty()) {
            System.out.println("That pile is empty.");
            return;
        }
        if (cardPosition < 1 || cardPosition > from.size()) {
            System.out.println("That card position is not in the pile.");
            return;
        }

        int index = cardPosition - 1;
        Card movingCard = from.get(index);
        if (!movingCard.isFaceUp()) {
            System.out.println("Cannot move a face-down card.");
            return;
        }

        if (!canPlaceOnTableau(movingCard, to)) {
            if (to.isEmpty()) {
                System.out.println("Only a King can be moved to an empty pile.");
            } else {
                System.out.println("Cards must be one rank lower and the opposite color.");
            }
            return;
        }

        ArrayList<Card> movingCards = from.removeFrom(index);
        to.addAll(movingCards);
        from.flipTopIfFaceDown();
        moveCount++;
    }

    public void moveWasteToTableau(int toPile) {
        if (toPile < 0 || toPile >= 7) {
            System.out.println("Pile numbers must be between 1 and 7.");
            return;
        }
        if (waste.isEmpty()) {
            System.out.println("Waste is empty.");
            return;
        }

        Card card = waste.peek();
        Pile dest = tableau.get(toPile);
        if (!canPlaceOnTableau(card, dest)) {
            if (dest.isEmpty()) {
                System.out.println("Only a King can be moved to an empty pile.");
            } else {
                System.out.println("Cards must be one rank lower and the opposite color.");
            }
            return;
        }

        dest.add(waste.removeTop());
        moveCount++;
    }

    public void moveWasteToFoundation() {
        if (waste.isEmpty()) {
            System.out.println("Waste is empty.");
            return;
        }
        if (tryMoveToFoundation(waste.peek(), waste)) {
            moveCount++;
        }
    }

    public void moveToFoundation(int pile) {
        if (pile < 0 || pile >= 7) {
            System.out.println("Pile numbers must be between 1 and 7.");
            return;
        }
        Pile from = tableau.get(pile);
        if (from.isEmpty()) {
            System.out.println("That pile is empty.");
            return;
        }
        if (tryMoveToFoundation(from.peek(), from)) {
            from.flipTopIfFaceDown();
            moveCount++;
        }
    }

    /** Moves the top card onto the matching-suit foundation if the ranks allow it. */
    private boolean tryMoveToFoundation(Card card, Pile fromPile) {
        if (!card.isFaceUp()) {
            System.out.println("Cannot move a face-down card.");
            return false;
        }

        Pile foundation = foundations.get(card.getSuitIndex());
        if (!canPlaceOnFoundation(card, foundation)) {
            if (foundation.isEmpty()) {
                System.out.println("Only an Ace can start a foundation.");
            } else {
                System.out.println("That card is not the next rank for this foundation.");
            }
            return false;
        }

        foundation.add(fromPile.removeTop());
        return true;
    }

    public void display() {
        System.out.println("\nMoves: " + moveCount);
        System.out.println("Stock: " + stock.size() + " cards");
        System.out.println("Waste: " + (waste.isEmpty() ? "[ ]" : waste.peek()));
        System.out.println("\nFoundations:");
        for (int i = 0; i < 4; i++) {
            System.out.print(SUIT_SYMBOLS[i] + ": ");
            if (foundations.get(i).isEmpty()) {
                System.out.println("[ ]");
            } else {
                System.out.println(foundations.get(i).peek());
            }
        }
        System.out.println("\nTableau (leftmost card is position 1):");
        for (int i = 0; i < 7; i++) {
            System.out.print((i + 1) + ": ");
            Pile pile = tableau.get(i);
            if (pile.isEmpty()) {
                System.out.print("[ ]");
            } else {
                for (int j = 0; j < pile.size(); j++) {
                    System.out.print(pile.get(j) + " ");
                }
            }
            System.out.println();
        }
    }

    public boolean checkWin() {
        for (int i = 0; i < foundations.size(); i++) {
            if (foundations.get(i).size() != 13) {
                return false;
            }
        }
        return true;
    }

    private void printHelp() {
        System.out.println("\nKlondike Solitaire (draw 1)");
        System.out.println("- Tableau builds down by alternating colors (red/black).");
        System.out.println("- Only a King can go on an empty tableau pile.");
        System.out.println("- Foundations build up by suit, Ace through King.");
        System.out.println("- Face-down cards are shown as [X].");
        System.out.println("- For tableau moves, position 1 is the leftmost card in that pile.");
        System.out.println("- When the stock is empty, drawing recycles the waste pile.");
        System.out.println("- Win by filling all four foundations.");
    }

    private int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private void writeSessionSummary(boolean won) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        try {
            PrintWriter out = new PrintWriter(new FileWriter("game-summary.txt", true));
            out.println("Date: " + timestamp);
            out.println("Result: " + (won ? "WIN" : "QUIT"));
            out.println("Moves: " + moveCount);
            out.println("---");
            out.close();
            System.out.println("Session saved to game-summary.txt");
        } catch (IOException e) {
            System.out.println("Could not write game-summary.txt: " + e.getMessage());
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        boolean won = false;

        while (true) {
            display();
            if (checkWin()) {
                System.out.println("You won!");
                won = true;
                break;
            }

            System.out.println("\n1. Draw from stock");
            System.out.println("2. Move tableau card(s)");
            System.out.println("3. Move tableau card to foundation");
            System.out.println("4. Move waste card to tableau");
            System.out.println("5. Move waste card to foundation");
            System.out.println("6. Help / rules");
            System.out.println("7. Restart");
            System.out.println("8. Quit");

            int choice = readInt(scanner, "Choose: ");
            if (choice == 1) {
                drawFromStock();
            } else if (choice == 2) {
                int from = readInt(scanner, "From pile (1-7): ") - 1;
                int pos = readInt(scanner, "Card position in the pile (1 is leftmost): ");
                int to = readInt(scanner, "To pile (1-7): ") - 1;
                moveTableau(from, pos, to);
            } else if (choice == 3) {
                int pile = readInt(scanner, "From tableau pile (1-7): ") - 1;
                moveToFoundation(pile);
            } else if (choice == 4) {
                int to = readInt(scanner, "Move waste to tableau pile (1-7): ") - 1;
                moveWasteToTableau(to);
            } else if (choice == 5) {
                moveWasteToFoundation();
            } else if (choice == 6) {
                printHelp();
            } else if (choice == 7) {
                setupGame();
                System.out.println("New game started.");
            } else if (choice == 8) {
                break;
            } else {
                System.out.println("Enter a number from 1 to 8.");
            }
        }

        writeSessionSummary(won);
        scanner.close();
    }
}
