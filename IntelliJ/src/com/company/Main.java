//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.company;

import java.security.SecureRandom;

public class Main {

    static private SecureRandom secureRandom;

    public Main() {
    }

    public static void main(String[] args) {

        try {

            String host = "192.168.0.18";
            int port = 4;

            System.out.println("Wait while secure random numbers are initialized....");
            secureRandom = new SecureRandom();
            secureRandom.nextInt();


            Client client = new Client(host, port);

            registerTest(client);

            System.out.println("Done.");
        } catch (Exception var2) {
            System.out.println("ERROR: " + var2);
        }

    }

    public static void loginTest(Client client){
        Login login_session = new Login();

        String username = "Jamie";
        String password = "mkng1A@gwstrgtg";

        Boolean result = login_session.login(client, username, password);

        System.out.println(result);
    }

    public static void registerTest(Client client){
        Login login_session = new Login();

        String username = "Sam";
        String password = "mkyong1A@gwstrgtg";
        String password2 = "mkyong1A@gwstrgtg";

        Boolean[] errors = login_session.register(client, username, password, password2);

        for(Boolean temp : errors){
            System.out.println(temp);
        }

    }

}