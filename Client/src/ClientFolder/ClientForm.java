package ClientFolder;

import NetworkFolder.ConnectionListener;
import NetworkFolder.NetworkTCP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientForm extends JFrame implements ActionListener, ConnectionListener {

    private static final String IP = "192.168.44.1";
    private static final int PORT = 3000;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientForm();
            }
        });
    }

    private final JTextArea jTextArea = new JTextArea();
    private final JTextField jTextFieldOne = new JTextField("User --");
    private final JTextField jTextFieldTwo = new JTextField("sent: ");

    private NetworkTCP networkTCP;

    public ClientForm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);
        add(jTextArea, BorderLayout.CENTER);
        add(jTextFieldTwo, BorderLayout.SOUTH);
        add(jTextFieldOne, BorderLayout.NORTH);
        jTextFieldTwo.addActionListener(this);

        setVisible(true);

        try {
            networkTCP = new NetworkTCP(this, IP, PORT);
        } catch (IOException e) {
            messageLog("Connection exception..." + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String stringMessage = jTextFieldTwo.getText();
        if (stringMessage.equals(""))return;
        jTextFieldTwo.setText(null);
        networkTCP.sendString(jTextFieldOne.getText() + " " + stringMessage);
    }

    @Override
    public void onConnectionReady(NetworkTCP networkTCP) {
        messageLog("Connected!");
    }

    @Override
    public void onReceiveString(NetworkTCP networkTCP, String value) {
        messageLog(value);
    }

    @Override
    public void onDisconnect(NetworkTCP networkTCP) {
        messageLog("Disconnected");
    }

    @Override
    public void onException(NetworkTCP networkTCP, Exception e) {
        messageLog("Connection exception..." + e);
    }

    private synchronized void messageLog(String stringLog) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jTextArea.append(stringLog + "\n");
                jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
            }
        });
    }
}
