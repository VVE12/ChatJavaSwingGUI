package ServerFolder;

import NetworkFolder.ConnectionListener;
import NetworkFolder.NetworkTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

//43.Создаём основной класс сервера.
public class ServerSendingMessage implements ConnectionListener {//54.Класс сервера реализует интерфейс слушателя.

    //44.Создаём основную точку входа сервера.
    public static void main(String[] args) {
        new ServerSendingMessage();//46.Создаём объект класса, вызываем конструктор.
    }

    //57.Создаём коллекцию ArrayList для хранения списка из TCP соединений.
    private final ArrayList<NetworkTCP> arrayList = new ArrayList<>();

    //45.Создаём конструктор для сервера.
    public ServerSendingMessage() {
        System.out.println("Initializing server...");//47.Выводим сообщение при запуске сервара.

        //48.Вызываем конструкцию try/catch. Создаём и указываем в параметрах объект сервер сокета(указывая номер порта), который будет слушать(обрабатывать) набранный код и принимать входящее соединение.
        try (ServerSocket serverSocket = new ServerSocket(3000)){

            //50.Создаём бесконечный цикл, для приёма входящих соединений. Каждый раз, сервер, слушая, создаёт новое TCP соединение.
            while (true) {

                //51.Создаём конструкцию try/catch для исключений при подключении клиентов.
                try {

                    //53.На каждое новое соединение создаётся новый TCP(NetworkTCP).
                    new NetworkTCP(serverSocket.accept(), this);//56.В параметрах TCP, указываем объект сокета(принимая его) и реализуем слушателя.
                } catch (IOException e) {
                    System.out.println("Connection exception " + e);//52.Вывод сообщения при выбросе исключения с объектом исключения.
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);//49.Вызов непроверенного исключения.
        }
    }

    //55.Переопределяем(реализуем) методы интерфеса слушателя в данном классе сервера.
    //66.В методах ниже через autoSender, рассылаем сообщения.

    @Override
    public synchronized void onConnectionReady(NetworkTCP networkTCP) {
        arrayList.add(networkTCP);//58.Добавляем объект TCP соединения из списка ArrayList в метод для подключённого соединения.
        autoSender("Connected! " + networkTCP);//67.Выводим сообщение о состоянии соединения, и само, подключённое TCP соединение.
    }

    @Override
    public synchronized void onReceiveString(NetworkTCP networkTCP, String value) {
        autoSender(value);//69.Отправляем входящую строку соединения.
    }

    @Override
    public synchronized void onDisconnect(NetworkTCP networkTCP) {
        arrayList.remove(networkTCP);//59.Удаляем объект TCP соединения из списка ArrayList в метод для разорванного соединения.
        autoSender("Disconnected! " + networkTCP);//68.Выводим сообщение о состоянии соединения, и само, разорванное TCP соединение.
    }

    @Override
    public synchronized void onException(NetworkTCP networkTCP, Exception e) {
        System.out.println("Connection exception... " + e);//60.Выводим сообщение и объект исключения.
    }

    //61.Создаём метод рассылки сообщения.
    private void autoSender(String value) {//62.Указываем в параметрах конкретную строку.
        System.out.println(value);

        //63.Проходим по списку соединений.
        final int arraySize = arrayList.size();//64.Упрощяем вид переменной ArrayList.
        for (int i = 0; i < arraySize; i++) arrayList.get(i).sendString(value);//65.Отправляем каждому данное сообщение.
    }
}
