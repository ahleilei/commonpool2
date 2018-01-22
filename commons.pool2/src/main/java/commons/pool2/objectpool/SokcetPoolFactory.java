package commons.pool2.objectpool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commons.pool2.SocketClient;
/**
 * 
 * 项目名称 : commons.pool2
 * 创建日期 : 2018年1月19日
 * 类  描  述 : 池工厂
 * 修改历史 : 
 *     1. [2018年1月19日]创建文件 by ziqiang.zhang
 */
public class SokcetPoolFactory implements PooledObjectFactory<SocketClient> {
    
    Logger log = LoggerFactory.getLogger(SokcetPoolFactory.class);
    
    @Override
    public PooledObject<SocketClient> makeObject() throws Exception {
        // 建立连接
        SocketClient socket = new SocketClient("127.0.0.1",8080);
        
        return new DefaultPooledObject<SocketClient>(socket);
    }

    @Override
    public void destroyObject(PooledObject<SocketClient> p) throws Exception {
        
        log.info("SokcetPoolFactory.destroyObject close socket");
        
        p.getObject().close();
    }

    @Override
    public boolean validateObject(PooledObject<SocketClient> p) {
        
        // 验证socket连接是否关闭      
        if(p.getObject().isClosed()){
            
            log.info("SokcetPoolFactory.validateObject socket isclosed");
            
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
    public void activateObject(PooledObject<SocketClient> p) throws Exception {
        
        
    }

    @Override
    public void passivateObject(PooledObject<SocketClient> p) throws Exception {
        
    }

    

    

}
