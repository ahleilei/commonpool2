package commons.pool2;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * 项目名称 : commons.pool2
 * 创建日期 : 2018年1月19日
 * 类  描  述 : pool object
 * 修改历史 : 
 *     1. [2018年1月19日]创建文件 by ziqiang.zhang
 */
public class SocketClient extends Socket {
    
    static Logger log = LoggerFactory.getLogger(SocketClient.class);
    
    public String secretKey;
        
    public SocketClient(String host, int port) throws UnknownHostException, IOException {
        super(host, port);
    }

    public void send(String message){
        
        try {
            
            log.info("SocketClient.send " + message);
            
            byte[] b = message.getBytes(Charset.forName("utf-8"));
            
            byte[] r = new byte[b.length + 2];
            
            r[0] = (byte)(b.length>>8);
            
            r[1] = (byte)b.length;
            
            System.arraycopy(b, 0, r, 2, b.length);
            
            this.getOutputStream().write(r);
            
            this.getOutputStream().flush();
            
        } catch (Exception e) {
            
            log.error("SocketClient.send",e);
            
            try {
                //关闭连接
                this.close();
            } catch (IOException e1) {
               
                e1.printStackTrace();
            }
            
        }
    }
    
    public String receive(){  
        
        try {
            
            DataInputStream in = new DataInputStream(this.getInputStream());
            
            int length = in.readShort();
            
            byte[] r = new byte[length];
            
            in.read(r);                        
            
            String res = new String(r,"utf-8");
            
            log.info("SocketClient.receive "+ res);
            
            return res;
            
        } catch (Exception e) {
            
            log.error("SocketClient.receive",e);
            try {
                this.close();
            } catch (IOException e1) {
                
                e1.printStackTrace();
            }
        }       
        
        return null;
    }
    
    
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
