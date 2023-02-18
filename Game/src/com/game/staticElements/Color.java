package com.game.staticElements;
/*
* Масти карт
* */
enum Color {
    CLUBS(0, "♣"),
    HEARTS(1, "♥"),
    DIAMONDS(2, "♦"),
    SPADES(3, "♠");
    private final String name;
    private final int order;
    int getOrder(){
        return order;
    }

    @Override
    public String toString(){
        return name;
    }

    Color(int order, String name) {
        this.order = order;
        this.name = name;
    }
}
