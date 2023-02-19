package game;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Deck {
    private Deque<Card> deck;
    private Color trump;

    Deck() {
        int trumpNumber = ThreadLocalRandom.current().nextInt(4); //Создает рандомное число от 0 до 3
        trump = Color.values()[trumpNumber]; //Берет одну из мастей на основе этого числа
        List<Card> tempDeck = new ArrayList<>(); //Временная коллекция для колоды, чтобы восползьоваться методом shuffle
        //Создание коллекции
        for (Color c : Color.values()) {
            int ord = c.getOrder();
            for (Weight w : Weight.values()) {
                tempDeck.add(new Card(c, w, ord == trumpNumber));
                Collections.shuffle(tempDeck);
            }
        }
        //Создание используемой коллекции
        deck = new ArrayDeque<>();
        for (Card card : tempDeck) {
            deck.addFirst(card);
        }
    }

    //Возвращет козырь из колоды
    Color getTrump() {
        return trump;
    }

    //Бросает одну карту из колоды
    Card throwCard() {
        return deck.pollFirst();
    }

    //Возвращает колоду в виде коллекции
    Deque<Card> getDeck() {
        return deck;
    }

    //Возвращает количество карт в колоде
    int numberOfCards() {
        return deck.size();
    }

    //Возвращает колоду как последовательность карт в виде строки
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : deck) {
            sb.append(card + " \n");
        }
        return sb.toString();
    }
}

