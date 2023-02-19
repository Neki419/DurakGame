package game;

import network.TCPConnection;
import network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

//Класс запускает сервер и является обработчиком событий
public class DurakServer implements TCPConnectionListener {

    //Хранит соединения в коллекции
    private final List<TCPConnection> connections = new ArrayList<>();

    private final String ID = "192.168.1.148"; //Мой IP

    public DurakServer() throws InterruptedException {
        System.out.println("Serever running...");
        //Создается серверный сокет, который запускает сервер
        try (ServerSocket serverSocket = new ServerSocket(8189);) {
            try {
                //Ожидает двух соединений
                for (int player = 0; player < 2; player++) {
                    new TCPConnection(this, serverSocket.accept());
                }
                //Отсылает всем игрокам приветствие
                this.onReceiveStringToAll("Добро Пожаловать в игру DURAK ONLINE");
                //Запускает игру с предварительным выбором никнеймов и созданием игроков
                startGame(choosePlayers());
            } catch (IOException e) {
                System.out.println("TCPConnection Exception:" + e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Возвращает список созданных игроков
    private List<Player> choosePlayers() {
        List<Player> playerList = new ArrayList<>();
        TCPConnection firstConnection = connections.get(0);
        TCPConnection secondConnection = connections.get(1);
        this.onReceiveStringTo(firstConnection, "Введите ваше имя");
        this.onReceiveStringTo(secondConnection, "Введите ваше имя");
        playerList.add(new Player(0, this.onWaitFromMessageFrom(firstConnection), firstConnection));
        playerList.add(new Player(0, this.onWaitFromMessageFrom(secondConnection), secondConnection));
        return playerList;
    }

    //Начинает игру с двумя игроками
    private void startGame(List<Player> playerList) {
        new Game(playerList.get(0), playerList.get(1), this);
    }

    //Реагирует на соединение соединения
    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Подключается: " + tcpConnection);
    }

    //Отправляет сообщение данному соединению
    @Override
    public void onReceiveStringTo(TCPConnection tcpConnection, String value) {
        System.out.println(value);
        tcpConnection.sendString(value);
    }

    //Отправляет сообщение всем соединениям
    @Override
    public void onReceiveStringToAll(String value) {
        System.out.println(value);
        sendToAllConnections(value);
    }

    //Ожидает сообщение от данного соединения
    @Override
    public String onWaitFromMessageFrom(TCPConnection tcpConnection) {
        return tcpConnection.sendString();
    }

    //Реакция на отключение соединения
    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Отключился: " + tcpConnection);
    }

    //Реакция на исключение, связанное с соединением
    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection Exception: " + e);
    }

    //Отсылает всем соединениям данное сообщение
    private void sendToAllConnections(String value) {
        System.out.println(value);
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).sendString(value);
        }
    }

}

