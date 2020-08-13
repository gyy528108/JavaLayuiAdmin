package com.lowi.admin.pojo.vo;

/**
 * TreadDemo.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/11 11:28
 */
public class TreadDemo extends Thread {

    private volatile boolean flag = false;

    public boolean isFlag(){
        return flag;
    }

    @Override
    public void run(){
        flag = true;
        System.out.println("flag = " + flag);
    }
}
