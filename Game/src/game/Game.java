package game;

import network.TCPConnectionListener;

public class Game {
    private final GameField gameField; //Игровое поле
    private Player attacker; //Атакующий в данном раунде
    private Player defender; //Защищающийся в данном раунде
    /*
     * Слушатель соединений,
     * который будет делать опредлеленные действия в ответ на другие
     */
    private final TCPConnectionListener actionListener;


    public Game(Player player1, Player player2, TCPConnectionListener listener) {
        gameField = new GameField(player1, player2); //Создание игрового поля
        actionListener = listener;
        attacker = player1; //Первый игрок, который присоединился - атакующий
        defender = player2; //Второй игрок, который соединился - защищающийся
        actionListener.onReceiveStringToAll("Игра началась!"); //Отсылает сообщение всем игрокам

        //Буквально делаем новые раунды, пока не будут соблюдены условия окончания игры
        do {
            new Round();
        }
        while (!endOfGameConditions());
        //Выбор и объявление победителя
        actionListener.onReceiveStringToAll("Победил " + chooseWinner() + "!!!");
    }

    //Возвращает boolean true, если соблюдены условия окончания игры, false если игра не окончилась
    private boolean endOfGameConditions() {
        return (!gameField.haveCardsInDeck()) && (!attacker.haveCardsInHand() || !defender.haveCardsInHand());
    }

    //Возвращает игрока, который выиграл игру
    private Player chooseWinner() {
        if (!attacker.haveCardsInHand() && !defender.haveCardsInHand()) {
            return defender;
        }
        if (!attacker.haveCardsInHand()) {
            return attacker;
        }
        return defender;
    }

    //Меняет местами атакующего и защищающегося
    private void swapAttacker() {
        Player temp = attacker;
        attacker = defender;
        defender = temp;
    }

    //Раздает карты
    private void dealCards() {
        gameField.dealCards();
    }

    //Отправляет игрокам сообщение о том, кто ходит, а кто защищается
    private void announceWhoGoesFirst() {
        actionListener.onReceiveStringTo(attacker.getMyConnection(), "Вы ходите");
        actionListener.onReceiveStringTo(defender.getMyConnection(), "Вы защищаетесь");
    }

    //Отправляет в виде строки свои карты игрокам
    private void drawCards() {
        gameField.drawCards();
    }

    //Отправляет карты, которые есть на поле, в виде строки игрокам
    private void drawCardsOnTheField() {
        if (!gameField.getCardsOnTheField().isEmpty()) {
            actionListener.onReceiveStringToAll(gameField.stringCardsOnTheField());
        }
    }

    private class Round {
        //Магическое число, которое при подставлении в граничное условие цикла заканчивает раунд
        private final int END_ROUND = 777;

        private Round() {
            dealCards(); //Раздача карт
            announceWhoGoesFirst(); //Оповещение кто ходит, кто защищается
            int cardsCanThrow = attacker.howMuchCanIThrow(); //Хранит, сколько карт может подбросить атакующий
            for (int move = 0; move < cardsCanThrow; move++) {
                //Объявление козыря
                actionListener.onReceiveStringToAll("Козырь " + gameField.getTrump());
                //Объявление, сколько карт осталось в колоде
                actionListener.onReceiveStringToAll(gameField.numberCardsInDeck() + " карт осталось в колоде");
                //Отрисовка карт игроков
                drawCards();
                //Отрисовка карт на столе
                drawCardsOnTheField();
                //Инициализация атакующей и защищающейся карты
                Card attackerCard = null;
                Card defenderCard = null;
                //Ход атакующего
                while (true) {
                    //Если ход первый в раунде, атакующий просто выбирает карту
                    if (move == 0) { //
                        attackerCard = attacker.chooseCard();
                        actionListener.onReceiveStringToAll(attacker + " бросает на поле " + attackerCard);
                        attacker.throwCard(attackerCard);
                        gameField.addCard(attackerCard);
                        break;
                    }
                    //В ином случае атакующий выбирает, будет подбрасывать или бита
                    else {
                        //Атакующий выбирает, будет подкидывать или бита
                        String number = chooseAttackerAction();
                        /*Ветка, если атакующий выбрал подкинуть и если карту,
                        которую он выбрал, действительно можно подкинуть*/
                        if (number.equals("1") && gameField.canThrowThis(attackerCard = attacker.chooseCard())) {
                            actionListener.onReceiveStringToAll(attacker + " бросает на поле " + attackerCard);
                            //Удаляет карту из рук атакующего и подкидывает ее на поле
                            attacker.throwCard(attackerCard);
                            gameField.addCard(attackerCard);
                            break;
                        }
                        //Ветка, если атакующий выбрал подкинуть, но карту нельзя подкинуть
                        else if (number.equals("1")) {
                            actionListener.onReceiveStringTo(attacker.getMyConnection(),
                                    "Вы не можете бросить эту карту");
                            continue;
                        }
                        //Ветка, если атакующий выбрал биту
                        else if (number.equals("2")) {
                            actionListener.onReceiveStringToAll("Бита");
                            //Очистка карты с поля и смена атакующего и защищающегося ролями
                            gameField.clearCardsField();
                            swapAttacker();
                            /*Активация магического числа,
                            которое пропускает ход защищающегося и пропускает последующие ходы*/
                            move = END_ROUND;
                            break;
                        }

                    }
                }
                while (move != END_ROUND) {
                    //Защищающийся выбирает, будет отбиваться или забирать
                    String defenderAnswer = chooseDefenderAction();
                    //Ветка, если защищающийся выбрал отбивать и успешно отбил
                    if (defenderAnswer.equals("1") &&
                            //Второе условие,  которое проверяет отбился или нет защищающийся
                            new Pair(attackerCard, defenderCard = defender.chooseCard()).parryOrNot()) {
                        actionListener.onReceiveStringToAll(defender + " отбивается картой " + defenderCard);
                        //Удаляет карту из рук защищающегося и подкидывает ее на поле
                        defender.throwCard(defenderCard);
                        gameField.addCard(defenderCard);
                        break;
                    }
                    //Ветка если защищающийся выбрал защищаться, но не смог отбиться
                    else if (defenderAnswer.equals("1")) {
                        actionListener.onReceiveStringTo(defender.getMyConnection(),
                                "Вы не можете отбиться этой картой");
                        continue;

                    }
                    //Ветка, если защищающийся выбрал забрать карты
                    else {
                        actionListener.onReceiveStringToAll(defender + " забирает карты");
                        //Забирает все карты с игрового поля
                        defender.grabCards(gameField.getCardsOnTheField());
                        //Очищает поле
                        gameField.clearCardsField();
                        //Активация магическог числа, пропускающего дальнейшие раунды
                        move = END_ROUND;
                        break;
                    }
                }
            }
        }

        /*Возвращает в виде строки ответ атакующего, будет ли он подбрасывать или выбирает биту
          1 - подбрасывает, 2 - бита*/
        private String chooseAttackerAction() {
            actionListener.onReceiveStringTo(attacker.getMyConnection(), "1:Подбрасываю  2:Бита");
            while (true) {
                String number = actionListener.onWaitFromMessageFrom(attacker.getMyConnection());
                if (number.equals("1")) {
                    return "1";
                } else if (number.equals("2")) {
                    return "2";
                } else {
                    actionListener.onReceiveStringTo(attacker.getMyConnection(), "Введите корректную цифру");
                    continue;
                }
            }

        }
        /*Возвращает в виде строки ответ защищающегося, будет ли он защищаться или выбирает забрать карты с поля
          1 - защищается, 2 - забирает*/
        private String chooseDefenderAction() {
            actionListener.onReceiveStringTo(defender.getMyConnection(), "1:Защищаюсь  2:Забираю");
            while (true) {
                String number = actionListener.onWaitFromMessageFrom(defender.getMyConnection());
                if (number.equals("1")) {
                    return "1";
                } else if (number.equals("2")) {
                    return "2";
                } else {
                    actionListener.onReceiveStringTo(defender.getMyConnection(), "Введите корректную цифру");
                    continue;
                }
            }
        }
    }
}


