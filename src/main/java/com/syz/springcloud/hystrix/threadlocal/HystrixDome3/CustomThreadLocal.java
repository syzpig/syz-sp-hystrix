package com.syz.springcloud.hystrix.threadlocal.HystrixDome3;


import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *从demo2可以看到这次的请求是由2个线程共同完成的，在Service中还是可以拿到ThreadLocal的值，到了Dao中就拿不到了，因为线程已经切换了，这就是开始讲的ThreadLocal的数据会丢失的问题。

 那么怎么解决这个问题呢，其实也很简单，只需要改一行代码即可:
 */
public class CustomThreadLocal {
    //只需要修改2个地方,修饰线程池和替换InheritableThreadLocal，我们看下改造之后的效果：
    static TransmittableThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();
    static ExecutorService pool =  TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(2));


    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CustomThreadLocal.threadLocal.set("猿天地");
                new Service().call();
            }
        }).start();
    }

}

class Service {
    public void call() {
        System.out.println("Service:" + Thread.currentThread().getName());
        System.out.println("Service:" + CustomThreadLocal.threadLocal.get());
        //new Dao().call();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Dao().call();
            }
        }).start();
    }
}

class Dao {
    public void call() {
        System.out.println("==========================");
        System.out.println("Dao:" + Thread.currentThread().getName());
        System.out.println("Dao:" + CustomThreadLocal.threadLocal.get());
    }

}