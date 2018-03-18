package Interfaces;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Communication {

    public void write(DataOutputStream dout)throws IOException;

    public int getcode();

    public void setcode(int code);

    public String username();

    public void username(String text);

    public String toString();

}
