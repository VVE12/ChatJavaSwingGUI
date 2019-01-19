import NetworkFolder.ConnectionListener;
import NetworkFolder.NetworkTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

//43.Create the main server class.
public class ServerSendingMessage implements ConnectionListener {//54.The server class implements the listener interface.

    //44.Create the main entry point of the server.
    public static void main(String[] args) {
        new ServerSendingMessage();//46.Create the class object, call the constructor.
    }

    //57.Create an ArrayList collection to store a list of TCP connections.
    private final ArrayList<NetworkTCP> arrayList = new ArrayList<>();

    //45.Create a constructor for the server.
    public ServerSendingMessage() {
        System.out.println("Initializing server...");//47.We output a message when the server starts.

        //48.We call the try / catch construct. Create and specify the socket server object in the parameters (indicating the port number), which will listen (process) the dialed code and accept the incoming connection.
        try (ServerSocket serverSocket = new ServerSocket(3000)){

            //50.We create an infinite loop to receive incoming connections. Each time, the server, while listening, creates a new TCP connection.
            while (true) {

                //51.We create a try / catch construct for exceptions when connecting clients.
                try {

                    //53.A new TCP connection is created on every new connection (NetworkTCP).
                    new NetworkTCP(serverSocket.accept(), this);//56.In the TCP parameters, specify the socket object (accepting it) and implement the listener.
                } catch (IOException e) {
                    System.out.println("Connection exception " + e);//52.The message output when throwing an exception with the object of the exception.
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);//49.Calling an unchecked exception.
        }
    }

    //55. Redefine (realize) the listener interface methods in this server class.
    //66.In the methods below via autoSender, we are sending messages.

    @Override
    public synchronized void onConnectionReady(NetworkTCP networkTCP) {
        arrayList.add(networkTCP);//58.Add a TCP connection object from the ArrayList to the method for the connected connection.
        autoSender("Connected! " + networkTCP);//67.We send a message about the connection status, and the connected TCP connection itself.
    }

    @Override
    public synchronized void onReceiveString(NetworkTCP networkTCP, String value) {
        autoSender(value);//69.Send the incoming connection string.
    }

    @Override
    public synchronized void onDisconnect(NetworkTCP networkTCP) {
        arrayList.remove(networkTCP);//59.Remove the TCP connection object from the ArrayList to the method for the broken connection.
        autoSender("Disconnected! " + networkTCP);//68.We output a message about the state of the connection, and the TCP connection itself broken.
    }

    @Override
    public synchronized void onException(NetworkTCP networkTCP, Exception e) {
        System.out.println("Connection exception... " + e);//60.We output a message and an exception object.
    }

    //61.We create a message distribution method.
    private void autoSender(String value) {//62.Specify a specific string in the parameters.
        System.out.println(value);

        //63.We go through the list of connections.
        final int arraySize = arrayList.size();//64.Simplify the ArrayList variable.
        for (int i = 0; i < arraySize; i++) arrayList.get(i).sendString(value);//65.Send everyone a given message.
    }
}
