package org.fish.chat.common.dubbo.loadbalance;

import org.fish.chat.common.log.LoggerManager;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance;
import org.apache.commons.lang3.StringUtils;
import org.fish.chat.common.utils.ChannelSessionUtil;

import java.util.List;

/**
 * Comments for CidLoadBalance.java
 *
 * mqtt channel session load balance
 */
public class CidLoadBalance extends RoundRobinLoadBalance {

    /* (non-Javadoc)
     * @see com.alibaba.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance#doSelect(java.util.List, com.alibaba.dubbo.common.URL, com.alibaba.dubbo.rpc.Invocation)
     */
    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int length = invokers.size(); // 总个数
        if (length > 0) {
            //默认第二个为cid cid类型为long
            if (invocation.getArguments().length >= 2 && invocation.getParameterTypes().length >= 2
                    && invocation.getParameterTypes()[1] == long.class) {
                long cid = (Long) invocation.getArguments()[1];
                String ip = ChannelSessionUtil.getIpByChannelId(cid);
                for (Invoker<T> invoker : invokers) {
                    String invokerIp = invoker.getUrl().getIp();

                    if (StringUtils.equals(ip, invokerIp)) {
                        return invoker;
                    }
                }
                LoggerManager.warn("not found right invocation!!!!! cid=" + cid + ", ip=" + ip);
            }
            //没有找到轮询
            return super.doSelect(invokers, url, invocation);
        }
        return null;
    }
}
