package game;

import network.TCPConnection;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final int id; //id игрока в виде int
    private final String nickname; //Никнейм игрока, который выбирает сам игрок

    private final List<Card> hand = new ArrayList<>(); //Колода карт в руке игрока
    private final TCPConnection myConnection; //Соединение человека, который является данным игроком

    public Player(int id, String nickname, TCPConnection connection) {
        this.id = id;
        this.nickname = nickname;
        myConnection = connection;
    }

    //Возвращает в виде boolean, есть ли карты в руке игрока
    public boolean haveCardsInHand() {
        return !hand.isEmpty();
    }

    //Возвращает в виде int, сколько карт не хватает игроку
    int cardsNeed() {
        int diff = 6 - getHandSize();
        return diff < 0 ? 0 : diff;
    }

    //Возвращает количество карт в руке игрока
    int getHandSize() {
        return hand.size();
    }

    //Забирает данную карту себе в руку
    public void grabCard(Card card) {
        hand.add(card);
    }

    //Забирает все карты из данной коллекции себе в руку
    public void grabCards(List<Card> cards) {
        for (Card card : cards) {
            hand.add(card);
        }
    }

    //Присылает игроку в виде строки на экран его карты
    void drawCards() {
        myConnection.sendString(stringCards());
    }

    //Возвращает экзмепляр соединения игрока
    public TCPConnection getMyConnection() {
        return myConnection;
    }

    //Возвращает карту, под данным номером
    private Card chooseCard(int number) {
        System.out.println("Брошена карта " + hand.get(number - 1));
        Card card = hand.get(number - 1);
        return card;
    }

    //Возвращает в виде int, сколько карт может подбросить игрок в данном раунде
    public int howMuchCanIThrow() {
        if (getHandSize() >= 6) {
            return 6;
        }
        return getHandSize();
    }

    //Возвращает в виде строки карты в руке игрока
    private String stringCards() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ваши карты:  ");
        int handSize = getHandSize();
        for (int card = 0; card < handSize; card++) {
            sb.append((card + 1) + ":" + hand.get(card) + "  ");
        }
        return sb.toString();
    }

    //Возвращает карту, которую выберет игрок из руки
    public Card chooseCard() {
        while (true) {
            myConnection.sendString("Выберите карту: ");
            try {
                String numberString = myConnection.sendString();
                int number = Integer.parseInt(numberString);
                if (number < 1 || number > getHandSize()) {
                    myConnection.sendString("Карты под таким номером нет.");
                    myConnection.sendString("Введите заново");
                    continue;
                } else {
                    return chooseCard(number);
                }
            } catch (Exception e) {
                myConnection.sendString("Введите цифру!");
                continue;
            }
        }

    }

    //Выкидывает данную карту из руки
    public void throwCard(Card card) {
        hand.remove(card);
    }

    //Возвращает в виде строки ник игрока
    @Override
    public String toString() {
        return nickname;
    }

}
