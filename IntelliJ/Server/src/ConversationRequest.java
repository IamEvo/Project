import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConversationRequest {

    private int code;
    private String senderusername;
    private String recipientusername;


    private ConversationRequest() {
    }

    public ConversationRequest(int code, String username, String recipientusername) {
        this.code = code;
        this.senderusername = username;
        this.recipientusername = recipientusername;
    }

    static ConversationRequest read(DataInputStream din) throws IOException {
        ConversationRequest conversationRequest = new ConversationRequest();
        //conversationRequest.code = din.readInt() ;
        conversationRequest.senderusername = din.readUTF();
        conversationRequest.recipientusername = din.readUTF();

        System.out.println("Conversation request between " + conversationRequest);
        return conversationRequest;
    }

//    public void write(DataOutputStream dout) throws IOException {
//        dout.writeInt(code);
//        dout.writeUTF(senderusername);
//        dout.writeUTF(recipientusername);
//
//        System.out.println("wrote " + this);
//        dout.flush();
//    }

    public int getcode() {
        return code;
    }

    public void setcode(int code) {
        this.code = code;
    }

    public String getSenderUsername(String username) {
        return senderusername;
    }

    public void setSenderUsername(String username) {
        this.senderusername = username;
    }

    public String getRecipientUsername() {
        return recipientusername;
    }

    public void setRecipientUsername(String username) {
        this.recipientusername = username;
    }

    public String toString() {
        return code + senderusername + recipientusername;
    }


}
