package com.syz.springcloud.hystrix.threadlocal.HystrixDome2;


/**
 *从demo1可以看到整个流程都是在同一个线程中执行的，也正确的获取到了ThreadLocal中的值，这种情况是没有问题的。

 接下来我们改造下程序，进行线程切换，将调用Dao中的call重启一个线程执行：
 */
public class CustomThreadLocal {
    static ThreadLocal<String> threadLocal = new ThreadLocal<>();

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