package NetworkFolder;

//14.Создаём интерфейс для работы с событиями (задаём варианты поведений), для прослушивания во входящем потоке.
public interface ConnectionListener {

    //15.Создаём методы для работы с соединением, передавая в качестве параметра сущность класса NetworkTCP.
    void onConnectionReady(NetworkTCP networkTCP);//16.Создаём метод для подключённого соединения.
    void onReceiveString(NetworkTCP networkTCP, String value);//17.Создаём метод для принятия строки, кроме сущности NetworkTCP, добавляем String value если хотим увидеть конкретно принятую строку.
    void onDisconnect(NetworkTCP networkTCP);//18.Создаём метод для разорванного соединения.
    void onException(NetworkTCP networkTCP, Exception e);//19.Создаём метод для исключений, отловленных в соединениях.

}