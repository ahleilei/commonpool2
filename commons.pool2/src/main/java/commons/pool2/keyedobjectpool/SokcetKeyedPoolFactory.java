package commons.pool2.keyedobjectpool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commons.pool2.SocketClient;

public class SokcetKeyedPoolFactory implements KeyedPooledObjectFactory<String, SocketClient> {

    Logger log = LoggerFactory.getLogger(SokcetKeyedPoolFactory.class);
    
    @Override
    public PooledObject<SocketClient> makeObject(String key) throws Exception {
        
        // 建立连接
        SocketClient socket = new SocketClient("127.0.0.1",8080);
        
        return new DefaultPooledObject<SocketClient>(socket);
    }

    @Override
    public void destroyObject(String key, PooledObject<SocketClient> p) throws Exception {
        
        log.info("SokcetPoolFactory.SokcetKeyedPoolFactory close socket");
        
        p.getObject().close();
    }

    @Override
    public boolean validateObject(String key, PooledObject<SocketClient> p) {
        // 验证socket连接是否关闭      
        if(p.getObject().isClosed()){
            
            log.info("SokcetPoolFactory.SokcetKeyedPoolFactory socket isclosed");
            
            return false;
        }
        
        p.getObject().send("100");
        
        String res = p.getObject().receive();
        
        if(res == null||!"100".equals(res)){
            
            return false;
        }
        
        return true;
    }

    @Override
    public void activateObject(String key, PooledObject<SocketClient> p) throws Exception {
        
    }

    @Override
    public void passivateObject(String key, PooledObject<SocketClient> p) throws Exception {
        
    }

}
