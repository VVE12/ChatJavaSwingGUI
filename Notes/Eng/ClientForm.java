import NetworkFolder.ConnectionListener;
import NetworkFolder.NetworkTCP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientForm extends JFrame implements ActionListener, ConnectionListener {//70.The client class is inherited from JFrame and implements ActionListener, ConnectionListener.

    //74. We define several constants.
    private static final String IP = "192.168.44.1";//75.IP Address.
    private static final int PORT = 3000;//76.Port number.
    private static final int WIDTH = 500;//77.Window width.
    private static final int HEIGHT = 500;//78.Window height.

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {//71.Method for working with swing without restrictions when working with streams.
            @Override
            public void run() {
                new ClientForm();
            }
        });
    }

    //83.We set the text fields.
    private final JTextArea jTextArea = new JTextArea();
    private final JTextField jTextFieldOne = new JTextField("User --");
    private final JTextField jTextFieldTwo = new JTextField("sent: ");

    //90.Variable for connection.
    private NetworkTCP networkTCP;

    //72.Working with the client window.
    public ClientForm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//73.Close the window.
        setSize(WIDTH, HEIGHT);//79.We set the window size.
        setLocationRelativeTo(null);//80.The window is always in the middle.
        setAlwaysOnTop(true);//81.The window is always on top.
        jTextArea.setEditable(false);//84.Prohibition on editing.
        jTextArea.setLineWrap(true);//85.Automatic word wrap.
        add(jTextArea, BorderLayout.CENTER);//86. Add 1 text field to the center.
        add(jTextFieldTwo, BorderLayout.SOUTH);//87.Add 2 text field to the south.
        add(jTextFieldOne, BorderLayout.NORTH);//88. Add 3 text field north.
        jTextFieldTwo.addActionListener(this);//89.We call the actionListener interface via a text field to catch the Enter key and send a message.

        setVisible(true);//82.We set the window visibility.

        //91.In the try / catch construct, call the variable to work with the network, specifying IP and PORT.
        try {
            networkTCP = new NetworkTCP(this, IP, PORT);
        } catch (IOException e) {
            messageLog("Connection exception..." + e);//96.Exception exception.
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String stringMessage = jTextFieldTwo.getText();//100.We get our message string.
        if (stringMessage.equals(""))return;//101. If the string is empty, nothing will happen.
        jTextFieldTwo.setText(null);//102.If there is something, erase the line.
        networkTCP.sendString(jTextFieldOne.getText() + " " + stringMessage);//103.Transmit the message, in the parameters, we get the text from the first field + from the second field.
    }

    @Override
    public void onConnectionReady(NetworkTCP networkTCP) {
        messageLog("Connected!");//97.Output ready connection.
    }

    @Override
    public void onReceiveString(NetworkTCP networkTCP, String value) {
        messageLog(value);//99.Output the received string.
    }

    @Override
    public void onDisconnect(NetworkTCP networkTCP) {
        messageLog("Disconnected");//98.Output break connection.
    }

    @Override
    public void onException(NetworkTCP networkTCP, Exception e) {
        messageLog("Connection exception..." + e);//95.Exception exception.
    }

    //92.Methods for logging, displaying messages in a text field.
    private synchronized void messageLog(String stringLog) {//93.Method for working with swing without restrictions when working with streams.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jTextArea.append(stringLog + "\n");
                jTextArea.setCaretPosition(jTextArea.getDocument().getLength());//94.Autoscrolling.
            }
        });
    }
}
