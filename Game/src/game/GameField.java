package game;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GameField {
    private final Deck deck; //Колода
    private final List<Card> cardsOnTheField = new ArrayList<>(); //Карты на поле
    private final Player player1; //Игрок номер 1
    private final Player player2; //Игрок номер 2
    private final Color trump; //Масть козыря

    public GameField(Player player1, Player player2) {
        deck = new Deck();
        this.trump = deck.getTrump();
        this.player1 = player1;
        this.player2 = player2;
    }

    //Возвращает в виде коллекции колоду
    private Deque<Card> getDeck() {
        return deck.getDeck();
    }

    //Возвращает масть козыря
    public Color getTrump() {
        return deck.getTrump();
    }

    //Возвращает в виде коллекции карты на поле
    public List<Card> getCardsOnTheField() {
        return cardsOnTheField;
    }

    //Возвращает в виде int, сколько карт осталось в колоде
    public int numberCardsInDeck() {
        return deck.numberOfCards();
    }

    //Возвращает  в виде int, сколько карт лежит на поле
    private int numberCardsOnTheField() {
        return cardsOnTheField.size();
    }

    //Возвращает в виде boolean, есть ли еще карты в колоде
    public boolean haveCardsInDeck() {
        return !getDeck().isEmpty();
    }

    //Раздает карты двум игрокам
    public void dealCards() {
        dealCards(player1);
        dealCards(player2);
    }

    //Очищает поле от карт
    public void clearCardsField() {
        cardsOnTheField.clear();
    }

    //Раздает карты данному игроку
    private void dealCards(Player player) {
        int howMuchDeal = howMuchDeal(player);
        for (int card = 0; card < howMuchDeal; card++) {
            player.grabCard(throwCard());
        }
    }

    //Достает одну карту из колоды
    private Card throwCard() {
        return deck.throwCard();
    }

    //Представляет в видео строки карты на поле
    public String stringCardsOnTheField() {
        StringBuilder sb = new StringBuilder();
        sb.append("Карты на поле: ");
        for (int card = 0, cardsOnTheField = numberCardsOnTheField(); card < cardsOnTheField; card++) {
            sb.append(this.cardsOnTheField.get(card) + "  ");
        }
        return sb.toString();
    }

    //Возвращает в виде boolean, можно ли подбросить данную карту
    public boolean canThrowThis(Card card) {
        if (cardsOnTheField.isEmpty()) {
            return true;
        }
        for (Card cardOnTheField : cardsOnTheField) {
            if (card.getWeight().equals(cardOnTheField.getWeight())) {
                return true;
            }
        }
        return false;
    }

    //Отрисовывает карты игрокам
    public void drawCards() {
        player1.drawCards();
        player2.drawCards();
    }

    //Возвращает в виде int, сколько карт колода может выдать карт данному игроку
    private int howMuchDeal(Player player) {
        int cardsNeed = player.cardsNeed();
        return numberCardsInDeck() >= cardsNeed ? cardsNeed : numberCardsInDeck();
    }

    //Добавляет данную карту на поле
    public void addCard(Card card) {
        cardsOnTheField.add(card);
    }
}
