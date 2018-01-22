package commons.pool2.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

public class NettyServerTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() throws UnknownHostException, IOException {
        
        Socket socket = new Socket("127.0.0.1", 8080);
        
        DataInputStream in = new DataInputStream(socket.getInputStream());
        
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        
        String message = "103";
//        MessagePack pack = new MessagePack();
        byte[] b =message.getBytes();// pack.write(message);
        
        byte[] r = new byte[b.length+2];
        
        r[0] = (byte)(b.length>>8);
        r[1] = (byte)b.length;
        
        System.arraycopy(b, 0, r, 2, b.length);
        
        out.write(r);
        out.flush();
        
        
        int length = in.readShort();
        
        byte[] res = new byte[length];
        
        in.read(res);
        
        System.out.println(new String(res,"utf-8"));
        
        in.close();
        out.close();
        socket.close();
    }    

}
