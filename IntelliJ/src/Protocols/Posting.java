package Protocols;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Posting {
    private int code;
    private String username;
    private String password;

    private Posting() {
    }

    public Posting(int code, String username, String password) {
        this.code = code;
        this.username = username;
        this.password = password;
    }

    public static Posting read(DataInputStream din) throws IOException {
        Posting posting = new Posting();
        posting.code = din.readInt();
        posting.username = din.readUTF();
        posting.password = din.readUTF();

        System.out.println(" | read " + posting);
        return posting;
    }

    public void write(DataOutputStream dout) throws IOException {
        dout.writeInt(code);
        dout.writeUTF(username);
        dout.writeUTF(password);

        System.out.print("wrote " + this);
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

    public String password() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return code+ username + password;
    }
}
