package Interfaces;

import java.net.Socket;

public interface AuthenticatedConnections {

    public Socket getSocket();

    public void setSocket(Socket socket);

    public String getUsername();

    public void setUsername(String username);

    public String toString();

}
