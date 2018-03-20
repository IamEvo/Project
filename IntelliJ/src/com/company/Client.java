package com.company;

import javax.net.ssl.*;
import javax.swing.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Client extends JFrame implements Runnable {
    /**
     * Connection to the client
     */
    private DataInputStream din;

    /**
     * Connection to the client
     */
    private DataOutputStream dout;

    /**
     * KeyStore for storing our public/private key pair
     */
    private KeyStore clientKeyStore;

    /**
     * KeyStore for storing the server's public key
     */
    private KeyStore serverKeyStore;

    /**
     * Used to generate a SocketFactory
     */
    private SSLContext sslContext;

    /**
     * A list of visible postings
     */
    private Set postings = new HashSet();

    /**
     * Passphrase for accessing our authentication keystore
     */
    static private final String passphrase = "clientpw";

    /**
     * A source of secure random numbers
     */
    static private SecureRandom secureRandom;

    public Client(String host, int port) {

        secureRandom = new SecureRandom();
        secureRandom.nextInt();

        connect(host, port);
        new Thread(this).start();

    }

    private void setupServerKeystore() throws GeneralSecurityException, IOException {
        serverKeyStore = KeyStore.getInstance("JKS");
        serverKeyStore.load(new FileInputStream("C:\\Users\\40091207\\Desktop\\SSL\\server.public"),
                "public".toCharArray());
    }

    private void setupClientKeyStore() throws GeneralSecurityException, IOException {
        clientKeyStore = KeyStore.getInstance("JKS");
        clientKeyStore.load(new FileInputStream("C:\\Users\\40091207\\Desktop\\SSL\\client.private"),
                passphrase.toCharArray());
    }

    private void setupSSLContext() throws GeneralSecurityException, IOException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(serverKeyStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(clientKeyStore, passphrase.toCharArray());

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(),
                tmf.getTrustManagers(),
                secureRandom);
    }

    private void connect(String host, int port) {
        try {
            setupServerKeystore();
            setupClientKeyStore();
            setupSSLContext();

            SSLSocketFactory sf = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) sf.createSocket(host, port);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            din = new DataInputStream(in);
            dout = new DataOutputStream(out);

        } catch (GeneralSecurityException gse) {
            gse.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                int code = Communication.inputtype(din);

//                if (code == 1){
//                    Response  response= Response.read(din);
//                    response.setcode(code);
//                    System.out.println(response.toString());
//                }else if (code == 2){
//                    Response  response= Response.read(din);
//                    response.setcode(code);
//                    System.out.println(response.toString());
//                }

                switch(code) {
                    case 1 : code = 1;{
                        Response  response= Response.read(din);
                        response.setcode(code);
                        System.out.println(response.toString());
                        break;
                    }
                    case 2 : code = 2;{
                        Response  response= Response.read(din);
                        response.setcode(code);
                        System.out.println(response.toString());
                        break;
                    }
                    case 3 : code = 3;{
                        Response  response = Response.read(din);
                        response.setcode(code);
                        String hostAndPort = response.username();
                        System.out.println(response.username());
                        String host = hostAndPort.substring(0, hostAndPort.indexOf(':'));
                        int port = Integer.parseInt(hostAndPort.substring(hostAndPort.indexOf(':') + 1));
                        System.out.println(response.toString());
                        Client client = new Client(host , port);
                        break;
                    }
                    case 4 : code = 4;{
                        Message message = Message.read(din);
                        message.setCode(code);
                        System.out.println(message.toString());
                        break;
                    }
                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void newPosting(Posting posting) {
        postings.add(posting);
        try {
            posting.write(dout);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void sendMessage(Message message){
        try{
            message.write(dout);
        }catch(IOException ie){
            ie.printStackTrace();
        }
    }

    public void newConversationRequest(ConversationRequest conversationRequest) {
        try {
            conversationRequest.write(dout);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }


}
