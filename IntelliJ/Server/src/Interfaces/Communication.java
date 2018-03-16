package Interfaces;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Communication {

    public void write(DataOutputStream dout)throws IOException;

    public int code();

    public void code(int code);

    public String username();

    public void username(String text);

    public String toString();

}
