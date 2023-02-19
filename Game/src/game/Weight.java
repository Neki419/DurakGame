package game;

/*
 * Значения карт
 * */

enum Weight implements Comparable<Weight> {
    SIX(6, "6"), SEVEN(7, "7"),
    EIGHT(8, "8"), NINE(9, "9"),
    TEN(10, "10"), JACK(11, "J"),
    QUEEN(12, "Q"), KING(13, "K"),
    ACE(14, "A");
    private final int ordinal;
    private final String name;

    @Override
    public String toString() {
        return name;
    }

    //Сравнивает, больше ли данный экзмепляр веса карты по своему номеру
    boolean parryOrNot(Weight weight2) {
        return this.ordinal > weight2.ordinal;
    }

    Weight(int ord, String name) {
        this.ordinal = ord;
        this.name = name;
    }
}
