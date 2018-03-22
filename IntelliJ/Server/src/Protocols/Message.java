package Protocols;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Message {
    private int code;
    private String senderusername;
    private String recipientusername;
    private String message;

    private Message() {
    }

    public Message(int code, String senderusername, String recipientusername, String message) {
        this.code = code;
        this.senderusername = senderusername;
        this.recipientusername = recipientusername;
        this.message = message;
    }

    public static Message read(DataInputStream din) throws IOException {
        Message message = new Message();
        message.senderusername = din.readUTF();
        message.recipientusername = din.readUTF();
        message.message = din.readUTF();

        System.out.print("read " + message);
        return message;
    }

    public void write(DataOutputStream dout) throws IOException {
        dout.writeInt(code);
        dout.writeUTF(senderusername);
        dout.writeUTF(recipientusername);
        dout.writeUTF(message);

        System.out.println("wrote " + this);
        dout.flush();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSenderusername() {
        return senderusername;
    }

    public void setSenderusername(String senderusername) {
        this.senderusername = senderusername;
    }

    public String getRecipientusername() {
        return recipientusername;
    }

    public void setRecipientusername(String recipientusername) {
        this.recipientusername = recipientusername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return code+ senderusername + recipientusername + message;
    }
}
