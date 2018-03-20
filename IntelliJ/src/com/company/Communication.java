package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Communication implements Interfaces.Communication{


    private int code;
    private String username;

    public Communication() {
    }

    public Communication(int code, String username) {
        this.code = code;
        this.username = username;
    }

    static Communication read(DataInputStream din) throws IOException {
        Communication communication = new Communication();
        communication.code = din.readInt();
        communication.username = din.readUTF();

        System.out.println("read " + communication);
        return communication;
    }

    public void write(DataOutputStream dout) throws IOException {
        dout.writeInt(code);
        dout.writeUTF(username);

        System.out.println("wrote " + this);
        dout.flush();
    }

    public static int inputtype(DataInputStream din) throws IOException {
        int message_code = din.readInt();
        //communication.username = din.readUTF();

        System.out.println("read " + message_code);
        return message_code;
    }

    public int getcode() {
        return code;
    }

    public void setcode(int code) {
        this.code = code;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public void username(String text) {
        this.username = text;
    }


    public String toString() {
        return code + username;
    }
}

