package com.game.staticElements;

public class Card {
    private final Color color; //Масть карты
    private final Weight weight; //Вес карты

    private final boolean trump; //Содержит в себе в виде boolean, являет ли карты козырем

    Card(Color color, Weight weight, boolean trump) {
        this.color = color;
        this.weight = weight;
        this.trump = trump;
    }

    //Возвращает строчное представление карты в строчного представления ее масти и значения
    @Override
    public String toString() {
        return color.toString() + weight.toString();
    }

    //Возвращает в виде boolean, является ли карта козырем
    boolean isTrump() {
        return trump;
    }

    //Возвращает масть карты
    Color getColor() {
        return this.color;
    }

    //Возвращает значение карты
    Weight getWeight() {
        return this.weight;
    }
}
