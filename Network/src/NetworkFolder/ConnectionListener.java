package NetworkFolder;

public interface ConnectionListener {

    void onConnectionReady(NetworkTCP networkTCP);
    void onReceiveString(NetworkTCP networkTCP, String value);
    void onDisconnect(NetworkTCP networkTCP);
    void onException(NetworkTCP networkTCP, Exception e);

}
