package ClientFolder;

import NetworkFolder.ConnectionListener;
import NetworkFolder.NetworkTCP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientForm extends JFrame implements ActionListener, ConnectionListener {//70.Класс клиента наследуется от JFrame и имплементирует ActionListener, ConnectionListener.

    //74.Определим несколько констант.
    private static final String IP = "192.168.44.1";//75.IP Адрес.
    private static final int PORT = 3000;//76.Номер порта.
    private static final int WIDTH = 500;//77.Ширина окна.
    private static final int HEIGHT = 500;//78.Длина окна.

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {//71.Метод для работы со свингом без ограничейний при работе с потоками.
            @Override
            public void run() {
                new ClientForm();
            }
        });
    }

    //83.Задаём текстовые поля.
    private final JTextArea jTextArea = new JTextArea();
    private final JTextField jTextFieldOne = new JTextField("User --");
    private final JTextField jTextFieldTwo = new JTextField("sent: ");

    //90.Переменная для соединения.
    private NetworkTCP networkTCP;

    //72.Работа с клиентским окном.
    public ClientForm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//73.Закрытие окна.
        setSize(WIDTH, HEIGHT);//79.Задаём размер окна.
        setLocationRelativeTo(null);//80.Окно всегда посередине.
        setAlwaysOnTop(true);//81.Окно всегда сверху.
        jTextArea.setEditable(false);//84.Запрет на редактирование.
        jTextArea.setLineWrap(true);//85.Автоматический перенос слов.
        add(jTextArea, BorderLayout.CENTER);//86.Добавляем 1 текстовое поле в центр.
        add(jTextFieldTwo, BorderLayout.SOUTH);//87.Добавляем 2 текстовое поле на юг.
        add(jTextFieldOne, BorderLayout.NORTH);//88.Добавляем 3 текстовое поле на север.
        jTextFieldTwo.addActionListener(this);//89.Вызываем интерфейс actionListener через текстовое поле, для отловки клавиши Enter и отправки сообщения.

        setVisible(true);//82.Задаём видимость окна.

        //91.В конструкции try/catch вызываем переменную для работы с сетью, указывая IP и PORT.
        try {
            networkTCP = new NetworkTCP(this, IP, PORT);
        } catch (IOException e) {
            messageLog("Connection exception..." + e);//96.Вывод исключения.
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String stringMessage = jTextFieldTwo.getText();//100.Получаем нашу строку с сообщением.
        if (stringMessage.equals(""))return;//101.Если строка пустая, то ничего не произойдёт.
        jTextFieldTwo.setText(null);//102.Если что-то есть стираем строку.
        networkTCP.sendString(jTextFieldOne.getText() + " " + stringMessage);//103.Передаём сообщение, в параметрах, получаем текст из первого поля + из второго поля.
    }

    @Override
    public void onConnectionReady(NetworkTCP networkTCP) {
        messageLog("Connected!");//97.Вывод готового соединения.
    }

    @Override
    public void onReceiveString(NetworkTCP networkTCP, String value) {
        messageLog(value);//99.Вывод принятой строки.
    }

    @Override
    public void onDisconnect(NetworkTCP networkTCP) {
        messageLog("Disconnected");//98.Вывод разрыва соединения.
    }

    @Override
    public void onException(NetworkTCP networkTCP, Exception e) {
        messageLog("Connection exception..." + e);//95.Вывод исключения.
    }

    //92.Методя для логирования, вывода сообщений в текстовое поле.
    private synchronized void messageLog(String stringLog) {//93.Метод для работы со свингом без ограничейний при работе с потоками.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jTextArea.append(stringLog + "\n");
                jTextArea.setCaretPosition(jTextArea.getDocument().getLength());//94.Автоскроллинг.
            }
        });
    }
}
