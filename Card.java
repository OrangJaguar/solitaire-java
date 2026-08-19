class Card {
    private String suit;
    private String rank;
    private boolean faceUp;
    private int suitIndex;
    private int rankIndex;
    private final String[] suits = {
        "\u2660\uFE0F", "\u2665\uFE0F", "\u2666\uFE0F", "\u2663\uFE0F"
    };
    private final String[] ranks = {
        "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"
    };

    public Card(int suitIndex, int rankIndex) {
        this.suitIndex = suitIndex;
        this.rankIndex = rankIndex;
        this.suit = suits[suitIndex];
        this.rank = ranks[rankIndex];
        this.faceUp = false;
    }

    /** Turns the card face up. Cards are never turned face down again. */
    public void flip() {
        faceUp = true;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public int getRankIndex() {
        return rankIndex;
    }

    public int getSuitIndex() {
        return suitIndex;
    }

    public String getColor() {
        if (suitIndex == 0 || suitIndex == 3) {
            return "black";
        } else {
            return "red";
        }
    }

    public String toString() {
        return faceUp ? rank + suit : "[X]";
    }
}
