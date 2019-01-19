import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

//1.Create the main class for the connection.
public class NetworkTCP {

    //2.Create the main variables of the class instance.
    private final Socket socket;//3.Main socket for connection setup.
    private final Thread thread;//4.The main stream for listening to the incoming message (input stream).
    private final BufferedReader bufferedReader;//5.The main variable that reads text from the incoming stream.
    private final BufferedWriter bufferedWriter;//6.The main variable that writes text to the stream.
    private final ConnectionListener connectionListener;//20.Main variable, event listener.

    //41.We create a second constructor for the connection, creating a socket object inside (by the parameters of the IP address and port number).
    public NetworkTCP(ConnectionListener connectionListener, String IP, int PORT) throws IOException {
        this(new Socket(IP, PORT), connectionListener);//42.We call from one constructor to another. We specify IP and PORT through the socket, the second parameter is the listener.
    }

    //7.We create a constructor for the connection, which takes a ready-made socket object to create a connection.
    public NetworkTCP(Socket socket, ConnectionListener connectionListener) throws IOException {
        this.socket = socket;//8.We set the socket in the constructor.
        this.connectionListener = connectionListener;//21.We set the listener in the constructor.

        //9.We set input (getInputStream ()) / output (getOutputStream ()) streams through the read objects (bufferedReader) and text writes (bufferedWriter), and also specify the UTF-8 encoding.
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

        //10.Create a new stream to listen to all the incoming information.
        thread = new Thread(new Runnable() {//12.We pass an instance of the class that implements the Runnable interface, creating an anonymous class.
            @Override//13.We override the Run method to listen for incoming connections in it.
            public void run() {

                //22.In the try / catch construct, we catch the sources.
                try {
                    connectionListener.onConnectionReady(NetworkTCP.this);//23.Through the listener variable, we call the method of the connected connection, calling this connection object in the parameters.

                    //24.We set the infinite while loop (until the thread is interrupted), for the constant receipt of rows.
                    while (!thread.isInterrupted()) {
                        connectionListener.onReceiveString(NetworkTCP.this, bufferedReader.readLine());//25.Through the listener variable, we call the string acceptance method. We specify this connection object in the parameters and through the read variable of the string, we derive it.
                    }
                } catch (IOException e) {
                    connectionListener.onException(NetworkTCP.this, e);//37.Through the listener variable, we derive the method for string exceptions, in the parameters we indicate this object and the exception object.
                } finally {
                    connectionListener.onDisconnect(NetworkTCP.this);//38.Through the listener variable, we deduce the method for breaking the connection, specify this object in the parameters.
                }
            }
        });
        thread.start();//11.Start the stream.
    }

    //26.We create a method for sending a message, in the parameters we specify the line we want to send. To safely access methods from different threads, we set the method synchronization.
    public synchronized void sendString(String value) {

        //32.In the try / catch construct, we output the recorded data.
        try {
            bufferedWriter.write(value + "\r\n");//33.Through the record variable, we display the value of the string, line by line.
            bufferedWriter.flush();//36.Through the flush () method, clear the data from the clipboard and send them.
        } catch (IOException e) {
            connectionListener.onException(NetworkTCP.this, e);//34.Through the listener variable, we derive the method for exceptions of the string, in the parameters we indicate this object and the exception object.
            disconnect();//35.We deduce the disconnect method.
        }
    }

    //27.We create a method for breaking the connection.
    public synchronized void disconnect() {
        thread.isInterrupted();//28.Calling the thread and interrupting it.

        //29.In the try / catch construct, close the socket and catch the exception.
        try {
            socket.close();//30.We close the socket.
        } catch (IOException e) {
            connectionListener.onException(NetworkTCP.this, e);//31.By catching an exception, through the listener we call methods for exceptions, in the parameters we specify this object and the object of exception.
        }
    }

    //39.To represent the object in the text version and display the information in the log, override the toString () method.
    @Override
    public String toString() {
        return "............ " + socket.getInetAddress() + "|" + socket.getPort();//40.We output the string + through the socket and specify the address from which the connection is established + port number.
    }
}
