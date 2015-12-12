package org.fish.chat.common.dubbo.cluster.support;

import cn.techwolf.common.log.LoggerManager;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.cluster.Directory;
import com.alibaba.dubbo.rpc.cluster.LoadBalance;
import com.alibaba.dubbo.rpc.cluster.support.AbstractClusterInvoker;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liujun on 15/3/5.
 */
public class MergeClusterInvoker<T> extends AbstractClusterInvoker<T> {

    private final ExecutorService executor = Executors.newCachedThreadPool(new NamedThreadFactory("merge-cluster-timer", true));

    public MergeClusterInvoker(Directory<T> directory) {
        super(directory);
    }

    @Override
    protected Result doInvoke(final Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance) throws RpcException {
        this.checkInvokers(invokers, invocation);
        int timeout = this.getUrl().getParameter("timeout", 1000);

        final Object selected = invokers;

        RpcContext.getContext().setInvokers((List) selected);
        final CountDownLatch countDownLatch = new CountDownLatch(invokers.size());

        final List<Result> resultList = new ArrayList<Result>();
        Iterator e = ((List) selected).iterator();

        while (e.hasNext()) {
            final Invoker e1 = (Invoker) e.next();
            this.executor.execute(new Runnable() {
                public void run() {
                    try {
                        Result result = e1.invoke(invocation);
                        resultList.add(result);
                    } catch (Throwable e) {
                        LoggerManager.error("", e);
                    } finally {
                        countDownLatch.countDown();
                    }

                }
            });
        }

        try {
            //先不超时，后期优化
            countDownLatch.await();

            if (resultList.size() > 0) {
                Result result = resultList.get(0);
                Object value = result.getValue();
                if (result instanceof Collection) {
                    for(int i =1; i< resultList.size();i++) {
                        ((Collection) value).addAll((Collection) resultList.get(i).getValue());
                    }
                }
                else  if(result instanceof Map) {
                    for(int i =1; i< resultList.size();i++) {
                        ((Map) value).putAll((Map) resultList.get(i).getValue());
                    }
                }

                return result;
            }
            return null;
        } catch (InterruptedException var11) {
            throw new RpcException("Failed to forking invoke provider " + selected + ", but no luck to perform the invocation. Last error is: " + var11.getMessage(), var11);
        }
    }
}
