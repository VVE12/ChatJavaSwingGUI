package NetworkFolder;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

//1.Создаём основной класс для соединения.
public class NetworkTCP {

    //2.Создаём основные переменные экземпляра класса.
    private final Socket socket;//3.Основной сокет для установки соединения.
    private final Thread thread;//4.Основной поток для прослушивания входящего сообщения (потока ввода).
    private final BufferedReader bufferedReader;//5.Основная переменная, считывающая текст из входящего потока.
    private final BufferedWriter bufferedWriter;//6.Основная переменная, записывающая текст в поток.
    private final ConnectionListener connectionListener;//20.Основная переменная, слушателя событий.

    //41.Создаём второй конструктор для соединения, создавая объект сокета внутри (по параметрам IP адреса и номера порта).
    public NetworkTCP(ConnectionListener connectionListener, String IP, int PORT) throws IOException {
        this(new Socket(IP, PORT), connectionListener);//42.Вызываем из одного конструктора другой. Через сокет указываем IP и PORT, второй параметр - слушатель.
    }

    //7.Создаём конструктор для соединения, принимающий готовый объект сокета для создания соединения.
    public NetworkTCP(Socket socket, ConnectionListener connectionListener) throws IOException {
        this.socket = socket;//8.Задаём сокет в конструкторе.
        this.connectionListener = connectionListener;//21.Задаём слушателя в конструкторе.

        //9. Задаём потоки ввода(getInputStream())/вывода(getOutputStream()) через объекты считывания(bufferedReader) и записи(bufferedWriter) текста, также указываем кодировку UTF-8.
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

        //10.Создаём новый поток для прослушивания всей входящей информации.
        thread = new Thread(new Runnable() {//12.Передаём экземпляр класса, реализующий интерфейс Runnable, создавая анонимный класс.
            @Override//13.Переопределяем метод Run, для прослушивания в нём входящих соединений.
            public void run() {

                //22.В конструкции try/catch отлавливаем искоючения.
                try {
                    connectionListener.onConnectionReady(NetworkTCP.this);//23.Через переменную слушателя, вызываем метод подключённого соединения, вызывая в параметрах этот объект соединения.

                    //24.Задаём бесконечный цикл while(пока поток не прерван), для постоянного получения строк.
                    while (!thread.isInterrupted()) {
                        connectionListener.onReceiveString(NetworkTCP.this, bufferedReader.readLine());//25.Через переменную слушателя, вызываем метод принятия строки. Указываем в параметрах этот объект соединения и через переменную считывания строки, выводим её.
                    }
                } catch (IOException e) {
                    connectionListener.onException(NetworkTCP.this, e);//37.Через переменную слушателя, выводим метод для исключений строки, в параметрах указываем этот объект и объект исключения.
                } finally {
                    connectionListener.onDisconnect(NetworkTCP.this);//38.Через переменную слушателя, выводим метод для разрыва соединения, в параметрах указываем этот объект.
                }
            }
        });
        thread.start();//11.Запускаем поток.
    }

    //26.Создаём метод для отправки сообщения, в параметрах указываем строчку, которую хотим отправить. Для безопасного обращения к методам из разных потоков, задаём синхронизацию метода.
    public synchronized void sendString(String value) {

        //32.В конструкции try/catch выводим записанные данные.
        try {
            bufferedWriter.write(value + "\r\n");//33.Через переменную записи выводим значение строки, построчно.
            bufferedWriter.flush();//36.Через метод flush(), очищаем данные из буфера обмена и отправляем их.
        } catch (IOException e) {
            connectionListener.onException(NetworkTCP.this, e);//34.Через переменную слушателя, выводим метод для исключений строки, в параметрах указываем этот объект и объект исключения.
            disconnect();//35.Выводим метод разрыва соединения.
        }
    }

    //27.Создаём метод для разрыва соединения.
    public synchronized void disconnect() {
        thread.isInterrupted();//28.Вызываем поток и прерываем его.

        //29.В конструкции try/catch закрываем сокет и отлавливаем исключение.
        try {
            socket.close();//30.Закрываем сокет.
        } catch (IOException e) {
            connectionListener.onException(NetworkTCP.this, e);//31.Отловив исключение, через слушателя вызываем методя для исключений, в параметрах указываем этот объект и объект исключения.
        }
    }

    //39.Для представления объекта в текстовом варианте и вывода информации в log, переопределяем метод toString().
    @Override
    public String toString() {
        return "............ " + socket.getInetAddress() + "|" + socket.getPort();//40.Выводим строку + через сокет указываем адрес с которого установлено соединение + номер порта.
    }
}
