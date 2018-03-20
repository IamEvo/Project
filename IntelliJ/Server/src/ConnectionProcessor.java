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
     * Send all the postings we were too late for, so
     * we have the whole history of the session
     */
    private void sendExistingPostings() throws IOException {
        for (Iterator it = server.getPostings(); it.hasNext(); ) {
            Posting posting = (Posting) it.next();
            System.out.println("list " + posting);
            sendPosting(posting);
        }
    }

    /**
     * Deal with an incoming posting
     */
    private void processPosting(Posting posting) throws IOException {
        sendToOtherClients(posting);
        server.addPosting(posting);
    }

    /**
     * Send a posting to all clients but this one
     */
    private void sendToOtherClients(Posting posting) throws IOException {
        for (Iterator it = server.getConnections(); it.hasNext(); ) {

            ConnectionProcessor cp = (ConnectionProcessor) it.next();

            if (cp == this) {
                System.out.println("forward " + this + " " + cp + " (skip)");
                continue;
            }

            System.out.println("forward " + this + " " + cp);
            cp.sendPosting(posting);
        }
    }

    private void sendReply(Posting posting) throws IOException {
        server.getConnections();
    }
    /**
     * Send a new posting to this client
     */
    private void sendPosting(Posting posting) throws IOException {
        posting.write(dout);
    }

    private void sendCommunication(Communication communication) throws IOException {
        communication.write(dout);
    }

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
            din2 = new DataInputStream(in);
            dout = new DataOutputStream(out);

            while (true) {
                DataController sql = new DataController();
                int code = Communication.inputtype(din);
//                Posting posting = Posting.read( din );
//                posting.code(code);
//                int code = din.readInt();
//                System.out.println(code);
//                Posting posting = Posting.read( din );
//                din2 = din;
//                process login or register
//                String temp = din.readUTF();
//                Posting posting = Posting.read( din );
//                Communication communication = Communication.read(din2);
                switch(code) {
                    case 1 : code = 1;{
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
                    case 2 : code = 2;{
                        Posting posting = Posting.read(din);
                        posting.code(code);
                        Boolean[] sql_errors;
                        sql_errors = sql.login(posting.username(), posting.password());
                        Response response = new Response(2, sql_errors[0], sql_errors[1], posting.username());
                        sendResponse(response);
                        if (sql_errors[0] == false || sql_errors[0] == false) {
                            server.removeConnection(this);
                        }else{
                            verified = true;
                        }
                        AuthenticatedConnections authenticatedConnections= new AuthenticatedConnections(socket, posting.username());
                        server.addAuthenticatedUser(authenticatedConnections);
                        break;
                    }
                    case 3 : code = 3;{
                        if(verified == true){
                            ConversationRequest conversationRequest = ConversationRequest.read(din);
                            conversationRequest.setcode(code);
                            ArrayList<AuthenticatedConnections> ConnectedUsers = server.getAuthenticatedConnections();
                            Boolean user_exists = false;
                            for (AuthenticatedConnections temp: ConnectedUsers) {
                                //TODO reading desired person wrong
                                if(conversationRequest.getRecipientUsername().equals(temp.getUsername())){
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
                    case 4 : code = 4;{
                        Message message = Message.read(din);
                        message.setCode(code);
                        String recipient = message.getRecipientusername();
                        ArrayList<AuthenticatedConnections> ConnectedUsers = server.getAuthenticatedConnections();
                        for (AuthenticatedConnections temp : ConnectedUsers) {
                            if(recipient.equals(temp.getUsername())){
                                Socket tempSocket = temp.getSocket();
                                Set<ConnectionProcessor> serverConnections = server.getServerConnections();
                                for (ConnectionProcessor temp2: serverConnections) {
                                    if (temp2.socket.equals(tempSocket)){
                                        temp2.sendMessage(message);
                                    }
                                }
                            }
                        }
//                        Iterator connections = server.getConnections();
//                        ArrayList<AuthenticatedConnections> ConnectedUsers = server.getAuthenticatedConnections();
//                        for (AuthenticatedConnections temp: ConnectedUsers) {
//                        }
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
            while(iterator.hasNext()){
                AuthenticatedConnections temp = iterator.next();

                if(temp.getSocket().equals(this.socket)){
                    iterator.remove();
                }
            }

            System.out.println("Closed connection from socket " + socket);
        }
    }

    public Socket getSocket(){
        return socket;
    }

    /**
     * Send a message to this client
     */
    private void sendMessage(Message message) throws IOException {
        message.write(dout);
    }
}
