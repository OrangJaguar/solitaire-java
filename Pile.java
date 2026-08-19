import java.util.ArrayList;
import java.util.Collections;

/**
 * A single stack of cards (stock, waste, one tableau column, or one foundation).
 */
class Pile {
    private ArrayList<Card> cards;

    public Pile() {
        cards = new ArrayList<>();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public Card get(int index) {
        return cards.get(index);
    }

    public Card peek() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(cards.size() - 1);
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void addAll(ArrayList<Card> movingCards) {
        cards.addAll(movingCards);
    }

    public Card removeTop() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }

    /** Removes this card and every card above it (toward the top of the pile). */
    public ArrayList<Card> removeFrom(int index) {
        ArrayList<Card> movingCards = new ArrayList<>();
        while (cards.size() > index) {
            movingCards.add(cards.remove(index));
        }
        return movingCards;
    }

    public void flipTopIfFaceDown() {
        if (!cards.isEmpty() && !peek().isFaceUp()) {
            peek().flip();
        }
    }

    public void transferAllFrom(Pile other) {
        cards.addAll(other.cards);
        other.cards.clear();
    }

    public void reverse() {
        Collections.reverse(cards);
    }

    public void clear() {
        cards.clear();
    }
}
