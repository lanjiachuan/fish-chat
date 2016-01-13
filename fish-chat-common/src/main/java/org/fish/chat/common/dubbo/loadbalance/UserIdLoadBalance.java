/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.common.dubbo.loadbalance;

import org.fish.chat.common.log.LoggerManager;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance;

import java.util.List;

/**
 * Comments for CidLoadBalance.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年5月2日 下午6:39:54
 */
public class UserIdLoadBalance extends RoundRobinLoadBalance {

    /* (non-Javadoc)
     * @see com.alibaba.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance#doSelect(java.util.List, com.alibaba.dubbo.common.URL, com.alibaba.dubbo.rpc.Invocation)
     */
    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int length = invokers.size(); // 总个数
        if (length > 0) {
            //默认第一个为uid uid类型为long
            if (invocation.getArguments().length >= 1 && invocation.getParameterTypes().length >= 1
                    && invocation.getParameterTypes()[0] == long.class) {
                long uid = (Long) invocation.getArguments()[0];
                int index = (int) (uid % length);
                Invoker<T> invoker = invokers.get(index);
                LoggerManager.debug("UserIdLoadBalance: uid = " + uid + ", invoker.ip = " + invoker.getUrl().getIp()
                        + ", index=" + index + ", invokers.size()=" + length);
                return invoker;
            }
            LoggerManager.warn("UserIdConsistentHashLoadBalance usage has some wrong!");
            //没有找到轮询
            return super.doSelect(invokers, url, invocation);
        }
        return null;
    }
}
