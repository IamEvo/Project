import Protocols.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ConnectionProcessor implements Runnable {
    /**
     * The Server we are running inside
     */
    private Server server;

    /**
     * The socket connected to our client
     */
    private Socket socket;

    private Boolean verified;
    /**
     * Connection to the client
     */
    private DataInputStream din;
    private DataInputStream din2;

    /**
     * Connection to the client
     */
    private DataOutputStream dout;

    /**
     * Create a new ConnectionProcessor
     */
    public ConnectionProcessor(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        new Thread(this).start();
    }

    /**
     * Send a new posting to this client
     */
    private void sendPosting(Posting posting) throws IOException {
        posting.write(dout);
    }

    /**
     * Send a new Response to this client
     */
    private void sendResponse(Response response) throws IOException {
        response.write(dout);
    }

    /**
     * Background thread: read new text strings from the client,
     * and forward them to all other clients
     */
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            din = new DataInputStream(in);
            dout = new DataOutputStream(out);

            while (true) {

                int code = Communication.inputtype(din);

                switch (code) {
                    case 1:
                        code = 1;
                    {
                        DataController sql = new DataController();
                        Posting posting = Posting.read(din);
                        posting.code(code);
                        Boolean[] sql_errors;
                        sql_errors = sql.register(posting.username(), posting.password());
                        Response response = new Response(1, sql_errors[0], sql_errors[1], posting.username());
                        sendResponse(response);
                        if (sql_errors[0] == false || sql_errors[0] == false) {
                            server.removeConnection(this);
                        } else {
                            verified = true;
                        }
                        break;
                    }
                    case 2:
                        code = 2;
                    {
                        DataController sql = new DataController();
                        Posting posting = Posting.read(din);
                        posting.code(code);
                        Boolean[] sql_errors;
                        sql_errors = sql.login(posting.username(), posting.password());
                        Response response = new Response(2, sql_errors[0], sql_errors[1], posting.username());
                        sendResponse(response);
                        if (sql_errors[0] == false || sql_errors[0] == false) {
                            server.removeConnection(this);
                        } else {
                            verified = true;
                        }
                        AuthenticatedConnections authenticatedConnections = new AuthenticatedConnections(socket, posting.username());
                        server.addAuthenticatedUser(authenticatedConnections);
                        break;
                    }
                    case 3:
                        code = 3;
                    {
                        if (verified == true) {
                            ConversationRequest conversationRequest = ConversationRequest.read(din);
                            conversationRequest.setcode(code);
                            ArrayList<AuthenticatedConnections> ConnectedUsers = server.getAuthenticatedConnections();
                            Boolean user_exists = false;
                            for (AuthenticatedConnections temp : ConnectedUsers) {
                                //TODO reading desired person wrong
                                if (conversationRequest.getRecipientUsername().equals(temp.getUsername())) {
                                    System.out.println("temp matches");
                                    Socket tempsocket = temp.getSocket();
                                    String hostAndPort = tempsocket.getInetAddress().getHostAddress();
                                    hostAndPort = hostAndPort + ":" + tempsocket.getLocalAddress();
                                    Response response = new Response(3, true, true, hostAndPort);
                                    sendResponse(response);
                                    user_exists = true;
                                }
                            }
                        }
                        break;
                    }
                    case 4:
                        code = 4;
                    {
                        AuthConfirmationRequest authConfirmationRequest = Protocols.AuthConfirmationRequest.read(din);
                        authConfirmationRequest.setCode(code);

                        AuthConfirmationRequestResponse(authConfirmationRequest);
                        break;
                    }
                    case 5:
                        code = 5;
                    {
                        Message message = Protocols.Message.read(din);
                        message.setCode(5);
                        sendMessage(message);
                        break;
                    }
                }
                server.getConnections();
            }
        } catch (IOException ie) {
            try {
                socket.close();
            } catch (IOException ie2) {
                System.out.println("Error closing socket " + socket);
            }

            server.removeConnection(this);
            ArrayList<AuthenticatedConnections> ConnectedUsers = server.getAuthenticatedConnections();
            Iterator<AuthenticatedConnections> iterator = ConnectedUsers.iterator();
            while (iterator.hasNext()) {
                AuthenticatedConnections temp = iterator.next();

                if (temp.getSocket().equals(this.socket)) {
                    iterator.remove();
                }
            }

            System.out.println("Closed connection from socket " + socket);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Send a message to this client
     */
    private void sendMessage(Message message) throws IOException {

        String recipient = message.getRecipientusername();
        ArrayList<AuthenticatedConnections> ConnectedUsers = server.getAuthenticatedConnections();
        for (AuthenticatedConnections temp : ConnectedUsers) {
            if (recipient.equals(temp.getUsername())) {
                Socket tempSocket = temp.getSocket();
                Set<ConnectionProcessor> serverConnections = server.getServerConnections();
                for (ConnectionProcessor temp2 : serverConnections) {
                    if (temp2.socket.equals(tempSocket)) {
                        temp2.sendMessage(message);
                    }
                }
            }
        }

        message.write(dout);
    }

    /**
     * Process the authentication check and respond
     */
    private void AuthConfirmationRequestResponse(AuthConfirmationRequest authConfirmationRequest) {
        String username = authConfirmationRequest.getUsername();

        ArrayList<AuthenticatedConnections> ConnectedUsers = server.getAuthenticatedConnections();
        for (AuthenticatedConnections temp : ConnectedUsers) {
            if (username.equals(temp.getUsername()) && this.socket.equals(temp.getSocket())) {
                authConfirmationRequest.setResult(true);
                break;
            } else {
                authConfirmationRequest.setResult(false);
            }
        }
        try {
            authConfirmationRequest.write(dout);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    /**
     * Confirm the user is authenticated
     */
    private Boolean verifyUserAuthenticated(String username) {
        Boolean result = false;
        ArrayList<AuthenticatedConnections> ConnectedUsers = server.getAuthenticatedConnections();
        for (AuthenticatedConnections temp : ConnectedUsers) {
            if (username.equals(temp.getUsername()) | this.socket.equals(temp.getSocket())) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }
}
