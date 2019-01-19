//14.We create an interface for working with events (we set options for behavior), for listening in the incoming stream.
public interface ConnectionListener {

    //15.We create methods for working with connections, passing the NetworkTCP class entity as a parameter.
    void onConnectionReady(NetworkTCP networkTCP);//16.We create a method for the connected connection.
    void onReceiveString(NetworkTCP networkTCP, String value);//17.We create a method for accepting a string, except for the NetworkTCP entity, add a String value if we want to see the specifically accepted string.
    void onDisconnect(NetworkTCP networkTCP);//18.We create a method for a broken connection.
    void onException(NetworkTCP networkTCP, Exception e);//19.We create a method for exceptions caught in connections.

}