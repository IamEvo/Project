package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConversationRequest extends Communication implements Interfaces.ConversationRequest{

    private String recipientusername;

    public ConversationRequest() {
    }

    public ConversationRequest(int code, String username, String recipientusername) {
        super(code, username);
        this.recipientusername = recipientusername;
    }

    static ConversationRequest read(DataInputStream din) throws IOException {
        ConversationRequest conversationRequest = new ConversationRequest();
        conversationRequest.setcode(din.readInt());
        conversationRequest.username(din.readUTF());
        conversationRequest.recipientusername = din.readUTF();

        conversationRequest.toString();
        return conversationRequest;
    }

    public void write(DataOutputStream dout) throws IOException {
        dout.writeInt(this.getcode());
        dout.writeUTF(this.username());
        dout.writeUTF(recipientusername);

        System.out.println("wrote " + this);
        dout.flush();
    }

    public String toString() {
        return this.getcode()+ this.username() + recipientusername;
    }


}
