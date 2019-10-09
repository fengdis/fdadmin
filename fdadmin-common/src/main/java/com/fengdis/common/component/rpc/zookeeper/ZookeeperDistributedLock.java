package com.fengdis.common.component.rpc.zookeeper;

import com.fengdis.common.util.PropertiesUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

/**
 * @version 1.0
 * @Descrittion: zookeeper分布式锁（单例模式）
 * @author: fengdi
 * @since: 2019/08/28 17:26
 */
public class ZookeeperDistributedLock {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //zk客户端
    private ZooKeeper zk;
    //zk是一个目录结构，root为最外层目录
    private String root = "/locks";
    //锁的名称
    private String lockName;
    //当前线程创建的序列node
    private ThreadLocal<String> nodeId = new ThreadLocal<>();
    //用来同步等待zkclient链接到了服务端
    private CountDownLatch connectedSignal = new CountDownLatch(1);
    private final static int sessionTimeout = 3000;
    private final static byte[] data = new byte[0];

    private static volatile ZookeeperDistributedLock zookeeperDistributedLock = null;

    public static ZookeeperDistributedLock getInstance(){
        if(zookeeperDistributedLock == null) {
            String config = PropertiesUtils.getString("zookeeper.host", "127.0.0.1:2181");
            zookeeperDistributedLock = new ZookeeperDistributedLock(config);
        }
        return zookeeperDistributedLock;
    }

    private ZookeeperDistributedLock(String config) {
        this.lockName = "locks";

        try {
            zk = new ZooKeeper(config, sessionTimeout, new Watcher() {

                @Override
                public void process(WatchedEvent event) {
                    // 建立连接
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        connectedSignal.countDown();
                    }
                }

            });

            connectedSignal.await();
            Stat stat = zk.exists(root, false);
            if (null == stat) {
                // 创建根节点
                zk.create(root, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class LockWatcher implements Watcher {
        private CountDownLatch latch = null;

        public LockWatcher(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void process(WatchedEvent event) {

            if (event.getType() == Event.EventType.NodeDeleted)
                latch.countDown();
        }
    }

    public void lock() {
        try {

            // 创建临时子节点
            String myNode = zk.create(root + "/" + lockName, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

            logger.info(Thread.currentThread().getName() + " " + myNode + " is created");

            // 取出所有子节点
            List<String> subNodes = zk.getChildren(root, false);
            TreeSet<String> sortedNodes = new TreeSet<>();
            for (String node : subNodes) {
                sortedNodes.add(root + "/" + node);
            }

            String smallNode = sortedNodes.first();
            String preNode = sortedNodes.lower(myNode);

            if (myNode.equals(smallNode)) {
                // 如果是最小的节点,则表示取得锁
                logger.info(Thread.currentThread().getName() + " " + myNode + " get lock");
                this.nodeId.set(myNode);
                return;
            }

            CountDownLatch latch = new CountDownLatch(1);
            Stat stat = zk.exists(preNode, new LockWatcher(latch));// 同时注册监听。
            // 判断比自己小一个数的节点是否存在,如果不存在则无需等待锁,同时注册监听
            if (stat != null) {
                logger.info(Thread.currentThread().getName() + " " + myNode + " waiting for " + root + "/" + preNode + " released lock");
                latch.await();// 等待，这里应该一直等待其他线程释放锁
                nodeId.set(myNode);
                latch = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void unlock() {
        try {
            logger.info(Thread.currentThread().getName() + " " + nodeId.get() + " unlock ");
            if (null != nodeId) {
                zk.delete(nodeId.get(), -1);
            }
            nodeId.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

}
