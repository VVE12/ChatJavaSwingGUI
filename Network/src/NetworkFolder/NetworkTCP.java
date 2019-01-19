package NetworkFolder;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class NetworkTCP {

    private final Socket socket;
    private final Thread thread;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final ConnectionListener connectionListener;

    public NetworkTCP(ConnectionListener connectionListener, String IP, int PORT) throws IOException {
        this(new Socket(IP, PORT), connectionListener);
    }

    public NetworkTCP(Socket socket, ConnectionListener connectionListener) throws IOException {
        this.socket = socket;
        this.connectionListener = connectionListener;

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    connectionListener.onConnectionReady(NetworkTCP.this);

                    while (!thread.isInterrupted()) {
                        connectionListener.onReceiveString(NetworkTCP.this, bufferedReader.readLine());
                    }
                } catch (IOException e) {
                    connectionListener.onException(NetworkTCP.this, e);
                } finally {
                    connectionListener.onDisconnect(NetworkTCP.this);
                }
            }
        });
        thread.start();
    }

    public synchronized void sendString(String value) {

        try {
            bufferedWriter.write(value + "\r\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            connectionListener.onException(NetworkTCP.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        thread.isInterrupted();

        try {
            socket.close();
        } catch (IOException e) {
            connectionListener.onException(NetworkTCP.this, e);
        }
    }

    @Override
    public String toString() {
        return "............ " + socket.getInetAddress() + "|" + socket.getPort();
    }
}
