package com.fengdis.common.component.rpc.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.fengdis.common.util.PropertiesUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * @version 1.0
 * @Descrittion: zookeeper工具类（单例模式）
 * @author: fengdi
 * @since: 2019/08/28 17:26
 */
public class ZookeeperUtils {

    private ZooKeeper zookeeper;

    //用来同步等待zkClient连接到了客户端
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private static volatile ZookeeperUtils zookeeperUtils= null;

    public static ZookeeperUtils getInstance() {
        if (zookeeperUtils == null){
            synchronized (ZookeeperUtils.class){
                if (zookeeperUtils == null){
                    zookeeperUtils = new ZookeeperUtils();
                }
            }
        }
        return zookeeperUtils;
    }

    private ZookeeperUtils() {
        try {
            zookeeper = new ZooKeeper(PropertiesUtils.getString("zookeeper.host","127.0.0.1:2181"), Integer.valueOf(PropertiesUtils.getString("zookeeper.timeout","5000")), new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
                        countDownLatch.countDown();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建znode结点
     * @param path 结点路径
     * @param data 结点数据
     * @return true 创建结点成功 false表示结点存在
     * @throws Exception
     */
    public boolean addZnodeData(String path,String data,CreateMode mode) {
        try {
            if(zookeeper.exists(path, true) == null){
                zookeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, mode);
                return true;
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException("创建znode："+path+"出现问题！！",e);
        }
        System.out.println("znode"+path+"结点已存在");
        return false;
    }

    /**
     * 创建永久znode结点
     * @param path 结点路径
     * @param data 结点数据
     * @return true 创建结点成功 false表示结点存在
     * @throws Exception
     */
    public boolean addPZnode(String path,String data) {
        return addZnodeData(path,data,CreateMode.PERSISTENT);
    }

    /**
     * 创建临时znode结点
     * @param path 结点路径
     * @param data 结点数据
     * @return true 创建结点成功 false表示结点存在
     * @throws Exception
     */
    public boolean addZEnode(String path,String data) {
        return addZnodeData(path,data,CreateMode.EPHEMERAL);
    }

    /**
     * 修改znode
     * @param path 结点路径
     * @param data 结点数据
     * @return  修改结点成功   false表示结点不存在
     */
    public boolean updateZnode(String path,String data){
        try {
            Stat stat=null;
            if((stat=zookeeper.exists(path, true))!=null){
                zookeeper.setData(path, data.getBytes(), stat.getVersion());
                return true;
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException("修改znode："+path+"出现问题！！",e);
        }
        return false;
    }
    /**
     *
     * 删除结点
     * @param path 结点
     * @return true 删除键结点成功  false表示结点不存在
     */
    public boolean deleteZnode(String path){
        try {
            Stat stat=null;
            if((stat=zookeeper.exists(path, true))!=null){
                List<String> subPaths=zookeeper.getChildren(path, false);
                if(subPaths.isEmpty()){
                    zookeeper.delete(path, stat.getVersion());
                    return true;
                }else{
                    for (String subPath : subPaths) {
                        deleteZnode(path+"/"+subPath);
                    }
                }
            }
        } catch (InterruptedException | KeeperException e) {
            throw new RuntimeException("删除znode："+path+"出现问题！！",e);
        }
        return false;
    }
    /**
     * 取到结点数据
     * @param path 结点路径
     * @return null表示结点不存在 否则返回结点数据
     */
    public String getZnodeData(String path){
        String data=null;
        try {
            Stat stat=null;
            if((stat=zookeeper.exists(path, true))!=null){
                data=new String(zookeeper.getData(path, true, stat));
            }else{
                System.out.println("znode:"+path+",不存在");
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException("取到znode："+path+"出现问题！！",e);
        }
        return data;
    }

}
