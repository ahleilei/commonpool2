package commons.pool2.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;

import commons.pool2.SocketClient;
import commons.pool2.keyedobjectpool.SokcetKeyedPoolFactory;
import commons.pool2.objectpool.SokcetPoolFactory;

public class PoolTest {

    GenericKeyedObjectPool<String, SocketClient> keypool;

    GenericObjectPool<SocketClient> objectpool;
    
    @Before
    public void setUp() throws Exception {
        
        initObjectPool();
        
        initKeyedPool();
    }

    @Test
    public void testObjectPool() {
        
        SocketClient client = null;
        
        try {
            
            client = objectpool.borrowObject();
            
            client.send("103");
            
            String key = client.receive();
            
            client.setSecretKey(key);
            
            client.send("200");
            
            String resp = client.receive();
            
            System.out.println(resp);
                        
        } catch (Exception e) {
            
            e.printStackTrace();
        }finally{
            
            if(client!=null){
                objectpool.returnObject(client);               
            }            
        }
        
    }

    @Test
    public void testObjectKeyedPool() {

    }

    private void initObjectPool(){
        
        PooledObjectFactory<SocketClient> factory = new SokcetPoolFactory();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        // 池对象放入对象池的方式 true 队头 false 队尾
        config.setLifo(true);
        // 是否执行空闲链接检测 默认false
        config.setTestWhileIdle(true);
        // 空闲链接检测周期（毫秒）
        config.setTimeBetweenEvictionRunsMillis(10000);
        // 每次检测的空闲链接个数
        config.setNumTestsPerEvictionRun(1);
        // 最大连接数
        config.setMaxTotal(100);
        // 对应的最大连接数
        config.setMaxTotal(10);
        // 对应的最小空闲连接
        config.setMinIdle(1);
        // 对应的最大空闲连接
        config.setMaxIdle(1);
        // 最大等待时间（毫秒） 默认－1 一直等待
        config.setMaxWaitMillis(-1);
        // 是否阻塞等待
        config.setBlockWhenExhausted(true);
        // 是否在创建池对象时执行validate  默认false
        config.setTestOnCreate(false);
        // 是否在获取池对象时执行validate 默认false
        config.setTestOnBorrow(false);
        // 是否在归还池对象时执行validate 默认false
        config.setTestOnReturn(false);

        // 空闲最小时间，超时清除，不清除
        config.setMinEvictableIdleTimeMillis(3000000);
        // 空闲最小时间，保留最小空闲
        config.setSoftMinEvictableIdleTimeMillis(-1);
        
        objectpool = new GenericObjectPool<SocketClient>(factory,config);
    }
    
    private void initKeyedPool() {
        KeyedPooledObjectFactory<String, SocketClient> factory = new SokcetKeyedPoolFactory();
        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        // 池对象放入对象池的方式 true 队头 false 队尾
        config.setLifo(true);
        // 是否执行空闲链接检测 默认false
        config.setTestWhileIdle(true);
        // 空闲链接检测周期（毫秒）
        config.setTimeBetweenEvictionRunsMillis(10000);
        // 每次检测的空闲链接个数
        config.setNumTestsPerEvictionRun(1);
        // 最大连接数
        config.setMaxTotal(100);
        // 每个key对应的最大连接数
        config.setMaxTotalPerKey(1);
        // 每个key对应的最小空闲连接
        config.setMinIdlePerKey(0);
        // 每个key对应的最大空闲连接
        config.setMaxIdlePerKey(1);
        // 最大等待时间（毫秒） 默认－1 一直等待
        config.setMaxWaitMillis(-1);
        // 是否阻塞等待
        config.setBlockWhenExhausted(true);
        // 是否在创建池对象时执行validate  默认false
        config.setTestOnCreate(false);
        // 是否在获取池对象时执行validate 默认false
        config.setTestOnBorrow(false);
        // 是否在归还池对象时执行validate 默认false
        config.setTestOnReturn(false);

        // 空闲最小时间，超时清除，不清除
        config.setMinEvictableIdleTimeMillis(3000000);
        // 空闲最小时间，保留最小空闲
        config.setSoftMinEvictableIdleTimeMillis(-1);

        keypool = new GenericKeyedObjectPool<>(factory, config);
    }
}
