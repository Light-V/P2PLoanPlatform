package com.scut.p2ploanplatform.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 自动触发器，按照每日指定时间定时触发（独立线程）
 * 若存在@Autowired字段，则会等待所有@Autowired注入之后才完成
 * （PS：通过Setter方式注入的请在字段上加上@com.scut.p2ploanplatform.utils.AutowireField，实例可以参考com.scut.p2ploanplatform.service.impl.RepayServiceImpl）
 */
@Getter
@Slf4j
public class AutoTrigger implements Runnable {

    private Method invokeMethod;
    private Object[] invokeArgs;
    private Object invokeObject;

    private int triggerHour;
    private int triggerMinute;
    private int triggerSecond;

    private boolean triggerAfterInstantiation;

    /**
     * 默认构造函数
     *
     * @param invokeMethod              回调方法，如调用foo()，则为getClass().getDeclaredMethod("foo")
     * @param invokeObject              调用该方法的实例化对象，如this，则相当于this.foo()
     * @param triggerHour               每日触发的小时（0~23）（注意：时区为UTC）
     * @param triggerMinute             每日触发的分钟（0~59）
     * @param triggerSecond             每日触发的秒数（0~59）
     * @param triggerAfterInstantiation 是否在实例化后立刻触发回调
     * @param invokeArgs                回调函数的参数（可选）
     */
    public AutoTrigger(Method invokeMethod, Object invokeObject, int triggerHour, int triggerMinute, int triggerSecond, boolean triggerAfterInstantiation, Object... invokeArgs) throws IllegalArgumentException {
        if (triggerHour < 0 || triggerHour > 23)
            throw new IllegalArgumentException("trigger hour should between 0 and 23");
        if (triggerMinute < 0 || triggerMinute > 59)
            throw new IllegalArgumentException("trigger minute should between 0 and 59");
        if (triggerSecond < 0 || triggerSecond > 59)
            throw new IllegalArgumentException("trigger second should between 0 and 59");
        this.invokeMethod = invokeMethod;
        this.invokeArgs = invokeArgs;
        this.invokeObject = invokeObject;
        this.triggerHour = triggerHour;
        this.triggerMinute = triggerMinute;
        this.triggerSecond = triggerSecond;
        this.triggerAfterInstantiation = triggerAfterInstantiation;

        Thread thread = new Thread(this);
        thread.setName("TriggerThread");
        thread.setDaemon(true);
        thread.start();
    }

    private void waitFieldInitialization() throws Exception {
        if (invokeObject == null)
            return;
        Class c = invokeObject.getClass();
        while (true) {
            boolean isAllFieldSet = true;
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class) || field.isAnnotationPresent(AutowireField.class)) {
                    field.setAccessible(true);
                    Object fieldValue = field.get(invokeObject);
                    if (fieldValue == null) {
                        isAllFieldSet = false;
                        break;
                    }
                }
            }
            if (isAllFieldSet)
                break;
            Thread.sleep(100);
        }
    }

    private void doInvoke() throws Exception {
        waitFieldInitialization();
        invokeMethod.invoke(invokeObject, invokeArgs);
    }

    @Override
    public void run() {
        try {
            long triggerTimeOfDay = (triggerSecond + (60 * (60 * triggerHour + triggerMinute))) * 1000;
            Date now = new Date();
            long nextTriggerDate = now.getTime() / 86400000; //floor
            if (now.getTime() % 86400000 >= triggerTimeOfDay)
                nextTriggerDate++;
            long nextTriggerTimestamp = nextTriggerDate * 86400000 + triggerTimeOfDay;

            if (triggerAfterInstantiation) {
                log.info("Started trigger invocation: " + invokeMethod.toString());
                doInvoke();
            }

            //noinspection InfiniteLoopStatement
            while (true) {
                long sleepTimeForNextDay = (nextTriggerTimestamp - new Date().getTime());
                if (sleepTimeForNextDay > 0)
                    Thread.sleep(sleepTimeForNextDay);

                log.info("Started trigger invocation: " + invokeMethod.toString());
                doInvoke();

                nextTriggerTimestamp += 86400000;
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionDetails = sw.toString();
            log.error(exceptionDetails);

            // 重试
            Thread thread = new Thread(() -> {
                try {
                    log.warn("Unhandled exception caught in auto trigger thread, restarting in 1 hours");
                    Thread.sleep(3600000);
                    new AutoTrigger(invokeMethod, invokeObject, triggerHour, triggerMinute, triggerSecond, triggerAfterInstantiation, invokeArgs);
                } catch (InterruptedException ignore) {
                }
            });

            thread.setDaemon(true);
            thread.setName("TriggerRetryWaitThread");
            thread.start();
        }
    }
}
