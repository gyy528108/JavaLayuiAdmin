package com.lowi.admin.utils;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * SingleDemo.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/5 15:53
 */
public class SingleDemo {

    private static volatile SingleDemo singleDemo = null;

    public static SingleDemo getInstance() {
        Instant now = Instant.now();
        System.out.println("now = " + now);
        if (singleDemo == null) {
            synchronized (SingleDemo.class) {
                if (singleDemo == null) {
                    singleDemo = new SingleDemo();

                    LongAdder longAdder = new LongAdder();
                    AtomicInteger atomicInteger = new AtomicInteger(0);
                    int andGet = atomicInteger.addAndGet(1);

                }
            }
        }
        return singleDemo;
    }
}
