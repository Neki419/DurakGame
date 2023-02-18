/*package com.network;

import java.util.ArrayList;
import java.util.List;

public class DurakListener implements TCPConnectionListener {
    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Подключается: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveStringTo(TCPConnection tcpConnection, String value) {
        System.out.println(value);
        tcpConnection.sendString(value);
    }

    @Override
    public synchronized void onReceiveStringToAll(String value) {
        System.out.println(value);
        sendToAllConnections(value);

    }

    @Override
    public synchronized String onWaitFromMessageFrom(TCPConnection tcpConnection) {
        return tcpConnection.sendString();
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Отключился: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection Exception: " + e);
    }
    private void sendToAllConnections(String value){
        System.out.println(value);
        for(int i = 0; i < connections.size(); i++){
            connections.get(i).sendString(value);
        }
    }
}*/
