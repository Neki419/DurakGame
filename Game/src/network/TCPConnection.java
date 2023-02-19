package network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

//Класс, представляющий экзмепляр соединения
public class TCPConnection {

    private final Socket socket;
    private final TCPConnectionListener eventListener; //Обработчик событий, который реагирует на действия данного соединения
    private final BufferedReader in; //Поток данных ИЗ соединения
    private final BufferedWriter out; //Поток данных в соединение

    //Создание соединения
    public TCPConnection(TCPConnectionListener eventListener, String IPAddress, int port) throws IOException {
        this(eventListener, new Socket(IPAddress, port));
    }

    //Создание соединения
    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),
                Charset.forName("UTF-8")));
        eventListener.onConnectionReady(this);
    }

    //Отправляет данное на экран пользователя, который представляет данное соединение
    public void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    //Читает сообщение от данного пользователя
    public String sendString() {
        try {
            return in.readLine();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
        return "";
    }

    //Закрывает соединение
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    //Возвращает строку, представляющую некую информацию о соединении
    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
