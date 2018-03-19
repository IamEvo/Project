import java.net.Socket;

public class AuthenticatedConnections implements Interfaces.AuthenticatedConnections {

    private Socket socket;
    private String username;

    public AuthenticatedConnections(Socket socket, String username){
        this.socket = socket;
        this.username = username;
    }

    @Override
    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

}
