package com.syz.springcloud.hystrix.threadlocal.transmittableThreadLocalPoolDemo2;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *解决线程复用到时的数据被覆盖问题
 * 接下来使用transmittable-thread-local来改造有问题的代码
 *
 * 是阿里开源的transmittable-thread-local。
 GitHub地址：https://github.com/alibaba/transmittable-thread-local
 */
public class CustomThreadLocal {
    static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
    static ExecutorService pool = Executors.newFixedThreadPool(2);
    public static void main(String[] args) {
        for(int i=0;i<100;i++) {
            int j = i;
            pool.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    CustomThreadLocal.threadLocal.set("猿天地"+j);
                    new Service().call();
                }
            }));
        }
    }
}

class Service {
    public void call() {
        CustomThreadLocal.pool.execute(new Runnable() {
            @Override
            public void run() {
                new Dao().call();
            }
        });
    }
}

class Dao {
    public void call() {
        System.out.println("Dao:" + CustomThreadLocal.threadLocal.get());
    }
}
