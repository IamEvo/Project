package Protocols;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AuthConfirmationRequest {

    private int code;
    private String username;
    private Boolean result;

    private AuthConfirmationRequest() {
    }

    public AuthConfirmationRequest(int code, String username, Boolean result) {
        this.code = code;
        this.username = username;
        this.result = result;
    }

    public static AuthConfirmationRequest read(DataInputStream din) throws IOException {
        AuthConfirmationRequest authConfirmationRequest = new AuthConfirmationRequest();
        authConfirmationRequest.username = din.readUTF();
        authConfirmationRequest.result = din.readBoolean();
        System.out.print("Read: " + authConfirmationRequest.toString());
        return authConfirmationRequest;
    }

    public void write(DataOutputStream dout) throws IOException {
        dout.writeInt(code);
        dout.writeUTF(username);
        dout.writeBoolean(result);

        System.out.println(" | Wrote: " + this.toString());
        dout.flush();
    }

    public int getcode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String toString() {
        return code + username + result;
    }

}
