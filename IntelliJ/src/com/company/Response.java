package com.company;

import Interfaces.Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Response implements Communication {

    /**
     * Code 1 = Register, Code 2 = Login
     */
    private int code;
    private Boolean errorcode1;
    private Boolean errorcode2;
    private String username;

    private Response() {
    }

    public Response(int code, Boolean errorcode1, Boolean errorcode2, String username) {
        this.code = code;
        this.username = username;
        this.errorcode1 = errorcode1;
        this.errorcode2 = errorcode2;
    }

    static Response read(DataInputStream din) throws IOException {
        Response response = new Response();
        //response.code = din.readInt();
        response.errorcode1 = din.readBoolean();
        response.errorcode2 = din.readBoolean();
        response.username = din.readUTF();

        System.out.println("read " + response);
        return response;
    }

    public void write(DataOutputStream dout) throws IOException {
        dout.writeInt(code);
        dout.writeBoolean(errorcode1);
        dout.writeBoolean(errorcode2);
        dout.writeUTF(username);

        System.out.println("wrote " + this);
        dout.flush();
    }

    @Override
    public int getcode() {
        return code;
    }

    @Override
    public void setcode(int code) {
        this.code = code;
    }

    @Override
    public String username() {
        return null;
    }

    @Override
    public void username(String username) {
        this.username = username;
    }

    public String toString() {
        return code + username + errorcode1 + errorcode2;
    }

}
