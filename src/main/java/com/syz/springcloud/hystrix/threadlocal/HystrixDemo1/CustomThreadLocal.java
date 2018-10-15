package com.syz.springcloud.hystrix.threadlocal.HystrixDemo1;



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
        new Dao().call();
    }
}

class Dao {
    public void call() {
        System.out.println("==========================");
        System.out.println("Dao:" + Thread.currentThread().getName());
        System.out.println("Dao:" + CustomThreadLocal.threadLocal.get());
    }

}