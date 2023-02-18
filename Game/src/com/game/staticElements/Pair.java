package com.game.staticElements;

//Класс, который вычисляет, смогла ли отбиться защищающаяся карта

public class Pair {
    private final Card attacker; //Атакующая карта
    private final Card defender; //Защищающаяся карта

    private final boolean resultOfPair; //Результат в виде boolean, смогла ли отбиться защищающаяся карта

    //В конструкторе сразу определяется, смогла ли отбиться карта
    public Pair(Card attacker, Card defender) {
        this.attacker = attacker;
        this.defender = defender;
        resultOfPair = parryOrNot();
    }

    //Возвращает результат в виде boolean, смогла ли отбиться защищающаяся карта
    public boolean parryOrNot() {
        if ((attacker.isTrump() && defender.isTrump() || attacker.getColor().equals(defender.getColor()))) {
            return defender.getWeight().parryOrNot(attacker.getWeight());
        }
        if (!attacker.isTrump() && defender.isTrump()) {
            return true;
        }
        return false;
    }
}
