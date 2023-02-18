package com.network;

//Контракт, показывающий, какие методы должен реализовать слушатель соединения
/*Слушаетель соединения - это сущность,
которая определнным образом реагирует на действия соединения(в нашем случае на действия игроков)*/
public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection); //Реагирует на соединение соединения
    void onReceiveStringTo(TCPConnection tcpConnection, String value); //Отправляет сообщение данному соединению
    void onReceiveStringToAll(String value); //Отправляет сообщение всем соединениям
    String onWaitFromMessageFrom(TCPConnection tcpConnection); //Ожидает сообщение от данного соединения
    void onDisconnect(TCPConnection tcpConnection); //Реакция на отключение соединения
    void onException(TCPConnection tcpConnection, Exception e); //Реакция на исключение, связанное с соединением

}
