//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.company;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

public class Main {

    static private SecureRandom secureRandom;

    public Main() {
    }

    public static void main(String[] args) {

        try {
            int port = 4;
            String host = "192.168.0.18";
            System.out.println("Wait while secure random numbers are initialized....");
            secureRandom = new SecureRandom();
            secureRandom.nextInt();
            Client client = new Client(host, port);

            loginTest(client);

            TimeUnit.SECONDS.sleep(5);

            Message message = new Message(4, "Jamie", "Sam", "Hello server");
            client.sendMessage(message);

//            String host = "192.168.0.18";
//            System.out.println("Wait while secure random numbers are initialized....");
//            secureRandom = new SecureRandom();
//            secureRandom.nextInt();
//            Client client = new Client(host, port);
//
//            loginTest(client);
//
//            TimeUnit.SECONDS.sleep(5);
//            ConversationRequest conversationRequest = new ConversationRequest(3, "Sam", "Jamie");
//            client.newConversationRequest(conversationRequest);

            System.out.println("Done.");
        } catch (Exception var2) {
            System.out.println("ERROR: " + var2);
        }

    }

    public static void loginTest(Client client){
        Login login_session = new Login();

        String username = "Jamie";
        String password = "mkyong1A@gwstrgtg";

        Boolean result = login_session.login(client, username, password);

    }

    public static void registerTest(Client client){
        Login login_session = new Login();

        String username = "Jamie";
        String password = "mkyong1A@gwstrgtg";
        String password2 = "mkyong1A@gwstrgtg";

        Boolean[] errors = login_session.register(client, username, password, password2);

        for(Boolean temp : errors){
            System.out.println(temp);
        }

    }

}
