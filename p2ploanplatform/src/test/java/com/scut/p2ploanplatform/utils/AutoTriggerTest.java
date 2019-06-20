package com.scut.p2ploanplatform.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

@SuppressWarnings("WeakerAccess")
public class AutoTriggerTest {

    private int noArgumentCallbackTriggered = 0;
    private int argumentCallbackTriggered = 0;
    private static int staticCallbackTriggered = 0;

    public synchronized void noArgumentCallback() {
        noArgumentCallbackTriggered++;
    }

    public synchronized void argumentCallback(boolean raiseException) throws Exception {
        argumentCallbackTriggered++;
        if (raiseException)
            throw new Exception("TEST EXCEPTION");
    }

    public synchronized static void staticCallback() {
        staticCallbackTriggered++;
    }

    @Test
    public void triggerTest() throws Exception{
        // todo: 修改为强线程同步测试
        // DELAY 5 SECONDS to trigger
        Date date = new Date(new Date().getTime() + 5000);
        int hour = (int)((date.getTime() % 86400000) / 3600000);
        int minute = (int)((date.getTime() % 3600000) / 60000);
        int second = (int)((date.getTime() % 60000) / 1000);

        new AutoTrigger(getClass().getDeclaredMethod("noArgumentCallback"), this, hour, minute, second, true);
        new AutoTrigger(getClass().getDeclaredMethod("noArgumentCallback"), this, hour, minute, second, false);
        new AutoTrigger(getClass().getDeclaredMethod("argumentCallback", boolean.class), this, hour, minute, second, true, true);
        new AutoTrigger(getClass().getDeclaredMethod("staticCallback"), null, hour, minute, second, true);
        Thread.sleep(10000);

        assertEquals(3, noArgumentCallbackTriggered);
        assertEquals(1, argumentCallbackTriggered);
        assertEquals(2, staticCallbackTriggered);

        new AutoTrigger(getClass().getDeclaredMethod("noArgumentCallback"), this, hour, minute, second, true);
        new AutoTrigger(getClass().getDeclaredMethod("noArgumentCallback"), this, hour, minute, second, false);
        Thread.sleep(5000);
        assertEquals(4, noArgumentCallbackTriggered);
    }
}