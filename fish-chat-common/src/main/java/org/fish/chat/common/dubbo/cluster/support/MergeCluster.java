package org.fish.chat.common.dubbo.cluster.support;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Cluster;
import com.alibaba.dubbo.rpc.cluster.Directory;

public class MergeCluster implements Cluster {

    public static final String NAME = "forking";

    public MergeCluster() {
    }

    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new MergeClusterInvoker(directory);
    }
}
