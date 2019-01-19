package ServerFolder;

import NetworkFolder.ConnectionListener;
import NetworkFolder.NetworkTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerSendingMessage implements ConnectionListener {

    
    public static void main(String[] args) {
        new ServerSendingMessage();
    }

    private final ArrayList<NetworkTCP> arrayList = new ArrayList<>();

    public ServerSendingMessage() {
        System.out.println("Initializing server...");

        try (ServerSocket serverSocket = new ServerSocket(3000)){

            while (true) {

                try {

                    new NetworkTCP(serverSocket.accept(), this);
                } catch (IOException e) {
                    System.out.println("Connection exception " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(NetworkTCP networkTCP) {
        arrayList.add(networkTCP);
        autoSender("Connected! " + networkTCP);
    }

    @Override
    public synchronized void onReceiveString(NetworkTCP networkTCP, String value) {
        autoSender(value);
    }

    @Override
    public synchronized void onDisconnect(NetworkTCP networkTCP) {
        arrayList.remove(networkTCP);
        autoSender("Disconnected! " + networkTCP);
    }

    @Override
    public synchronized void onException(NetworkTCP networkTCP, Exception e) {
        System.out.println("Connection exception... " + e);
    }

    private void autoSender(String value) {
        System.out.println(value);

        final int arraySize = arrayList.size();
        for (int i = 0; i < arraySize; i++) arrayList.get(i).sendString(value);
    }
}
