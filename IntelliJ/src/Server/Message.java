package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Message {
    private int code;
    private String username;
    private String message;

    private Message() {
    }

    public Message(int code, String username, String message) {
        this.code = code;
        this.username = username;
        this.message = message;
    }

    static Message read(DataInputStream din) throws IOException {
        Message message = new Message();
        message.code = din.readInt();
        message.username = din.readUTF();
        message.message = din.readUTF();

        System.out.println("read " + message);
        return message;
    }

    public void write(DataOutputStream dout) throws IOException {
        dout.writeInt(code);
        dout.writeUTF(username);
        dout.writeUTF(message);

        System.out.println("wrote " + this);
        dout.flush();
    }

    public int code() {
        return code;
    }

    public void code(int code) {
        this.code = code;
    }

    public String text() {
        return username;
    }

    public void text(String text) {
        this.username = text;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String password) {
        this.message = message;
    }

    public String toString() {
        return code+ username + message;
    }
}
